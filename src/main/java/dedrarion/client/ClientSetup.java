package dedrarion.client;

import dedrarion.Dedrarion;
import dedrarion.client.particle.SoftGlowParticle;
import dedrarion.client.render.OreMarkerOverlay;
import dedrarion.client.render.ProjectionCache;
import dedrarion.client.render.block.EftoritForgeRenderer;
import dedrarion.client.render.block.PedestalBlockEntityRenderer;
import dedrarion.compat.shimmer.ShimmerBridge;
import dedrarion.registry.ModBlockEntities;
import dedrarion.registry.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
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

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SOFT_GLOW.get(), SoftGlowParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("ore_markers",
                (gui, graphics, partialTick, width, height) ->
                        OreMarkerOverlay.render(graphics)
        );
    }

    /**
     * Registers the ore glow post-process renderer on the FORGE event bus.
     * Called from {@link Dedrarion} constructor on the client dist.
     */
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener((RenderLevelStageEvent event) -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
                ProjectionCache.capture(event);
            }
        });

        // Shimmer light fade/expiry
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase == TickEvent.Phase.END) ShimmerBridge.tick();
        });

        // Clean up on world unload
        MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.event.level.LevelEvent.Unload event) -> {
            if (event.getLevel().isClientSide()) ShimmerBridge.clearAll();
        });
    }
}
