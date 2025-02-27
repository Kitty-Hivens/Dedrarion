package Hivens.hdu.Client.render;

import Hivens.hdu.Common.Custom.Block.Entity.PedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(PedestalBlockEntity pedestalBlockEntity,
                       float partialTick,
                       @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource buffer,
                       int light,
                       int overlay
    ) {
        ItemStack storedItem = pedestalBlockEntity.getItem();

        if (!storedItem.isEmpty()) {
            // Создаем смещение для отображения предмета над пьедесталом
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5); // Рендерим чуть выше блока

            // Получаем модель и рендерим предмет
            LivingEntity playerEntity = Minecraft.getInstance().player; // Игрок как LivingEntity
            BakedModel model = itemRenderer.getModel(storedItem, Minecraft.getInstance().level, playerEntity, 0); // Передаем игрока вместо уровня
            itemRenderer.render(
                    storedItem,
                    ItemDisplayContext.GROUND,
                    false, poseStack, buffer, light, overlay, model
            );

            poseStack.popPose();
        }
    }
}
