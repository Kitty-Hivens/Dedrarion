package hivens.hdu.client.render;

import hivens.hdu.common.blocks.Entity.EftoritForgeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
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
    public void render(EftoritForgeEntity entity, float partialTicks, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int light, int overlay) {

        List<ItemStack> items = entity.getItems();

        if (items.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5);

        long timeMillis = System.currentTimeMillis();
        float time = (timeMillis % 36000) / 100.0f;

        int effectiveItemCount = 0;
        for (ItemStack stack : items) if (!stack.isEmpty()) effectiveItemCount++;
        if (effectiveItemCount == 0) {
            poseStack.popPose();
            return;
        }

        int renderedItems = 0;
        for (ItemStack stack : items) {
            if (!stack.isEmpty() && renderedItems < MAX_ITEMS) {
                poseStack.pushPose();

                float angle = (360f / effectiveItemCount) * renderedItems;
                double radians = Math.toRadians(angle + time);

                float xOffset = (float) Math.cos(radians) * RADIUS;
                float zOffset = (float) Math.sin(radians) * RADIUS;
                float yOffset = 0.1f + (renderedItems % 2) * 0.05f;

                poseStack.translate(xOffset, yOffset, zOffset);
                poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

                float itemRotation = angle + 90f + time * 2f;
                poseStack.mulPose(Axis.YP.rotationDegrees(itemRotation));

                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, poseStack, bufferSource, entity.getLevel(), 0);

                poseStack.popPose();
                renderedItems++;
            }
        }

        poseStack.popPose();
    }



}
