package Hivens.hdu.Client.render;

import Hivens.hdu.Common.Custom.Block.Entity.EftoritForgeEntity;
import Hivens.hdu.Config;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class EftoritForgeRenderer implements BlockEntityRenderer<EftoritForgeEntity> {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ItemRenderer itemRenderer;

    public EftoritForgeRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        System.out.println("Рендер: инвентарь на клиенте: " + entity.getInventory());
        List<ItemStack> items = entity.getVisibleInventory();
        System.out.println("Отрисовка: " + items.size() + " предметов");

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            System.out.println("Рендер предмета: " + stack);
            renderItem(stack, entity, poseStack, buffer);
        }
    }


    private void renderItem(ItemStack itemStack, EftoritForgeEntity entity, PoseStack poseStack,
                            MultiBufferSource buffer) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.0f, 0.5f);
        poseStack.scale(0.35f, 0.35f, 0.35f);
        poseStack.mulPose(Axis.XP.rotationDegrees(270));

        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getLevel(), 1
        );

        poseStack.popPose();
        LOGGER.debug("[EftoritForgeRenderer] Успешно отрендерен предмет {}");
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
