package dedrarion.client.render;

import dedrarion.content.item.MagicDetectorItem;
import dedrarion.content.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * Client-side passive firefly + puddle particle system for the Magic Detector.
 *
 * <h3>Fireflies</h3>
 * Orbit the player's hand continuously while the detector is held.
 * Count and speed scale with nearby ore count (radius 10).
 * Drift subtly toward the nearest ore cluster.
 *
 * <h3>Puddle (no-texture prototype)</h3>
 * When ores are highlighted via {@link OreHighlightTarget},
 * exposed ore faces emit slow-falling GLOW particles.
 * No custom block or texture needed.
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireflySystem {

    // --- Cached ore scan state ---

    private static int     nearbyOreCount  = 0;
    private static Vec3    nearestOreDir   = null;  // unit vector from player toward nearest ore
    private static long    lastScanTick    = Long.MIN_VALUE;

    private static final int SCAN_INTERVAL    = 40;  // ticks between ore scans
    private static final int SCAN_RADIUS      = 10;  // blocks
    private static final int FIREFLY_INTERVAL = 3;   // ticks between firefly spawns
    private static final int PUDDLE_INTERVAL  = 6;   // ticks between puddle particle spawns

    // --- Event handler ---

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level  = mc.level;

        if (player == null || level == null || mc.isPaused()) return;

        long gameTime = level.getGameTime();

        // Puddle particles run regardless of whether detector is held,
        // because OreHighlightTarget entries persist 10 seconds after scanning.
        if (gameTime % PUDDLE_INTERVAL == 0) {
            spawnPuddleParticles(level);
        }

        if (!isHoldingDetector(player)) {
            // Reset cached data so fireflies don't linger
            nearbyOreCount = 0;
            nearestOreDir  = null;
            return;
        }

        // Periodic ore scan (client-side read-only, cheap enough at 40-tick interval)
        if (gameTime - lastScanTick >= SCAN_INTERVAL) {
            scanNearbyOres(level, player);
            lastScanTick = gameTime;
        }

        // Firefly spawn
        if (gameTime % FIREFLY_INTERVAL == 0) {
            spawnFireflies(level, player, gameTime);
        }
    }

    // --- Fireflies ---

    private static void spawnFireflies(ClientLevel level, LocalPlayer player, long gameTime) {
        int count = fireflyCount();
        if (count == 0) return;

        RandomSource rng = level.getRandom();
        Vec3 hand = getHandPos(player);

        for (int i = 0; i < count; i++) {
            // Evenly distribute base angles, then add slow rotation over time
            double baseAngle = (2 * Math.PI / count) * i;
            double timeAngle = gameTime * 0.04;
            double angle     = baseAngle + timeAngle;

            float orbitR = 0.18f + rng.nextFloat() * 0.12f;

            // Nudge orbit toward nearest ore
            double bx = 0, bz = 0;
            if (nearestOreDir != null && nearbyOreCount > 0) {
                float bias = Math.min(nearbyOreCount / 25f, 0.35f);
                bx = nearestOreDir.x * bias;
                bz = nearestOreDir.z * bias;
            }

            double px = hand.x + Math.cos(angle) * orbitR + bx;
            double py = hand.y + (rng.nextFloat() - 0.5) * 0.25;
            double pz = hand.z + Math.sin(angle) * orbitR + bz;

            // Speed scales with ore density for visual urgency
            float speed = nearbyOreCount > 10 ? 0.05f : 0.015f;

            level.addParticle(ParticleTypes.GLOW,
                    px, py, pz,
                    (rng.nextFloat() - 0.5) * speed,
                    (rng.nextFloat() - 0.3) * speed * 0.4,
                    (rng.nextFloat() - 0.5) * speed);
        }
    }

    private static int fireflyCount() {
        if (nearbyOreCount == 0)  return 1;
        if (nearbyOreCount < 5)   return 3;
        if (nearbyOreCount < 15)  return 6;
        return 10;
    }

    /** Approximate main-hand position at player shoulder level. */
    private static Vec3 getHandPos(LocalPlayer player) {
        double yawRad = Math.toRadians(player.getYRot());
        return new Vec3(
                player.getX() - Math.sin(yawRad) * 0.38,
                player.getY() + player.getEyeHeight() - 0.38,
                player.getZ() + Math.cos(yawRad) * 0.38
        );
    }

    // --- Ore scan ---

    private static void scanNearbyOres(ClientLevel level, LocalPlayer player) {
        BlockPos center = player.blockPosition();
        int count = 0;
        double bestDist = Double.MAX_VALUE;
        Vec3 bestDir = null;

        for (int x = -SCAN_RADIUS; x <= SCAN_RADIUS; x++) {
            for (int y = -SCAN_RADIUS; y <= SCAN_RADIUS; y++) {
                for (int z = -SCAN_RADIUS; z <= SCAN_RADIUS; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    if (!level.getBlockState(pos).is(ModTags.Blocks.METAL_DETECTOR_VALUABLES)) continue;
                    count++;
                    double d = x * x + y * y + z * z;
                    if (d < bestDist) {
                        bestDist = d;
                        bestDir  = new Vec3(x, y, z).normalize();
                    }
                }
            }
        }

        nearbyOreCount = count;
        nearestOreDir  = bestDir;
    }

    // --- Puddle particles ---

    /**
     * For every active highlighted ore that has at least one exposed face (adjacent air),
     * emit a slow-falling GLOW particle from that face.
     * Acts as a no-texture "puddle" — visible seep from ore faces.
     */
    private static void spawnPuddleParticles(ClientLevel level) {
        List<OreHighlightTarget.Entry> entries = OreHighlightTarget.getEntries();
        if (entries.isEmpty()) return;

        RandomSource rng = level.getRandom();

        for (OreHighlightTarget.Entry entry : entries) {
            BlockPos pos = entry.pos();

            // Alpha fades toward end of highlight duration — skip low-alpha entries
            if (entry.alpha() < 0.2f) continue;

            // Find an exposed face (prefer DOWN for puddle-drip feel)
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.relative(dir);
                if (!level.isEmptyBlock(neighbor)) continue;

                // Spawn probability: roughly 1-2 particles per exposed ore per 6 ticks
                if (rng.nextFloat() > 0.25f) break;

                double fx = pos.getX() + 0.5 + dir.getStepX() * 0.52 + (rng.nextFloat() - 0.5) * 0.4;
                double fy = pos.getY() + 0.5 + dir.getStepY() * 0.52 + (rng.nextFloat() - 0.5) * 0.3;
                double fz = pos.getZ() + 0.5 + dir.getStepZ() * 0.52 + (rng.nextFloat() - 0.5) * 0.4;

                level.addParticle(ParticleTypes.GLOW,
                        fx, fy, fz,
                        0, -0.008, 0);
                break; // One face per ore per tick is enough
            }
        }
    }

    // --- Helpers ---

    private static boolean isHoldingDetector(LocalPlayer player) {
        return player.getMainHandItem().getItem() instanceof MagicDetectorItem
                || player.getOffhandItem().getItem() instanceof MagicDetectorItem;
    }
}
