package Hivens.hdu.Client.render;

import Hivens.hdu.Common.Custom.Block.Entity.EftoritForgeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;

public class EftoritForgeEntityRenderer implements BlockEntityRenderer<EftoritForgeEntity> {
    public EftoritForgeEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(EftoritForgeEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        if (entity.isCrafting()) {
            // Визуальные эффекты
            assert Minecraft.getInstance().level != null;
            Minecraft.getInstance().level.addParticle(
                    ParticleTypes.FLAME,
                    entity.getBlockPos().getX() + 0.5,
                    entity.getBlockPos().getY() + 1.0,
                    entity.getBlockPos().getZ() + 0.5,
                    0, 0, 0
            );

        }
    }
}
