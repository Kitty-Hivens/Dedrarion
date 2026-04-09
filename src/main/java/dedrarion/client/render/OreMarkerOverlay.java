package dedrarion.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dedrarion.content.item.MagicDetectorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;

/**
 * HUD overlay that renders ore detection markers.
 * <p>
 * When the player is holding a Magic Detector and there are active
 * {@link OreHighlightTarget} entries, this overlay:
 * <ul>
 *   <li>Projects each ore's world position to screen coordinates.</li>
 *   <li>Draws a soft glowing marker (radial gradient circle) at that position.</li>
 *   <li>For off-screen ores, draws a small directional indicator at the screen edge.</li>
 *   <li>Markers fade with distance and pulse gently.</li>
 *   <li>On scan activation, draws a brief expanding ring wave.</li>
 * </ul>
 */
public final class OreMarkerOverlay {

    private OreMarkerOverlay() {}

    /** Maximum render distance for markers (blocks). Beyond this they are hidden. */
    private static final double MAX_RENDER_DIST = 48.0;

    // --- Main render entry ---

    /**
     * Called from the registered GUI overlay. Renders all active ore markers.
     */
    public static void render(GuiGraphics graphics) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean holding = mc.player.getMainHandItem().getItem() instanceof MagicDetectorItem
                || mc.player.getOffhandItem().getItem() instanceof MagicDetectorItem;
        if (!holding) return;

        List<OreHighlightTarget.Entry> entries = OreHighlightTarget.getEntries();

        if (entries.isEmpty()) return;

        Vec3 cam = ProjectionCache.getCameraPos();
        float time = (System.currentTimeMillis() % 10000) / 1000f;

        for (OreHighlightTarget.Entry entry : entries) {
            long entryAge = System.currentTimeMillis() - entry.timestamp();
            Vec3 worldPos = entry.center();
            double dist = worldPos.distanceTo(cam);
            if (dist > MAX_RENDER_DIST) continue;

            // Distance-based alpha fade: full at 0, fading from 60% of max dist
            float distFade = dist > MAX_RENDER_DIST * 0.6
                    ? 1f - (float) ((dist - MAX_RENDER_DIST * 0.6) / (MAX_RENDER_DIST * 0.4))
                    : 1f;

            float alpha = entry.alpha() * distFade;
            if (alpha < 0.02f) continue;

            // Unpack color
            int packed = entry.color();
            float r = ((packed >> 16) & 0xFF) / 255f;
            float g = ((packed >> 8) & 0xFF) / 255f;
            float b = (packed & 0xFF) / 255f;

            // Gentle pulse
            float pulse = 1f + Mth.sin(time * 2.5f + (float) (entry.pos().hashCode() % 100) * 0.1f) * 0.12f;

            // Size scales inversely with distance
            float baseSize = 8f;
            float size = baseSize * pulse * (float) Math.max(0.4, 1.0 - dist / MAX_RENDER_DIST * 0.6);

            Vec2 screen = ProjectionCache.worldToScreen(worldPos);

            if (screen != null && ProjectionCache.isOnScreen(screen)) {
                drawMarker(graphics, screen.x, screen.y, r, g, b, alpha, size, entryAge);
            }
        }
    }

    // --- Draw state management ---

    /**
     * Prepares RenderSystem for color-only drawing (no texture, blending enabled,
     * depth test disabled) and returns a ready BufferBuilder.
     *
     * @param mode vertex format mode (TRIANGLE_FAN, TRIANGLE_STRIP, etc.)
     * @return BufferBuilder in the begun state
     */
    private static BufferBuilder beginColorDraw(VertexFormat.Mode mode) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(mode, DefaultVertexFormat.POSITION_COLOR);
        return buf;
    }

    /**
     * Uploads the buffer and restores RenderSystem state after a color draw call.
     */
    private static void endColorDraw(BufferBuilder buf) {
        BufferUploader.drawWithShader(buf.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    // --- Marker rendering ---

    /**
     * Draws a soft glowing circle marker at the given screen position.
     * Two passes: outer soft glow + inner bright core.
     */
    private static void drawMarker(GuiGraphics graphics, float cx, float cy,
                                   float r, float g, float b, float alpha,
                                   float size, long entryAge) {
        // Existing glow layers
        drawSoftCircle(graphics, cx, cy, size * 1.8f, r, g, b, alpha * 0.15f, 16);
        drawSoftCircle(graphics, cx, cy, size, r, g, b, alpha * 0.4f, 12);
        drawSoftCircle(graphics, cx, cy, size * 0.35f, r, g, b, alpha * 0.85f, 8);

        // Ripple pulse on fresh entries (first 500ms)
        if (entryAge < 1200) {
            float t = entryAge / 1200f;
            float rippleR = size * (1f + t * 5f);
            float rippleA = alpha * 0.35f * (1f - t);
            drawRing(graphics, cx, cy, rippleR, Mth.lerp(t, 2.5f, 0.5f), r, g, b, rippleA);
        }
    }

    /**
     * Draws a radial gradient circle using TRIANGLE_FAN.
     * Center vertex has the given alpha; edge vertices are fully transparent.
     */
    private static void drawSoftCircle(GuiGraphics graphics, float cx, float cy,
                                       float radius, float r, float g, float b,
                                       float alpha, int segments) {
        if (alpha < 0.01f || radius < 0.5f) return;

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buf = beginColorDraw(VertexFormat.Mode.TRIANGLE_FAN);

        // Center vertex — full color
        buf.vertex(matrix, cx, cy, 0f)
                .color(r, g, b, alpha)
                .endVertex();

        // Edge vertices — transparent
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2.0 * Math.PI * i / segments);
            float ex = cx + Mth.cos(angle) * radius;
            float ey = cy + Mth.sin(angle) * radius;
            buf.vertex(matrix, ex, ey, 0f)
                    .color(r, g, b, 0f)
                    .endVertex();
        }

        endColorDraw(buf);
    }

    // --- Scan wave ---

    /**
     * Draws a thin ring (annulus) as a series of quads.
     */
    private static void drawRing(GuiGraphics graphics, float cx, float cy,
                                 float radius, float thickness,
                                 float r, float g, float b, float alpha) {
        if (alpha < 0.01f) return;

        int segments = 48;
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buf = beginColorDraw(VertexFormat.Mode.TRIANGLE_STRIP);

        float innerR = radius - thickness * 0.5f;
        float outerR = radius + thickness * 0.5f;

        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2.0 * Math.PI * i / segments);
            float cos = Mth.cos(angle);
            float sin = Mth.sin(angle);

            buf.vertex(matrix, cx + cos * outerR, cy + sin * outerR, 0f)
                    .color(r, g, b, 0f)
                    .endVertex();
            buf.vertex(matrix, cx + cos * innerR, cy + sin * innerR, 0f)
                    .color(r, g, b, alpha)
                    .endVertex();
        }

        endColorDraw(buf);
    }

}
