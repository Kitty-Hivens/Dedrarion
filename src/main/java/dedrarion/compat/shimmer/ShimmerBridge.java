package dedrarion.compat.shimmer;

import net.minecraftforge.fml.ModList;

/**
 * Safe bridge for Shimmer integration.
 * <p>
 * All methods check {@link #isLoaded()} before touching any Shimmer classes,
 * so this class can be referenced freely without risking {@link NoClassDefFoundError}.
 */
public final class ShimmerBridge {

    private static final boolean LOADED = ModList.get().isLoaded("shimmer");

    private ShimmerBridge() {}

    public static boolean isLoaded() {
        return LOADED;
    }

    /**
     * Adds a colored bloom light at the ore position.
     * No-op if Shimmer is not installed.
     */
    public static void addOreLight(net.minecraft.core.BlockPos pos, int color) {
        if (LOADED) ShimmerLightCompat.addOreLight(pos, color);
    }

    /**
     * Ticks active lights (fading, expiry). No-op if Shimmer is not installed.
     */
    public static void tick() {
        if (LOADED) ShimmerLightCompat.tick();
    }

    /**
     * Removes all active lights. No-op if Shimmer is not installed.
     */
    public static void clearAll() {
        if (LOADED) ShimmerLightCompat.clearAll();
    }
}
