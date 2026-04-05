package dedrarion.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dedrarion.Dedrarion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.List;

/**
 * Renders ore glow highlight effect using Minecraft's built-in PostChain.
 * <p>
 * Pipeline:
 * 1. Silhouette pass — draws flat colored cubes into PostChain's "silhouette" target,
 *    with depth test disabled so they show through terrain.
 * 2. PostChain processes the rest: blur H → blur V → additive composite onto main.
 *    All managed by ore_glow.json.
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OreGlowRenderer {

    private static final ResourceLocation POST_CHAIN_ID =
            ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, "shaders/post/ore_glow.json");

    private static ShaderInstance silhouetteShader;
    private static PostChain postChain;
    private static int lastWidth  = -1;
    private static int lastHeight = -1;

    // --- Shader registration (MOD bus) ---

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ShaderRegistration {

        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
            event.registerShader(
                    new ShaderInstance(
                            event.getResourceProvider(),
                            ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, "ore_silhouette"),
                            DefaultVertexFormat.POSITION_COLOR
                    ),
                    shader -> silhouetteShader = shader
            );
        }
    }

    // --- Render hook (FORGE bus) ---

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) return;
        if (silhouetteShader == null) return;

        List<OreHighlightTarget.Entry> entries = OreHighlightTarget.getEntries();
        if (entries.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        int w = mc.getMainRenderTarget().width;
        int h = mc.getMainRenderTarget().height;

        ensurePostChain(mc, w, h);
        if (postChain == null) return;

        try {
            // 1. Get the silhouette target from PostChain and draw into it.
            var silhouetteTarget = postChain.getTempTarget("silhouette");

            silhouetteTarget.setClearColor(0f, 0f, 0f, 0f);
            silhouetteTarget.clear(Minecraft.ON_OSX);
            silhouetteTarget.bindWrite(true);

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(() -> silhouetteShader);

            var cam    = mc.gameRenderer.getMainCamera().getPosition();
            PoseStack pose = event.getPoseStack();
            Tesselator tes = Tesselator.getInstance();

            for (OreHighlightTarget.Entry entry : entries) {
                int packed = entry.color();
                float r = ((packed >> 16) & 0xFF) / 255f;
                float g = ((packed >>  8) & 0xFF) / 255f;
                float b = ( packed        & 0xFF) / 255f;
                float a = entry.alpha();

                pose.pushPose();
                pose.translate(
                        entry.pos().getX() - cam.x,
                        entry.pos().getY() - cam.y,
                        entry.pos().getZ() - cam.z
                );

                // Pre-multiply proj * modelview into vertex positions.
                Matrix4f mvp = new Matrix4f(event.getProjectionMatrix())
                        .mul(pose.last().pose());

                BufferBuilder buf = tes.getBuilder();
                buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                renderColoredCube(buf, mvp, r, g, b, a);
                BufferUploader.drawWithShader(buf.end());

                pose.popPose();
            }

            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();

            // 2. Let PostChain handle blur + composite.
            postChain.process(event.getPartialTick());

        } finally {
            mc.getMainRenderTarget().bindWrite(true);
        }
    }

    // --- Helpers ---

    private static void ensurePostChain(Minecraft mc, int w, int h) {
        if (postChain != null && lastWidth == w && lastHeight == h) return;

        if (postChain != null) {
            postChain.close();
            postChain = null;
        }

        try {
            postChain = new PostChain(
                    mc.getTextureManager(),
                    mc.getResourceManager(),
                    mc.getMainRenderTarget(),
                    POST_CHAIN_ID
            );
            postChain.resize(w, h);
            lastWidth  = w;
            lastHeight = h;
        } catch (IOException e) {
            Dedrarion.LOGGER.error("[OreGlowRenderer] Failed to load post chain: {}", e.getMessage());
            postChain = null;
        }
    }

    private static void renderColoredCube(BufferBuilder buf, Matrix4f mvp,
                                          float r, float g, float b, float a) {
        int ri = (int)(r*255), gi = (int)(g*255), bi = (int)(b*255), ai = (int)(a*255);
        // Bottom
        buf.vertex(mvp, 0,0,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,0,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,0,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,0,1).color(ri,gi,bi,ai).endVertex();
        // Top
        buf.vertex(mvp, 0,1,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,1,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,1,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,1,0).color(ri,gi,bi,ai).endVertex();
        // North
        buf.vertex(mvp, 0,0,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,1,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,1,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,0,0).color(ri,gi,bi,ai).endVertex();
        // South
        buf.vertex(mvp, 0,0,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,0,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,1,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,1,1).color(ri,gi,bi,ai).endVertex();
        // West
        buf.vertex(mvp, 0,0,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,0,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,1,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 0,1,0).color(ri,gi,bi,ai).endVertex();
        // East
        buf.vertex(mvp, 1,0,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,1,0).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,1,1).color(ri,gi,bi,ai).endVertex();
        buf.vertex(mvp, 1,0,1).color(ri,gi,bi,ai).endVertex();
    }
}
