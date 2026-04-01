package dedrarion;

import com.mojang.logging.LogUtils;
import dedrarion.common.compat.ModCompat;
import dedrarion.common.entity.NullGuardianEntity;
import dedrarion.common.loot.ModLootModifiers;
import dedrarion.api.registry.ModEffects;
import dedrarion.common.registry.*;
import dedrarion.common.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(dedrarion.Dedrarion.MOD_ID)
public class Dedrarion {
    public static final String MOD_ID = "hdu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Dedrarion(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::entityAttributeEvent);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("HDU Mod Initialization Started");
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModCompat.initCommon();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HDU Common Setup Complete");
    }

    private void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.NULL_GUARDIAN.get(), NullGuardianEntity.createAttributes().build());
    }
}
