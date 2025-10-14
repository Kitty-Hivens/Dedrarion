package hivens.hdu.client.render;

import hivens.hdu.common.blocks.entity.PedestalBlockEntity;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
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

import java.util.Objects;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PedestalBlockEntityRenderer() {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemStack itemStack = pBlockEntity.getItem();


        if (itemStack.isEmpty()) return; // Если предмет пустой — выходим

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.75f, 0.5f);
        pPoseStack.scale(0.35f, 0.35f, 0.35f);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(270));

        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                getLightLevel(Objects.requireNonNull(pBlockEntity.getLevel()), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1
        );

        pPoseStack.popPose();
    }



    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);

        return LightTexture.pack(bLight, sLight);
    }
}
