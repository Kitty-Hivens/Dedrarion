package hivens.hdu.client.render;

import hivens.hdu.common.blocks.entity.EftoritForgeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EftoritForgeRenderer implements BlockEntityRenderer<EftoritForgeEntity> {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    // --- ПАРАМЕТРЫ ОРБИТЫ (Оставлены широкими) ---
    private static final float BASE_RADIUS = 0.9f;
    private static final float WAVE_HEIGHT = 0.4f;
    private static final float BASE_ITEM_HEIGHT = 0.3f;
    private static final float VERTICAL_WAVE_SPEED = 1.5f;
    // --- ПАРАМЕТРЫ АНИМАЦИИ ---
    private static final float ITEM_SCALE = 0.5f;
    private static final int MAX_CRAFT_TIME = 100; // 5 секунд

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int light, int overlay) {

        List<ItemStack> items = entity.getItems();
        boolean isCrafting = entity.isCrafting();
        int craftTimer = entity.getCraftTimer();

        long gameTime = entity.getLevel() != null ? entity.getLevel().getGameTime() : 0;
        float baseTime = gameTime + partialTicks;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5); // Центр ковки

        if (isCrafting) {
            float progress = (craftTimer + partialTicks) / MAX_CRAFT_TIME;

            // Фаза 1: Сбор (0% - 80% времени крафта)
            if (progress < 0.8f) {

                // --- НОВОЕ: Отсутствие динамического изменения скорости и радиуса ---
                float currentRotationSpeed = 4.0f; // Постоянная медленная скорость
                float currentRadius = BASE_RADIUS; // Постоянный широкий радиус
                // ------------------------------------------------------------------

                renderIngredients(entity, items, baseTime, currentRotationSpeed, currentRadius, poseStack, bufferSource, light, overlay);

                // Фаза 2: Синтез и Финализация (80% - 100%)
            } else {
                float fusionProgress = (progress - 0.8f) / 0.2f;
                ItemStack resultStack = entity.getResultStack();

                if (!resultStack.isEmpty()) {
                    renderResultItem(entity, resultStack, fusionProgress, baseTime, poseStack, bufferSource, light, overlay);
                }

                if (entity.getLevel() instanceof ClientLevel clientLevel && clientLevel.random.nextFloat() < 0.2f) {
                    clientLevel.addParticle(ParticleTypes.FLASH, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0);
                }
            }
        } else {
            // Фаза 3: Покой - медленное вращение по широкой 3D орбите
            renderIngredients(entity, items, baseTime, 4.0f, BASE_RADIUS, poseStack, bufferSource, light, overlay);
        }

        poseStack.popPose();
    }

    // --- Метод для рендера ингредиентов (Фаза 1 и 3) ---
    private void renderIngredients(EftoritForgeEntity entity, List<ItemStack> items, float baseTime, float rotationSpeed, float radius, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        int activeItemCount = 0;
        for (ItemStack stack : items) if (!stack.isEmpty()) activeItemCount++;
        if (activeItemCount == 0) return;

        int renderedCount = 0;
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                poseStack.pushPose();

                float angle = (360f / activeItemCount) * renderedCount;
                double radians = Math.toRadians(angle + baseTime * (rotationSpeed / 4.0f));

                float xOffset = (float) Math.cos(radians) * radius;
                float zOffset = (float) Math.sin(radians) * radius;

                // 3D-орбита: угол * 3.0f для 3 волн + смещение по времени (оставлено, так как это крутое левитирование)
                float verticalAngle = (float) Math.toRadians(angle * 3.0f + baseTime * VERTICAL_WAVE_SPEED);
                float yOffset = BASE_ITEM_HEIGHT + Mth.sin(verticalAngle) * WAVE_HEIGHT;

                poseStack.translate(xOffset, yOffset, zOffset);
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

                float itemRotation = angle + 90f + baseTime * (rotationSpeed / 2.0f);
                poseStack.mulPose(Axis.YP.rotationDegrees(itemRotation));

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, entity.getLevel(), 0);

                poseStack.popPose();
                renderedCount++;
            }
        }
    }

    // --- Метод для рендера РЕЗУЛЬТИРУЮЩЕГО предмета (Фаза 2 - Синтез) ---
    private void renderResultItem(EftoritForgeEntity entity, ItemStack resultStack, float fusionProgress, float baseTime, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {

        poseStack.pushPose();

        // Используем синусоиду Mth.sin(x*PI) для плавного появления/исчезновения
        float sin_progress = Mth.sin(fusionProgress * Mth.PI);

        // 1. Анимация по вертикали: Плавный подъем до 0.4f и плавный спад
        float y_pos_peak = 0.4f;
        float y_pos = sin_progress * y_pos_peak;

        // 2. Масштаб: Плавная пульсация и коллапс
        float peak_scale = ITEM_SCALE * 1.8f;
        float pulse = Mth.sin(baseTime / 4.0f) * 0.1f;

        // Масштаб: (Пиковый + Пульс) * sin_progress (плавно идет от 0 до Пика и обратно к 0)
        float scale = (peak_scale + pulse) * sin_progress;

        float rotation = baseTime * 10.0f;

        poseStack.translate(0.0f, y_pos, 0.0f);
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        int fullBright = LightTexture.FULL_BRIGHT;

        itemRenderer.renderStatic(
                resultStack,
                ItemDisplayContext.FIXED,
                fullBright,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                entity.getLevel(),
                0
        );

        poseStack.popPose();
    }
}