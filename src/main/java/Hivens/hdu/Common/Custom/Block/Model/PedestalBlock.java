package Hivens.hdu.Common.Custom.Block.Model;

import Hivens.hdu.Common.Custom.Block.Entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PedestalBlock extends BaseEntityBlock {

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PedestalBlockEntity pedestal) {
                ItemStack heldItem = player.getItemInHand(hand);

                if (pedestal.hasItem()) {
                    player.addItem(pedestal.removeItem());
                } else if (!heldItem.isEmpty()) {
                    pedestal.setItem(heldItem.copyWithCount(1));
                    heldItem.shrink(1);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PedestalBlockEntity pedestal) {
                ItemStack itemStack = pedestal.removeItem();
                if (!itemStack.isEmpty()) {
                    // Дропаем содержимое
                    popResource(world, pos, itemStack);
                }
            }
        }
        // Теперь вызываем super.onRemove, чтобы дропнуть сам блок
        super.onRemove(state, world, pos, newState, isMoving);
    }

}
