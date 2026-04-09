package dedrarion;

import com.mojang.logging.LogUtils;
import dedrarion.client.ClientSetup;
import dedrarion.compat.ModCompat;
import dedrarion.config.ModConfigRegistration;
import dedrarion.config.ModConfigScreen;
import dedrarion.network.ModNetwork;
import dedrarion.registry.*;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Dedrarion.MOD_ID)
public class Dedrarion {

    public static final String MOD_ID = "dedrarion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Dedrarion(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();

        // --- Config ---
        ModConfigRegistration.register(context);

        // --- Cloth Config GUI (optional dep — no crash if absent) ---
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder");
            context.registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, parent) -> ModConfigScreen.create(parent)
                    )
            );
        } catch (ClassNotFoundException ignored) {
            LOGGER.info("Cloth Config not found — config GUI disabled, edit .toml files manually");
        }

        // --- Registries ---
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModParticles.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("Dedrarion initialization started");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModCompat.initCommon();
        ModNetwork.register();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientSetup.init();
    }
}
