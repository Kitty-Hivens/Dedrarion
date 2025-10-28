package hivens.hdu.common.Item;

import hivens.hdu.common.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicDetectorItem extends Item {

    public MagicDetectorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            Player player = pContext.getPlayer();
            if (player == null) return InteractionResult.FAIL;

            Vec3 eyePos = player.getEyePosition();
            BlockPos playerPos = new BlockPos((int) eyePos.x(), (int) eyePos.y(), (int) eyePos.z());
            int range = 7;

            BlockPos foundPos = null;
            boolean foundBlock = false;

            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos pos = playerPos.offset(x, y, z);
                        BlockState state = pContext.getLevel().getBlockState(pos);

                        if (isValuableBlock(state)) {
                            foundPos = pos;
                            addBlockHighlight(pContext.getLevel(), pos);
                            foundBlock = true;
                            break;
                        }
                    }
                    if (foundBlock) break;
                }
                if (foundBlock) break;
            }

            if (foundBlock) {
                addLineHighlight(pContext.getLevel(), eyePos, foundPos);
                playSoundForPlayer(player);
            }

            if (!foundBlock) {
                System.out.println("No valuable block found");
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void playSoundForPlayer(Player player) {
        if (player != null && player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, player.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.item.hdu.metal_detector"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void addBlockHighlight(Level world, BlockPos pos) {
        if (world instanceof ServerLevel) {
            ((ServerLevel) world).sendParticles(ParticleTypes.GLOW, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    20, 0.5, 0.5, 0.5, 0.1);
        }
    }

    private void addLineHighlight(Level world, Vec3 fromPos, BlockPos toPos) {
        if (world instanceof ServerLevel serverLevel) {
            double xDiff = toPos.getX() - fromPos.x();
            double yDiff = toPos.getY() - fromPos.y();
            double zDiff = toPos.getZ() - fromPos.z();

            double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);

            int particleCount = (int) (distance / 2);
            for (int i = 0; i <= particleCount; i++) {
                double x = fromPos.x() + (xDiff * i / particleCount);
                double y = fromPos.y() + (yDiff * i / particleCount);
                double z = fromPos.z() + (zDiff * i / particleCount);

                serverLevel.sendParticles(ParticleTypes.GLOW, x + 0.5, y - 0.5, z + 0.5, 1, 0, 0, 0, 0);
            }
        }
    }

    private boolean isValuableBlock(BlockState state) {
        return state.is(ModTags.Blocks.METAL_DETECTOR_VALUABLES);
    }
}

