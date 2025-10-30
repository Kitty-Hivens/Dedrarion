package hivens.hdu.common.blocks.model;

import hivens.hdu.common.blocks.entity.EftoritForgeEntity;
import hivens.hdu.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EftoritForgeBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    public enum Mode implements StringRepresentable {
        SIDE("side"), FRONT("front");

        private final String name;
        Mode(String name) { this.name = name; }

        @Override
        public @NotNull String getSerializedName() { return this.name; }
    }

    private static final VoxelShape BASE_NORTH_SHAPE = Shapes.or(
            Shapes.box(3/16d,0,4/16d,13/16d,2/16d,12/16d),
            Shapes.box(5/16d,2/16d,6/16d,11/16d,6/16d,10/16d),
            Shapes.box(0,6/16d,3/16d,1,8/16d,13/16d),
            Shapes.box(0,10/16d,3/16d,1,13/16d,13/16d),
            Shapes.box(2/16d,8/16d,5/16d,4/16d,10/16d,7/16d),
            Shapes.box(7/16d,8/16d,4/16d,9/16d,10/16d,6/16d),
            Shapes.box(12/16d,8/16d,5/16d,14/16d,10/16d,7/16d),
            Shapes.box(3/16d,8/16d,10/16d,5/16d,10/16d,12/16d),
            Shapes.box(7/16d,8/16d,9/16d,9/16d,10/16d,11/16d),
            Shapes.box(11/16d,8/16d,10/16d,13/16d,10/16d,12/16d)
    );

    private static final VoxelShape NORTH_SHAPE = BASE_NORTH_SHAPE;
    private static final VoxelShape EAST_SHAPE = rotateShape(Direction.EAST);
    private static final VoxelShape SOUTH_SHAPE = rotateShape(Direction.SOUTH);
    private static final VoxelShape WEST_SHAPE = rotateShape(Direction.WEST);

    private static VoxelShape rotateShape(Direction target) {
        int steps = switch (target) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
        VoxelShape result = BASE_NORTH_SHAPE;
        for (int i = 0; i < steps; i++) result = rotate90(result);
        return result;
    }

    private static VoxelShape rotate90(VoxelShape shape) {
        VoxelShape result = Shapes.empty();
        for (AABB aabb : shape.toAabbs()) {
            double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
            double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
            result = Shapes.or(result, Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX));
        }
        return result;
    }

    public EftoritForgeBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MODE, Mode.SIDE));
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getClickedFace();
        if (facing.getAxis() == Direction.Axis.Y) facing = ctx.getHorizontalDirection();
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(MODE, facing.getAxis() == Direction.Axis.Y ? Mode.SIDE : Mode.FRONT);
    }

    private VoxelShape getShapeForFacing(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> Shapes.block();
        };
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return getShapeForFacing(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return getShapeForFacing(state);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        // логика при установке блока, если нужна
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EftoritForgeEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, entity) -> {
            if (entity instanceof EftoritForgeEntity forge) {
                EftoritForgeEntity.tick(lvl, forge); // вызываем статический метод с двумя параметрами
            }
        };
    }

    @Override
    public void playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof EftoritForgeEntity forgeEntity) {
            forgeEntity.dropItems();
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof EftoritForgeEntity forge)) return InteractionResult.PASS;

        if (forge.isCrafting()) return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(hand);

        // Логика ИЗЪЯТИЯ (если рука пустая)
        if (heldItem.isEmpty()) {
            if (!forge.getItems().isEmpty()) {
                player.getInventory().placeItemBackInInventory(forge.removeLastItem());
                return InteractionResult.CONSUME;
            }
            // Логика ДОБАВЛЕНИЯ (если в руке есть предмет)
        } else {
            // Пытаемся вставить предмет через ItemHandler
            ItemStack remainder = ItemHandlerHelper.insertItem(
                    forge.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null),
                    heldItem.copy(),
                    false
            );

            // Если предмет вставился (полностью или частично)
            if (remainder.getCount() != heldItem.getCount()) {
                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, remainder); // Возвращаем остаток (если он есть)
                }
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS; // Ничего не произошло
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (level.isClientSide || !(entity instanceof ItemEntity item)) return;

        level.getBlockEntity(pos, ModBlockEntities.EFTORIT_FORGE_ENTITY.get()).ifPresent(forge -> {
            // Получаем IItemHandler нашего блока
            forge.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                ItemStack stackToInsert = item.getItem();

                // ItemHandlerHelper.insertItem - стандартный способ вставить предмет
                // Он учтёт все наши правила (1 предмет на слот, запрет на выходной слот)
                ItemStack remainder = ItemHandlerHelper.insertItem(handler, stackToInsert, false);

                if (remainder.isEmpty()) {
                    item.discard();
                } else {
                    item.setItem(remainder);
                }
            });
        });
    }

}
