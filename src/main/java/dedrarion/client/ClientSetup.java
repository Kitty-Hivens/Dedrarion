package dedrarion.client;

import dedrarion.client.render.OreGlowRenderer;
import dedrarion.client.render.block.EftoritForgeRenderer;
import dedrarion.client.render.block.PedestalBlockEntityRenderer;
import dedrarion.registry.ModBlockEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.PEDESTAL_ENTITY.get(),
                ctx -> new PedestalBlockEntityRenderer()
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.EFTORIT_FORGE_ENTITY.get(),
                ctx -> new EftoritForgeRenderer()
        );
    }

    /**
     * Registers the ore glow post-process renderer on the FORGE event bus.
     * Called from {@link dedrarion.Dedrarion} constructor on the client dist.
     */
    public static void init() {
        MinecraftForge.EVENT_BUS.register(OreGlowRenderer.class);
    }
}
