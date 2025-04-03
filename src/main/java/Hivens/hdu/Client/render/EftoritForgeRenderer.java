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
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EftoritForgeRenderer implements BlockEntityRenderer<EftoritForgeEntity> {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    private static final float RADIUS = 0.4f; // Increased radius for better item spacing
    private static final float ITEM_SCALE = 0.5f; // Scale of rendered items
    private static final int MAX_ITEMS = 16; // Increased max items to show

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        List<ItemStack> items = entity.getItems();
        if (items.isEmpty()) return;

        poseStack.pushPose();

        // Center of the block
        poseStack.translate(0.5, 1.0, 0.5);

        // Calculate animation time (smooth rotation)
        float time = (System.currentTimeMillis() / 20) % 360;

        // Filter out empty stacks and limit to MAX_ITEMS
        int effectiveItemCount = 0;
        for (ItemStack stack : items) {
            if (!stack.isEmpty() && effectiveItemCount < MAX_ITEMS) {
                effectiveItemCount++;
            }
        }

        if (effectiveItemCount == 0) {
            poseStack.popPose();
            return;
        }

        // Render non-empty items
        int renderedItems = 0;
        for (ItemStack stack : items) {
            if (!stack.isEmpty() && renderedItems < MAX_ITEMS) {
                poseStack.pushPose();

                // Calculate angle for this item in radians
                float angle = (360f / effectiveItemCount) * renderedItems;
                double radians = Math.toRadians(angle + time);

                // Position the item in a circle with vertical offset based on index
                float xOffset = (float) Math.cos(radians) * RADIUS;
                float zOffset = (float) Math.sin(radians) * RADIUS;
                float yOffset = 0.1f + (renderedItems % 2) * 0.05f; // Alternating heights for better visibility

                poseStack.translate(xOffset, yOffset, zOffset);
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

                // Rotate item to face outward
                float itemRotation = angle + 90f + time * 2;
                poseStack.mulPose(Axis.YP.rotationDegrees(itemRotation));

                // Render the item
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, entity.getLevel(), 0);

                poseStack.popPose();
                renderedItems++;
            }
        }

        poseStack.popPose();
    }
}
