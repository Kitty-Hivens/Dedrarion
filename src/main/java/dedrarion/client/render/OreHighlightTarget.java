package dedrarion.client.render;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Client-side shared state for ore highlight rendering.
 * <p>
 * Updated by {@link dedrarion.content.item.MagicDetectorItem} when the player
 * activates detection, consumed by {@link OreGlowRenderer} every frame.
 */
public final class OreHighlightTarget {

    /** Maximum time (in ms) a highlight stays active before fading out. */
    public static final long HIGHLIGHT_DURATION_MS = 10_000;

    private static final List<Entry> ENTRIES = new CopyOnWriteArrayList<>();

    private OreHighlightTarget() {}

    /**
     * Registers a block position to be highlighted with the given color.
     * Replaces any existing entry at that position.
     *
     * @param pos   block to highlight
     * @param color ARGB packed color (use {@link #color(float, float, float)})
     */
    public static void add(BlockPos pos, int color) {
        ENTRIES.removeIf(e -> e.pos.equals(pos));
        ENTRIES.add(new Entry(pos, color, System.currentTimeMillis()));
    }

    /** Clears all active highlights. */
    public static void clear() {
        ENTRIES.clear();
    }

    /** Returns an unmodifiable view of current highlight entries. */
    public static List<Entry> getEntries() {
        long now = System.currentTimeMillis();
        ENTRIES.removeIf(e -> now - e.timestamp > HIGHLIGHT_DURATION_MS);
        return Collections.unmodifiableList(ENTRIES);
    }

    /**
     * Packs an RGB float triplet into an ARGB int.
     * Alpha is always 1.0 (fully opaque silhouette).
     */
    public static int color(float r, float g, float b) {
        int ri = (int) (r * 255) & 0xFF;
        int gi = (int) (g * 255) & 0xFF;
        int bi = (int) (b * 255) & 0xFF;
        return (0xFF << 24) | (ri << 16) | (gi << 8) | bi;
    }

    // --- Ore color presets ---

    public static final int COLOR_RUBY     = color(1.0f, 0.13f, 0.13f);
    public static final int COLOR_EFTORIT  = color(0.27f, 0.27f, 1.0f);
    public static final int COLOR_ETHEREUM = color(1.0f, 1.0f, 0.87f);

    // --- Entry ---

    public record Entry(BlockPos pos, int color, long timestamp) {

        /** Returns the alpha factor [0,1] based on remaining highlight time. */
        public float alpha() {
            long elapsed = System.currentTimeMillis() - timestamp;
            float t = elapsed / (float) HIGHLIGHT_DURATION_MS;
            // Fade out in the last 20 % of the duration.
            return t > 0.8f ? 1f - (t - 0.8f) / 0.2f : 1f;
        }

        public Vec3 center() {
            return Vec3.atCenterOf(pos);
        }
    }
}
