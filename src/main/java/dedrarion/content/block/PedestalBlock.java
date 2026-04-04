package dedrarion.content.block;

import dedrarion.content.block.entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A decorative pedestal that displays a single item.
 * <p>
 * Interaction model:
 * <ul>
 *   <li>Right-click with item — place item on pedestal.</li>
 *   <li>Right-click empty hand — take item from pedestal.</li>
 *   <li>Break — drops stored item.</li>
 * </ul>
 */
@SuppressWarnings("deprecation")
public class PedestalBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.box(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

        if (pedestal.hasItem()) {
            // Take item from pedestal.
            player.getInventory().placeItemBackInInventory(pedestal.removeItem());
        } else if (!held.isEmpty()) {
            // Place one item onto pedestal.
            pedestal.setItem(held.copyWithCount(1));
            if (!player.getAbilities().instabuild) held.shrink(1);
        } else {
            return InteractionResult.PASS;
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                         @NotNull BlockState newState, boolean isMoving) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
            ItemStack stored = pedestal.removeItem();
            if (!stored.isEmpty()) popResource(level, pos, stored);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                 @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }
}
