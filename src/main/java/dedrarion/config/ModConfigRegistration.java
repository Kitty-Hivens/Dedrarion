package dedrarion.config;

import dedrarion.Dedrarion;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Registers config specs with Forge. Call once from the mod constructor.
 */
public final class ModConfigRegistration {

    private ModConfigRegistration() {}

    public static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, ModConfigValues.COMMON_SPEC, Dedrarion.MOD_ID + "-common.toml");
        context.registerConfig(ModConfig.Type.CLIENT, ModConfigValues.CLIENT_SPEC, Dedrarion.MOD_ID + "-client.toml");
    }
}
