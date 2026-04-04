package dedrarion.client.render.block;

import dedrarion.content.block.entity.EftoritForgeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Renders items orbiting above the {@link EftoritForgeEntity}.
 *
 * <h3>Animation phases</h3>
 * <ol>
 *   <li><b>Idle</b> — slow wide orbit, gentle vertical wave.</li>
 *   <li><b>Gathering</b> (0–70 % of craft time) — orbit gradually accelerates
 *       and tightens as crafting progresses.</li>
 *   <li><b>Converging</b> (70–90 %) — items spiral inward to the center,
 *       speed peaks, radius collapses to zero.</li>
 *   <li><b>Synthesis</b> (90–100 %) — result item pulses into view with a
 *       sine-eased scale, spins fast, and fades out at the end.</li>
 * </ol>
 */
public class EftoritForgeRenderer implements BlockEntityRenderer<EftoritForgeEntity> {

    // --- Orbit parameters ---
    private static final float BASE_RADIUS        = 0.9f;
    private static final float BASE_HEIGHT        = 0.3f;
    private static final float WAVE_HEIGHT        = 0.4f;
    private static final float WAVE_SPEED         = 1.5f;

    // --- Idle orbit speed (radians per tick equivalent) ---
    private static final float IDLE_SPEED         = 4.0f;

    // --- Craft phase thresholds (fraction of total craft time) ---
    private static final float PHASE_CONVERGE     = 0.70f;
    private static final float PHASE_SYNTHESIS    = 0.90f;

