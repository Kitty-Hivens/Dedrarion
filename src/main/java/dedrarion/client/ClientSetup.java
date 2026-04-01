package dedrarion.client;

import dedrarion.client.render.block.EftoritForgeRenderer;
import dedrarion.client.render.block.PedestalBlockEntityRenderer;
import dedrarion.client.render.entity.NullGuardianRenderer;
import dedrarion.registry.ModBlockEntities;
import dedrarion.registry.ModEntityTypes;
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

        event.registerBlockEntityRenderer(
                ModBlockEntities.EFTORIT_FORGE_ENTITY.get(),
                context -> new EftoritForgeRenderer()
        );

        event.registerEntityRenderer(
                ModEntityTypes.NULL_GUARDIAN.get(),
                NullGuardianRenderer::new
        );
    }
}
