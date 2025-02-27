package Hivens.hdu.Client;

import Hivens.hdu.Client.render.PedestalBlockEntityRenderer;
import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        // Register render for block entity
        event.enqueueWork(() -> BlockEntityRenderers.register(
                BlockEntitiesRegistry.PEDESTAL_ENTITY.get(),
                PedestalBlockEntityRenderer::new
        ));
    }
}