    // --- Item rendering ---
    private static final float ITEM_SCALE         = 0.5f;
    private static final float RESULT_PEAK_SCALE  = 0.9f;

    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, @NotNull PoseStack pose,
                       @NotNull MultiBufferSource buffer, int light, int overlay) {

        List<ItemStack> items    = entity.getItems();
        boolean isCrafting       = entity.isCrafting();
        int craftTimer           = entity.getCraftTimer();
        long gameTime            = entity.getLevel() != null ? entity.getLevel().getGameTime() : 0;
        float time               = gameTime + partialTicks;

        pose.pushPose();
        pose.translate(0.5, 1.0, 0.5);

        if (!isCrafting) {
            renderIdleOrbit(entity, items, time, pose, buffer, light, overlay);
        } else {
            // Normalized craft progress [0, 1].
            float progress = craftTimer / (float) entity.getCraftTime();

            if (progress < PHASE_CONVERGE) {
                renderGathering(entity, items, time, progress, pose, buffer, light, overlay);
            } else if (progress < PHASE_SYNTHESIS) {
                renderConverging(entity, items, time, progress, pose, buffer, light, overlay);
            } else {
                float synthProgress = (progress - PHASE_SYNTHESIS) / (1f - PHASE_SYNTHESIS);
                renderSynthesis(entity, entity.getResultStack(), synthProgress, time, pose, buffer);
            }
        }

        pose.popPose();
    }

    // -------------------------------------------------------------------------
    // Phase renderers
    // -------------------------------------------------------------------------

    /**
     * Idle phase — slow orbit, no crafting in progress.
     */
    private void renderIdleOrbit(EftoritForgeEntity entity, List<ItemStack> items, float time,
                                 PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        renderOrbit(entity, items, time, IDLE_SPEED, BASE_RADIUS, pose, buffer, light, overlay);
    }

    /**
     * Gathering phase — orbit gradually accelerates and tightens.
     * Progress goes from 0 to {@link #PHASE_CONVERGE}.
     */
    private void renderGathering(EftoritForgeEntity entity, List<ItemStack> items, float time,
                                 float progress, PoseStack pose, MultiBufferSource buffer,
                                 int light, int overlay) {
        // Normalize progress within this phase [0, 1].
        float t = progress / PHASE_CONVERGE;

        float speed  = Mth.lerp(t, IDLE_SPEED, IDLE_SPEED * 4f);
        float radius = Mth.lerp(t, BASE_RADIUS, BASE_RADIUS * 0.6f);

        renderOrbit(entity, items, time, speed, radius, pose, buffer, light, overlay);
    }

    /**
     * Converging phase — items spiral into the center and disappear.
     * Progress goes from {@link #PHASE_CONVERGE} to {@link #PHASE_SYNTHESIS}.
     */
    private void renderConverging(EftoritForgeEntity entity, List<ItemStack> items, float time,
                                  float progress, PoseStack pose, MultiBufferSource buffer,
                                  int light, int overlay) {
        float t = (progress - PHASE_CONVERGE) / (PHASE_SYNTHESIS - PHASE_CONVERGE);

        // Radius collapses to zero; speed stays at max.
        float speed  = IDLE_SPEED * 4f;
        float radius = Mth.lerp(t, BASE_RADIUS * 0.6f, 0f);

        // Items shrink as they converge.
        float scale = Mth.lerp(t, ITEM_SCALE, 0f);

        renderOrbit(entity, items, time, speed, radius, scale, pose, buffer, light, overlay);
    }

    /**
     * Synthesis phase — result item pulses into view, spins, then fades out.
     *
     * @param synthProgress normalized progress within this phase [0, 1]
     */
    private void renderSynthesis(EftoritForgeEntity entity, ItemStack result,
                                 float synthProgress, float time,
                                 PoseStack pose, MultiBufferSource buffer) {
        if (result.isEmpty()) return;

        // sin(x*PI) gives smooth 0→peak→0 envelope over the full phase.
        float envelope = Mth.sin(synthProgress * Mth.PI);
        float pulse    = Mth.sin(time / 3f) * 0.05f;
        float scale    = (RESULT_PEAK_SCALE + pulse) * envelope;
        float yPos     = 0.35f * envelope;

        pose.pushPose();
        pose.translate(0f, yPos, 0f);
        pose.scale(scale, scale, scale);
        pose.mulPose(Axis.YP.rotationDegrees(time * 12f));
        pose.mulPose(Axis.XP.rotationDegrees(90f));

        itemRenderer.renderStatic(
                result,
                ItemDisplayContext.FIXED,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                pose, buffer, entity.getLevel(), 0
        );

        pose.popPose();
    }

    // -------------------------------------------------------------------------
    // Orbit helpers
    // -------------------------------------------------------------------------

    /** Renders the orbit using the default {@link #ITEM_SCALE}. */
    private void renderOrbit(EftoritForgeEntity entity, List<ItemStack> items, float time,
                             float speed, float radius,
                             PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        renderOrbit(entity, items, time, speed, radius, ITEM_SCALE, pose, buffer, light, overlay);
    }

    /**
     * Core orbit renderer — distributes items evenly around a circle and applies
     * a vertical sine wave for a 3-D floating effect.
     *
     * @param speed  angular speed in degrees per tick equivalent
     * @param radius orbit radius in blocks
     * @param scale  item render scale
     */
    private void renderOrbit(EftoritForgeEntity entity, List<ItemStack> items, float time,
                             float speed, float radius, float scale,
                             PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        int count = (int) items.stream().filter(s -> !s.isEmpty()).count();
        if (count == 0) return;

        int rendered = 0;
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;

            float angleDeg = (360f / count) * rendered;
            double rad     = Math.toRadians(angleDeg + time * (speed / 4f));

            float x = (float) Math.cos(rad) * radius;
            float z = (float) Math.sin(rad) * radius;

            // Vertical wave: each item is offset in phase so they don't all bob in sync.
            float wavePhase = (float) Math.toRadians(angleDeg * 3f + time * WAVE_SPEED);
            float y = BASE_HEIGHT + Mth.sin(wavePhase) * WAVE_HEIGHT;

            pose.pushPose();
            pose.translate(x, y, z);
            pose.scale(scale, scale, scale);
            pose.mulPose(Axis.YP.rotationDegrees(angleDeg + 90f + time * (speed / 2f)));

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    light, overlay,
                    pose, buffer, entity.getLevel(), 0
            );

            pose.popPose();
            rendered++;
        }
    }
}
