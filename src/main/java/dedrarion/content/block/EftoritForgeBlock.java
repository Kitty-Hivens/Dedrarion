package dedrarion.content.block;

import dedrarion.content.block.entity.EftoritForgeEntity;
import dedrarion.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dedrarion.api.util.ShapeUtils.getVoxelShape;

/**
 * The Eftorit Forge block — a multi-ingredient crafting station.
 * <p>
 * Interaction model:
 * <ul>
 *   <li>Right-click with item — insert one item into the forge.</li>
 *   <li>Shift + right-click — remove the last inserted item.</li>
 *   <li>Item entity falls onto block — auto-insert.</li>
 *   <li>Break — drop all contents (skipped in creative).</li>
 * </ul>
 */
@SuppressWarnings("deprecation")
public class EftoritForgeBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // --- Hitbox shapes (matches the block model geometry exactly) ---

    private static final VoxelShape SHAPE_NORTH = buildBaseShape();
    private static final VoxelShape SHAPE_EAST  = rotate90(SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = rotate90(SHAPE_EAST);
    private static final VoxelShape SHAPE_WEST  = rotate90(SHAPE_SOUTH);

    /** Builds the base shape for NORTH facing. */
    private static VoxelShape buildBaseShape() {
        return Shapes.or(
                Shapes.box(3/16d,  0,       4/16d,  13/16d, 2/16d,  12/16d),
                Shapes.box(5/16d,  2/16d,   6/16d,  11/16d, 6/16d,  10/16d),
                Shapes.box(0,      6/16d,   3/16d,  1,      8/16d,  13/16d),
                Shapes.box(0,      10/16d,  3/16d,  1,      13/16d, 13/16d),
                Shapes.box(2/16d,  8/16d,   5/16d,  4/16d,  10/16d, 7/16d),
                Shapes.box(7/16d,  8/16d,   4/16d,  9/16d,  10/16d, 6/16d),
                Shapes.box(12/16d, 8/16d,   5/16d,  14/16d, 10/16d, 7/16d),
                Shapes.box(3/16d,  8/16d,   10/16d, 5/16d,  10/16d, 12/16d),
                Shapes.box(7/16d,  8/16d,   9/16d,  9/16d,  10/16d, 11/16d),
                Shapes.box(11/16d, 8/16d,   10/16d, 13/16d, 10/16d, 12/16d)
        );
    }

    /**
     * Rotates a VoxelShape 90 degrees clockwise around the Y-axis.
     * Each AABB is transformed: (x, y, z) → (1-z, y, x).
     */
    private static VoxelShape rotate90(VoxelShape shape) {
        return getVoxelShape(shape);
    }

    private VoxelShape getShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case EAST  -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST  -> SHAPE_WEST;
            default    -> SHAPE_NORTH;
        };
    }

    // --- Construction ---

    public EftoritForgeBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    // --- BlockState ---

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // Face toward the player who placed the block.
        return this.defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // --- Shapes ---

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world,
                                        @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return getShape(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world,
                                                 @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return getShape(state);
    }

    // --- BlockEntity ---

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EftoritForgeEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, entity) -> {
            if (entity instanceof EftoritForgeEntity forge) {
                EftoritForgeEntity.tick(lvl, forge);
            }
        };
    }

    // --- Interaction ---

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                          @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof EftoritForgeEntity forge)) return InteractionResult.PASS;
        if (forge.isCrafting()) return InteractionResult.PASS;

        // Shift + RMB — remove the last inserted item.
        if (player.isShiftKeyDown()) {
            ItemStack removed = forge.removeLastItem();
            if (!removed.isEmpty()) {
                player.getInventory().placeItemBackInInventory(removed);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        // RMB — insert one item from the player's hand.
        ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) return InteractionResult.PASS;

        var cap = forge.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!cap.isPresent()) return InteractionResult.PASS;

        ItemStack remainder = ItemHandlerHelper.insertItem(
                cap.resolve().orElseThrow(),
                held.copyWithCount(1),
                false
        );

        if (remainder.isEmpty()) {
            if (!player.getAbilities().instabuild) held.shrink(1);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    /**
     * Auto-inserts item entities that land on top of the forge.
     * Ignored while crafting is in progress.
     */
    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                             @NotNull Entity entity) {
        if (level.isClientSide || !(entity instanceof ItemEntity item)) return;

        level.getBlockEntity(pos, ModBlockEntities.EFTORIT_FORGE_ENTITY.get()).ifPresent(forge -> {
            if (forge.isCrafting()) return;

            forge.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                ItemStack remainder = ItemHandlerHelper.insertItem(handler, item.getItem(), false);
                if (remainder.isEmpty()) {
                    item.discard();
                } else {
                    item.setItem(remainder);
                }
            });
        });
    }

    /** Drops all forge contents when the block is broken (skipped in creative). */
    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                                  @NotNull Player player) {
        if (!player.getAbilities().instabuild
                && level.getBlockEntity(pos) instanceof EftoritForgeEntity forge) {
            forge.dropItems();
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
