package dedrarion.client.render;

import dedrarion.client.particle.SoftGlowParticle;
import dedrarion.config.ModConfigValues;
import dedrarion.content.item.MagicDetectorItem;
import dedrarion.content.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

/**
 * Client-side ambient firefly particle system for the Magic Detector.
 * <p>
 * Fireflies orbit the player's hand continuously while the detector is held.
 * Uses {@link SoftGlowParticle} for a soft, warm glow instead of vanilla GLOW
 * particles. Count and behavior scale with nearby ore density.
 * <p>
 * Compared to the previous implementation:
 * <ul>
 *   <li>No puddle particles — replaced by {@link OreMarkerOverlay} HUD.</li>
 *   <li>Uses custom soft particle for ambient feel.</li>
 *   <li>Subtle color tinting based on detector mode.</li>
 * </ul>
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FireflySystem {

    // --- Cached ore scan state ---

    private static int     nearbyOreCount  = 0;
    private static Vec3    nearestOreDir   = null;  // unit vector from player toward nearest ore
    private static long    lastScanTick    = Long.MIN_VALUE;

    // --- Color presets per mode ---

    private static final float[] COLOR_RAW = {1.0f, 0.75f, 0.35f};       // Warm amber
    private static final float[] COLOR_ETHEREAL = {0.7f, 0.85f, 1.0f};   // Cool blue-white
    private static final float[] COLOR_FULL = {0.85f, 0.7f, 1.0f};       // Soft purple
    private static final float[] COLOR_IDLE = {0.9f, 0.9f, 0.7f};        // Warm white

    // --- Event handler ---

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level  = mc.level;

        if (player == null || level == null || mc.isPaused()) return;
        if (!isHoldingDetector(player)) {
            // Reset cached data so fireflies don't linger
            nearbyOreCount = 0;
            nearestOreDir  = null;
            return;
        }

        long gameTime = level.getGameTime();
        int scanInterval = ModConfigValues.fireflyScanInterval.get();
        int spawnInterval = ModConfigValues.fireflySpawnInterval.get();

        // Periodic ore scan
        if (gameTime - lastScanTick >= scanInterval) {
            scanNearbyOres(level, player);
            lastScanTick = gameTime;
        }

        // Firefly spawn
        if (gameTime % spawnInterval == 0) {
            spawnFireflies(level, player, gameTime);
        }
    }

    // --- Fireflies ---

    private static void spawnFireflies(ClientLevel level, LocalPlayer player, long gameTime) {
        int count = fireflyCount();
        if (count == 0) return;

        RandomSource rng = level.getRandom();
        Vec3 hand = getHandPos(player);
        float[] color = getFireflyColor(player);

        Minecraft mc = Minecraft.getInstance();

        for (int i = 0; i < count; i++) {
            // Evenly distribute base angles, then add slow rotation over time
            double baseAngle = (2 * Math.PI / count) * i;
            double timeAngle = gameTime * 0.03;
            double angle     = baseAngle + timeAngle;

            float orbitR = 0.15f + rng.nextFloat() * 0.12f;

            // Nudge orbit toward nearest ore
            double bx = 0, bz = 0;
            if (nearestOreDir != null && nearbyOreCount > 0) {
                float bias = Math.min(nearbyOreCount / 30f, 0.3f);
                bx = nearestOreDir.x * bias;
                bz = nearestOreDir.z * bias;
            }

            double px = hand.x + Math.cos(angle) * orbitR + bx;
            double py = hand.y + (rng.nextFloat() - 0.5) * 0.2;
            double pz = hand.z + Math.sin(angle) * orbitR + bz;

            // Gentle velocity
            float speed = nearbyOreCount > 10 ? 0.02f : 0.008f;
            double vx = (rng.nextFloat() - 0.5) * speed;
            double vy = (rng.nextFloat() - 0.3) * speed * 0.3;
            double vz = (rng.nextFloat() - 0.5) * speed;

            // Slight color variation per firefly
            float cr = Mth.clamp(color[0] + (rng.nextFloat() - 0.5f) * 0.1f, 0f, 1f);
            float cg = Mth.clamp(color[1] + (rng.nextFloat() - 0.5f) * 0.1f, 0f, 1f);
            float cb = Mth.clamp(color[2] + (rng.nextFloat() - 0.5f) * 0.1f, 0f, 1f);

            // Size varies: more ores = slightly larger
            float size = nearbyOreCount > 5 ? 0.08f + rng.nextFloat() * 0.06f
                    : 0.05f + rng.nextFloat() * 0.04f;

            int lifetime = 20 + rng.nextInt(20);

            SoftGlowParticle particle = SoftGlowParticle.create(
                    level, px, py, pz, vx, vy, vz,
                    cr, cg, cb, 0.5f, size, lifetime
            );

            if (particle != null) {
                mc.particleEngine.add(particle);
            }
        }
    }

    private static int fireflyCount() {
        if (nearbyOreCount == 0)  return 1;
        if (nearbyOreCount < 5)   return 2;
        if (nearbyOreCount < 15)  return 4;
        return 6;
    }

    /** Approximate main-hand position at player shoulder level. */
    private static Vec3 getHandPos(LocalPlayer player) {
        double yawRad = Math.toRadians(player.getYRot());
        return new Vec3(
                player.getX() - Math.sin(yawRad) * 0.35,
                player.getY() + player.getEyeHeight() - 0.4,
                player.getZ() + Math.cos(yawRad) * 0.35
        );
    }

    /**
     * Returns the firefly tint color based on the current detector mode.
     */
    private static float[] getFireflyColor(LocalPlayer player) {
        MagicDetectorItem det = getDetectorItem(player);
        if (det == null) return COLOR_IDLE;

        net.minecraft.world.item.ItemStack stack = player.getMainHandItem().getItem() instanceof MagicDetectorItem
                ? player.getMainHandItem() : player.getOffhandItem();

        return switch (det.getMode(stack)) {
            case RAW -> COLOR_RAW;
            case ETHEREAL -> COLOR_ETHEREAL;
            case FULL -> COLOR_FULL;
        };
    }

    // --- Ore scan ---

    private static void scanNearbyOres(ClientLevel level, LocalPlayer player) {
        BlockPos center = player.blockPosition();
        int scanRadius = ModConfigValues.fireflyScanRadius.get();
        int count = 0;
        double bestDist = Double.MAX_VALUE;
        Vec3 bestDir = null;

        for (int x = -scanRadius; x <= scanRadius; x++) {
            for (int y = -scanRadius; y <= scanRadius; y++) {
                for (int z = -scanRadius; z <= scanRadius; z++) {
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

    // --- Helpers ---

    private static boolean isHoldingDetector(LocalPlayer player) {
        return player.getMainHandItem().getItem() instanceof MagicDetectorItem
                || player.getOffhandItem().getItem() instanceof MagicDetectorItem;
    }

    @Nullable
    private static MagicDetectorItem getDetectorItem(LocalPlayer player) {
        if (player.getMainHandItem().getItem() instanceof MagicDetectorItem det) return det;
        if (player.getOffhandItem().getItem() instanceof MagicDetectorItem det) return det;
        return null;
    }
}
