package Hivens.hdu.Client;

import Hivens.hdu.Client.render.EftoritForgeRenderer;
import Hivens.hdu.Client.render.PedestalBlockEntityRenderer;
import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    /*
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition();
    }

     */

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                BlockEntitiesRegistry.PEDESTAL_ENTITY.get(),
                PedestalBlockEntityRenderer::new
        );
        System.out.println("Registering PedestalBlockEntityRenderer");

        event.registerBlockEntityRenderer(
                BlockEntitiesRegistry.EFTORIT_FORGE_ENTITY.get(),
                EftoritForgeRenderer::new
        );
        System.out.println("Registering EftoritForgeEntityRenderer");
    }
}
