package hivens.hdu;

import hivens.hdu.common.compat.ModCompat;
import hivens.hdu.common.registry.*;
import hivens.hdu.common.loot.ModLootModifiers;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(HDU.MOD_ID)
public class HDU {
    public static final String MOD_ID = "hdu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HDU(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("HDU Mod Initialization Started");
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModCompat.initCommon(event);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HDU Common Setup Complete");
    }
}
