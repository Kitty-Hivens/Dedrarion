package dedrarion.compat.shimmer;

import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.client.light.LightManager;
import net.minecraft.core.BlockPos;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Optional Shimmer integration for the Magic Detector.
 * <p>
 * When Shimmer is loaded, detected ores get soft colored bloom lights
 * instead of (or in addition to) the HUD overlay markers.
 * <p>
 * All methods are safe to call only when Shimmer is present —
 * the caller ({@link dedrarion.compat.ModCompat}) guards the class loading.
 */
public final class ShimmerLightCompat {

    private ShimmerLightCompat() {}

    /** Active lights with their expiry timestamps. */
    private static final CopyOnWriteArrayList<TrackedLight> activeLights = new CopyOnWriteArrayList<>();

    /** Default light radius in blocks. */
    private static final float DEFAULT_RADIUS = 6.0f;

    /** Duration in ms before a light is removed. Matches OreHighlightTarget. */
    private static final long LIGHT_DURATION_MS = 10_000;

    // --- Public API ---

    /**
     * Adds a colored light at the given ore position.
     * If a light already exists at this position, it is refreshed.
     *
     * @param pos   block position of the detected ore
     * @param color packed ARGB color (alpha ignored by Shimmer)
     */
    public static void addOreLight(BlockPos pos, int color) {
        // Remove existing light at this position to avoid duplicates
        removeAt(pos);

        Vector3f center = new Vector3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f);
        ColorPointLight light = LightManager.INSTANCE.addLight(center, color, DEFAULT_RADIUS);

        if (light != null) {
            activeLights.add(new TrackedLight(pos, light, System.currentTimeMillis()));
        }
    }

    /**
     * Ticks all active lights — fades and removes expired ones.
     * Call this from a client tick handler.
     */
    public static void tick() {
        if (activeLights.isEmpty()) return;

        long now = System.currentTimeMillis();

        for (TrackedLight tracked : activeLights) {
            long elapsed = now - tracked.createdAt;

            if (elapsed >= LIGHT_DURATION_MS) {
                // Expired — remove
                tracked.light.remove();
                activeLights.remove(tracked);
                continue;
            }

            // Fade radius in the last 30% of lifetime
            float life = elapsed / (float) LIGHT_DURATION_MS;
            if (life > 0.7f) {
                float fade = 1f - (life - 0.7f) / 0.3f;
                tracked.light.radius = DEFAULT_RADIUS * fade;
                tracked.light.update();
            }
        }
    }

    /**
     * Removes all active lights immediately. Call on world unload or dimension change.
     */
    public static void clearAll() {
        for (TrackedLight tracked : activeLights) {
            tracked.light.remove();
        }
        activeLights.clear();
    }

    // --- Internals ---

    private static void removeAt(BlockPos pos) {
        activeLights.removeIf(t -> {
            if (t.pos.equals(pos)) {
                t.light.remove();
                return true;
            }
            return false;
        });
    }

    /**
     * Tracks a Shimmer light with its creation time and block position.
     */
    private record TrackedLight(BlockPos pos, ColorPointLight light, long createdAt) {}
}
