package Hivens.hdu.Client.render;

import Hivens.hdu.Common.Custom.Block.Entity.EftoritForgeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EftoritForgeRenderer implements BlockEntityRenderer<EftoritForgeEntity> {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    private static final float RADIUS = 0.3f; // Радиус расположения предметов вокруг центра
    private static final float ITEM_SCALE = 0.5f; // Уменьшение размера предметов
    private static final int MAX_ITEMS = 8; // Ограничение количества предметов в рендере

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (entity.getItems().isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5);

        float time = (System.currentTimeMillis() / 20) % 360; // Вращение каждый тик

        int itemCount = Math.min(entity.getItems().size(), MAX_ITEMS);
        for (int i = 0; i < itemCount; i++) {
            ItemStack stack = entity.getItems().get(i);
            if (!stack.isEmpty()) {
                poseStack.pushPose();

                // Распределение предметов по кругу
                float angle = (360f / itemCount) * i;
                double radians = Math.toRadians(angle + time); // Добавляем анимацию вращения

                float xOffset = (float) Math.cos(radians) * RADIUS;
                float zOffset = (float) Math.sin(radians) * RADIUS;
                poseStack.translate(xOffset, 0.1, zOffset);

                // Уменьшение размера предметов
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

                // Вращение предметов по Y
                poseStack.mulPose(Axis.YP.rotationDegrees(time * 2)); // Плавное вращение предметов

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, entity.getLevel(), 0);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }
}
