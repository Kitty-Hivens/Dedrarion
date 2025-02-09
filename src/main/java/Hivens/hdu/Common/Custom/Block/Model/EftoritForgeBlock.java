package Hivens.hdu.Common.Custom.Block.Model;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class EftoritForgeBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    public enum Mode implements StringRepresentable {
        SIDE("side"),
        FRONT("front");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    /*
     * Определяем базовую форму для направления NORTH.
     * Обратите внимание, что все координаты задаются как дроби (от 0 до 1),
     * где 1 соответствует 16/16.
     */
    private static final VoxelShape BASE_NORTH_SHAPE = Shapes.or(
            // Нижняя пластина
            Shapes.box(3.0D / 16.0D,  0.0D / 16.0D,  4.0D / 16.0D,
                    13.0D / 16.0D, 2.0D / 16.0D,  12.0D / 16.0D),
            // Основное тело
            Shapes.box(5.0D / 16.0D,  2.0D / 16.0D,  6.0D / 16.0D,
                    11.0D / 16.0D, 6.0D / 16.0D,  10.0D / 16.0D),
            // Левый боковой элемент (нижний)
            Shapes.box(0.0D / 16.0D,  6.0D / 16.0D,  3.0D / 16.0D,
                    1.0D,          8.0D / 16.0D,  13.0D / 16.0D),
            // Левый боковой элемент (верхний)
            Shapes.box(0.0D / 16.0D,  10.0D / 16.0D, 3.0D / 16.0D,
                    1.0D,          13.0D / 16.0D, 13.0D / 16.0D),
            // Детали (разные мелкие элементы)
            Shapes.box(2.0D / 16.0D,  8.0D / 16.0D,  5.0D / 16.0D,
                    4.0D / 16.0D,  10.0D / 16.0D, 7.0D / 16.0D),
            Shapes.box(7.0D / 16.0D,  8.0D / 16.0D,  4.0D / 16.0D,
                    9.0D / 16.0D,  10.0D / 16.0D, 6.0D / 16.0D),
            Shapes.box(12.0D / 16.0D, 8.0D / 16.0D,  5.0D / 16.0D,
                    14.0D / 16.0D, 10.0D / 16.0D, 7.0D / 16.0D),
            Shapes.box(3.0D / 16.0D,  8.0D / 16.0D,  10.0D / 16.0D,
                    5.0D / 16.0D,  10.0D / 16.0D, 12.0D / 16.0D),
            Shapes.box(7.0D / 16.0D,  8.0D / 16.0D,  9.0D / 16.0D,
                    9.0D / 16.0D,  10.0D / 16.0D, 11.0D / 16.0D),
            Shapes.box(11.0D / 16.0D, 8.0D / 16.0D,  10.0D / 16.0D,
                    13.0D / 16.0D, 10.0D / 16.0D, 12.0D / 16.0D)
    );

    // Получаем формы для остальных направлений путём поворота базовой формы.
    private static final VoxelShape NORTH_SHAPE = BASE_NORTH_SHAPE;
    private static final VoxelShape EAST_SHAPE = rotateShape(Direction.EAST);
    private static final VoxelShape SOUTH_SHAPE = rotateShape(Direction.SOUTH);
    private static final VoxelShape WEST_SHAPE = rotateShape(Direction.WEST);

    /**
     * Поворачивает исходную форму BASE_NORTH_SHAPE так, чтобы она соответствовала targetDirection.
     * Поворот осуществляется кратно 90° вокруг оси Y.
     */
    private static VoxelShape rotateShape(Direction targetDirection) {
        // Определяем число 90-градусных поворотов, необходимых от NORTH до targetDirection
        int rotationSteps = switch (targetDirection) {
            case EAST  -> 1;
            case SOUTH -> 2;
            case WEST  -> 3;
            default    -> 0;
        };

        VoxelShape result = EftoritForgeBlock.BASE_NORTH_SHAPE;
        for (int i = 0; i < rotationSteps; i++) {
            result = rotateShape90(result);
        }
        return result;
    }

    /**
     * Поворачивает форму на 90 градусов по часовой стрелке вокруг оси Y.
     * Для каждого AABB (ограничивающего параллелепипеда) вычисляем новые координаты:
     * newMinX = 1 - oldMaxZ, newMinZ = oldMinX, newMaxX = 1 - oldMinZ, newMaxZ = oldMaxX.
     */
    private static VoxelShape rotateShape90(VoxelShape shape) {
        VoxelShape result = Shapes.empty();
        for (AABB aabb : shape.toAabbs()) {
            double minX = aabb.minX;
            double minY = aabb.minY;
            double minZ = aabb.minZ;
            double maxX = aabb.maxX;
            double maxY = aabb.maxY;
            double maxZ = aabb.maxZ;
            double rMinX = 1 - maxZ;
            double rMaxX = 1 - minZ;
            result = Shapes.or(result, Shapes.box(rMinX, minY, minX, rMaxX, maxY, maxX));
        }
        return result;
    }

    public EftoritForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MODE, Mode.SIDE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        if (facing.getAxis() == Direction.Axis.Y) {
            facing = context.getHorizontalDirection();
        }
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(MODE, facing.getAxis() == Direction.Axis.Y ? Mode.SIDE : Mode.FRONT);
    }

    // Вспомогательный метод для выбора формы по направлению
    private VoxelShape getShapeForFacing(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST  -> EAST_SHAPE;
            case WEST  -> WEST_SHAPE;
            default    -> Shapes.block();
        };
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return getShapeForFacing(state);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
            @NotNull BlockState state,
            @NotNull BlockGetter world,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context) {
        return getShapeForFacing(state);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE);
    }
}
