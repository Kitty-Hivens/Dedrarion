package Hivens.hdu.Client.render;

import Hivens.hdu.Common.Custom.Block.Entity.EftoritForgeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EftoritForgeRenderer implements BlockEntityRenderer<EftoritForgeEntity> {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (entity.getItems().isEmpty()) return;

        poseStack.pushPose();

        // Перемещаем к центру блока
        BlockPos pos = entity.getBlockPos();
        poseStack.translate(0.5, 1.0, 0.5);

        // Поворачиваем предметы для красоты
        poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));

        // Рендер предметов
        for (int i = 0; i < entity.getItems().size(); i++) {
            ItemStack stack = entity.getItems().get(i);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0, 0.1 * i, 0); // Смещаем вверх

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, entity.getLevel(), 0);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }
}
