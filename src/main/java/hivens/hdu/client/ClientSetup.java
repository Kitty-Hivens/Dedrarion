package hivens.hdu.client;

import hivens.hdu.client.render.EftoritForgeRenderer;
import hivens.hdu.client.render.PedestalBlockEntityRenderer;
import hivens.hdu.common.registry.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.PEDESTAL_ENTITY.get(),
                context1 -> new PedestalBlockEntityRenderer()
        );
        System.out.println("Registering PedestalBlockEntityRenderer");
        event.registerBlockEntityRenderer(
                ModBlockEntities.EFTORIT_FORGE_ENTITY.get(),
                context -> new EftoritForgeRenderer()
        );

        System.out.println("Registering EftoritForgeRenderer");
    }
}
