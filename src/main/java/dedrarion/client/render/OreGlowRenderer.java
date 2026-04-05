package dedrarion.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dedrarion.Dedrarion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
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
    private static String lastError = "none";
    private static boolean didRenderOnce = false;
    private static long lastFailedAtTick = -200L;

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

        if (!didRenderOnce) {
            Dedrarion.LOGGER.info("[OreGlowRenderer] First render attempt: postChain={}, shader={}, entries={}",
                    postChain != null, silhouetteShader != null, entries.size());
            didRenderOnce = true;
        }

        Minecraft mc = Minecraft.getInstance();
        int w = mc.getMainRenderTarget().width;
        int h = mc.getMainRenderTarget().height;

        ensurePostChain(mc, w, h);
        if (postChain == null) return;

        try {
            // 1. Рисуем силуэты в target "silhouette"
            var silhouetteTarget = postChain.getTempTarget("silhouette");

            silhouetteTarget.setClearColor(0f, 0f, 0f, 0f);
            silhouetteTarget.clear(Minecraft.ON_OSX);
            silhouetteTarget.bindWrite(true);

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(() -> silhouetteShader);

            var cam        = mc.gameRenderer.getMainCamera().getPosition();
            PoseStack pose = event.getPoseStack();
            Tesselator tes = Tesselator.getInstance();

            for (OreHighlightTarget.Entry entry : entries) {
                double distSq = entry.pos().distToCenterSqr(cam.x, cam.y, cam.z);
                if (distSq < 1.5) continue;

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

            // 2. Blur через PostChain (только blur passes, composite убран из json)
            postChain.process(event.getPartialTick());

            // 3. Вручную additively blit blur_v → main RT без очистки сцены
            var blurV = postChain.getTempTarget("blur_v");
            mc.getMainRenderTarget().bindWrite(false);

            var mvStack = RenderSystem.getModelViewStack();
            mvStack.pushPose();
            mvStack.last().pose().identity();
            RenderSystem.applyModelViewMatrix();

            Matrix4f ortho = new Matrix4f();
            RenderSystem.setProjectionMatrix(ortho, VertexSorting.ORTHOGRAPHIC_Z);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE
            );
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.colorMask(true, true, true, false);

            RenderSystem.setShaderTexture(0, blurV.getColorTextureId());
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            BufferBuilder bb = tes.getBuilder();
            bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bb.vertex(-1, -1, 0).uv(0, 0).endVertex();
            bb.vertex( 1, -1, 0).uv(1, 0).endVertex();
            bb.vertex( 1,  1, 0).uv(1, 1).endVertex();
            bb.vertex(-1,  1, 0).uv(0, 1).endVertex();
            BufferUploader.drawWithShader(bb.end());

            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();

            mvStack.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setProjectionMatrix(
                    event.getProjectionMatrix(),
                    VertexSorting.DISTANCE_TO_ORIGIN
            );

        } finally {
            mc.getMainRenderTarget().bindWrite(true);
        }
    }

    // --- Helpers ---

    private static void ensurePostChain(Minecraft mc, int w, int h) {
        if (postChain != null && lastWidth == w && lastHeight == h) return;

        long now = mc.level != null ? mc.level.getGameTime() : 0;
        if (postChain == null && now - lastFailedAtTick < 200) return;

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
            lastError = e.getMessage();
            Dedrarion.LOGGER.error("[OreGlowRenderer] Failed to load post chain: {}", e.getMessage());
            postChain = null;
            lastFailedAtTick = now;
        }
    }

    private static void renderColoredCube(BufferBuilder buf, Matrix4f mvp,
                                          float r, float g, float b, float a) {
        int ri = (int)(r*255), gi = (int)(g*255), bi = (int)(b*255), ai = (int)(a*255);
        // Bottom
        vtx(buf,mvp, 0,0,0, ri,gi,bi,ai); vtx(buf,mvp, 1,0,0, ri,gi,bi,ai);
        vtx(buf,mvp, 1,0,1, ri,gi,bi,ai); vtx(buf,mvp, 0,0,1, ri,gi,bi,ai);
        // Top
        vtx(buf,mvp, 0,1,0, ri,gi,bi,ai); vtx(buf,mvp, 0,1,1, ri,gi,bi,ai);
        vtx(buf,mvp, 1,1,1, ri,gi,bi,ai); vtx(buf,mvp, 1,1,0, ri,gi,bi,ai);
        // North
        vtx(buf,mvp, 0,0,0, ri,gi,bi,ai); vtx(buf,mvp, 0,1,0, ri,gi,bi,ai);
        vtx(buf,mvp, 1,1,0, ri,gi,bi,ai); vtx(buf,mvp, 1,0,0, ri,gi,bi,ai);
        // South
        vtx(buf,mvp, 0,0,1, ri,gi,bi,ai); vtx(buf,mvp, 1,0,1, ri,gi,bi,ai);
        vtx(buf,mvp, 1,1,1, ri,gi,bi,ai); vtx(buf,mvp, 0,1,1, ri,gi,bi,ai);
        // West
        vtx(buf,mvp, 0,0,0, ri,gi,bi,ai); vtx(buf,mvp, 0,0,1, ri,gi,bi,ai);
        vtx(buf,mvp, 0,1,1, ri,gi,bi,ai); vtx(buf,mvp, 0,1,0, ri,gi,bi,ai);
        // East
        vtx(buf,mvp, 1,0,0, ri,gi,bi,ai); vtx(buf,mvp, 1,1,0, ri,gi,bi,ai);
        vtx(buf,mvp, 1,1,1, ri,gi,bi,ai); vtx(buf,mvp, 1,0,1, ri,gi,bi,ai);
    }

    private static void vtx(BufferBuilder buf, Matrix4f m,
                            float x, float y, float z, int r, int g, int b, int a) {
        float cx = m.m00()*x + m.m10()*y + m.m20()*z + m.m30();
        float cy = m.m01()*x + m.m11()*y + m.m21()*z + m.m31();
        float cz = m.m02()*x + m.m12()*y + m.m22()*z + m.m32();
        float cw = m.m03()*x + m.m13()*y + m.m23()*z + m.m33();
        if (cw < 0.05f) {
            // За near plane — ставим вырожденную вершину чтобы quad схлопнулся
            buf.vertex(0, 0, 0).color(r, g, b, 0).endVertex();
            return;
        }
        buf.vertex(cx/cw, cy/cw, cz/cw).color(r, g, b, a).endVertex();
    }

    public static String debugStatus() {
        return String.format("postChain=%s shader=%s lastError=[%s] renderedOnce=%s",
                postChain != null ? "OK" : "NULL",
                silhouetteShader != null ? "OK" : "NULL",
                lastError,
                didRenderOnce
        );
    }
}
