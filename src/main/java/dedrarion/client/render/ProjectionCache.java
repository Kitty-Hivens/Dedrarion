package dedrarion.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * Captures the projection + model-view matrices during world rendering
 * so that the HUD overlay can project world positions to screen coordinates.
 * <p>
 * Call {@link #capture(RenderLevelStageEvent)} once per frame from
 * {@link RenderLevelStageEvent.Stage#AFTER_WEATHER}.
 */
public final class ProjectionCache {

    private static final Matrix4f clipMatrix = new Matrix4f();
    private static Vec3 cameraPos = Vec3.ZERO;
    private static int screenWidth = 1;
    private static int screenHeight = 1;

    private ProjectionCache() {}

    /**
     * Captures the current frame's projection state.
     * Must be called from the render thread during world rendering.
     */
    public static void capture(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        screenWidth = mc.getWindow().getGuiScaledWidth();
        screenHeight = mc.getWindow().getGuiScaledHeight();

        // clip = projection * modelview
        clipMatrix.set(event.getProjectionMatrix())
                .mul(event.getPoseStack().last().pose());
    }

    /**
     * Projects a world position to GUI-scaled screen coordinates.
     *
     * @param worldPos position in world space
     * @return screen coordinates, or {@code null} if behind camera
     */
    @Nullable
    public static Vec2 worldToScreen(Vec3 worldPos) {
        Vector4f clip = new Vector4f(
                (float) (worldPos.x - cameraPos.x),
                (float) (worldPos.y - cameraPos.y),
                (float) (worldPos.z - cameraPos.z),
                1.0f
        );
        clip.mul(clipMatrix);

        if (clip.w() <= 0.01f) return null; // Behind camera

        float ndcX = clip.x() / clip.w();
        float ndcY = clip.y() / clip.w();

        float sx = (ndcX + 1f) / 2f * screenWidth;
        float sy = (1f - ndcY) / 2f * screenHeight;

        return new Vec2(sx, sy);
    }

    /**
     * Returns {@code true} if the given screen position is within the visible area
     * (with a small margin for partially visible markers).
     */
    public static boolean isOnScreen(Vec2 screen) {
        float margin = -8; // Allow slight off-screen for smooth entry
        return screen.x >= margin && screen.x <= screenWidth - margin
                && screen.y >= margin && screen.y <= screenHeight - margin;
    }

    /**
     * Clamps a screen position to the edges with an inset margin,
     * used for off-screen directional indicators.
     */
    public static Vec2 clampToEdge(Vec2 screen) {
        float margin = 24;
        return new Vec2(
                Mth.clamp(screen.x, margin, screenWidth - margin),
                Mth.clamp(screen.y, margin, screenHeight - margin)
        );
    }

    public static Vec3 getCameraPos() {
        return cameraPos;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
