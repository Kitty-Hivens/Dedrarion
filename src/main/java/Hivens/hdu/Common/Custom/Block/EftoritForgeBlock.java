package Hivens.hdu.Common.Custom.Block;

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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EftoritForgeBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Mode> MODE = EnumProperty.create("mode", Mode.class);

    // Определение режимов с реализацией StringRepresentable
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

    private static final VoxelShape SHAPE_FRONT = Shapes.or(
            // Нижняя пластина (3, 0, 4 to 13, 2, 12)
            Shapes.box(3.0D/16.0D, 0.0D/16.0D, 4.0D/16.0D, 13.0D/16.0D, 2.0D/16.0D, 12.0D/16.0D),

            // Средний блок (5, 2, 6 to 11, 6, 10)
            Shapes.box(5.0D/16.0D, 2.0D/16.0D, 6.0D/16.0D, 11.0D/16.0D, 6.0D/16.0D, 10.0D/16.0D),

            // Средняя пластина (0, 6, 3 to 16, 8, 13)
            Shapes.box(0.0D/16.0D, 6.0D/16.0D, 3.0D/16.0D, 1.0, 8.0D/16.0D, 13.0D/16.0D),

            // Верхняя пластина (0, 10, 3 to 16, 13, 13)
            Shapes.box(0.0D/16.0D, 10.0D/16.0D, 3.0D/16.0D, 1.0, 13.0D/16.0D, 13.0D/16.0D),

            // Малые элементы
            Shapes.box(2.0D/16.0D, 8.0D/16.0D, 5.0D/16.0D, 4.0D/16.0D, 10.0D/16.0D, 7.0D/16.0D),   // Левый передний
            Shapes.box(7.0D/16.0D, 8.0D/16.0D, 4.0D/16.0D, 9.0D/16.0D, 10.0D/16.0D, 6.0D/16.0D),    // Центральный передний
            Shapes.box(12.0D/16.0D, 8.0D/16.0D, 5.0D/16.0D, 14.0D/16.0D, 10.0D/16.0D, 7.0D/16.0D),  // Правый передний

            Shapes.box(3.0D/16.0D, 8.0D/16.0D, 10.0D/16.0D, 5.0D/16.0D, 10.0D/16.0D, 12.0D/16.0D),  // Левый задний
            Shapes.box(7.0D/16.0D, 8.0D/16.0D, 9.0D/16.0D, 9.0D/16.0D, 10.0D/16.0D, 11.0D/16.0D),   // Центральный задний
            Shapes.box(11.0D/16.0D, 8.0D/16.0D, 10.0D/16.0D, 13.0D/16.0D, 10.0D/16.0D, 12.0D/16.0D) // Правый задний
    );

    private static final VoxelShape SHAPE_SIDE = Shapes.or(
            // Нижняя пластина (3, 0, 4 to 13, 2, 12)
            Shapes.box(4.0D/16.0D, 0.0D/16.0D, 3.0D/16.0D, 12.0D/16.0D, 2.0D/16.0D, 13.0D/16.0D),

            // Средний блок (5, 2, 6 to 11, 6, 10)
            Shapes.box(6.0D/16.0D, 2.0D/16.0D, 5.0D/16.0D, 10.0D/16.0D, 6.0D/16.0D, 11.0D/16.0D),

            // Средняя пластина (0, 6, 3 to 16, 8, 13)
            Shapes.box(3.0D/16.0D, 6.0D/16.0D, 0.0D/16.0D, 13.0D/16.0D, 8.0D/16.0D, 1.0),

            // Верхняя пластина (0, 10, 3 to 16, 13, 13)
            Shapes.box(3.0D/16.0D, 10.0D/16.0D, 0.0D/16.0D, 13.0D/16.0D, 13.0D/16.0D, 1.0),

            // Малые элементы
            Shapes.box(5.0D/16.0D, 8.0D/16.0D, 2.0D/16.0D, 7.0D/16.0D, 10.0D/16.0D, 4.0D/16.0D),   // Левый передний
            Shapes.box(4.0D/16.0D, 8.0D/16.0D, 7.0D/16.0D, 6.0D/16.0D, 10.0D/16.0D, 9.0D/16.0D),   // Центральный передний
            Shapes.box(5.0D/16.0D, 8.0D/16.0D, 12.0D/16.0D, 7.0D/16.0D, 10.0D/16.0D, 14.0D/16.0D), // Правый передний

            Shapes.box(10.0D/16.0D, 8.0D/16.0D, 3.0D/16.0D, 12.0D/16.0D, 10.0D/16.0D, 5.0D/16.0D), // Левый задний
            Shapes.box(9.0D/16.0D, 8.0D/16.0D, 7.0D/16.0D, 11.0D/16.0D, 10.0D/16.0D, 9.0D/16.0D),  // Центральный задний
            Shapes.box(10.0D/16.0D, 8.0D/16.0D, 11.0D/16.0D, 12.0D/16.0D, 10.0D/16.0D, 13.0D/16.0D) // Правый задний
    );

    public EftoritForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MODE, Mode.SIDE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Mode mode = pContext.getClickedFace().getAxis() == Direction.Axis.Y
                ? Mode.SIDE
                : Mode.FRONT;

        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection().getOpposite())
                .setValue(MODE, mode);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return pState.getValue(MODE) == Mode.SIDE ? SHAPE_SIDE : SHAPE_FRONT;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return pState.getValue(MODE) == Mode.SIDE ? SHAPE_SIDE : SHAPE_FRONT;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE);
    }
}