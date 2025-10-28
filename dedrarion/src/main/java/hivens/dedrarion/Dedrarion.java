package hivens.dedrarion;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.awt.*;

@Mod(Dedrarion.MOD_ID)
public class Dedrarion {
    public static final String MOD_ID = "dedrarion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Dedrarion(FMLJavaModLoadingContext context) {
        var modEventBus = context.getModEventBus();

        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("HDU Mod Initialization Started");
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HDU Common Setup Complete");
    }

}
