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

    public EftoritForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MODE, Mode.SIDE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction facing = pContext.getClickedFace();

        if (facing.getAxis() == Direction.Axis.Y) {
            facing = pContext.getHorizontalDirection();
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(MODE, facing.getAxis() == Direction.Axis.Y ? Mode.SIDE : Mode.FRONT);
    }

    VoxelShape NorthShape = Shapes.or(
            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 0.0D / 16.0D,  /*Z1*/ 4.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 2.0D / 16.0D,  /*Z2*/ 12.0D / 16.0D),

            Shapes.box(/*X1*/ 5.0D / 16.0D,  /*Y1*/ 2.0D / 16.0D,  /*Z1*/ 6.0D / 16.0D,
                       /*X2*/ 11.0D / 16.0D, /*Y2*/ 6.0D / 16.0D,  /*Z2*/ 10.0D / 16.0D),

            Shapes.box(/*X1*/ 0.0D / 16.0D,  /*Y1*/ 6.0D / 16.0D,  /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 1.0D,          /*Y2*/ 8.0D / 16.0D,  /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 0.0D / 16.0D,  /*Y1*/ 10.0D / 16.0D, /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 1.0D,          /*Y2*/ 13.0D / 16.0D, /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 2.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 5.0D / 16.0D,
                       /*X2*/ 4.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 7.0D / 16.0D),

            Shapes.box(/*X1*/ 7.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 4.0D / 16.0D,
                       /*X2*/ 9.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 6.0D / 16.0D),

            Shapes.box(/*X1*/ 12.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 5.0D / 16.0D,
                       /*X2*/ 14.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 7.0D / 16.0D),

            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 10.0D / 16.0D,
                       /*X2*/ 5.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 12.0D / 16.0D),

            Shapes.box(/*X1*/ 7.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 9.0D / 16.0D,
                       /*X2*/ 9.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 11.0D / 16.0D),

            Shapes.box(/*X1*/ 11.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 10.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 12.0D / 16.0D)
    );

    VoxelShape SouthShape = Shapes.or(
            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 0.0D / 16.0D,  /*Z1*/ 4.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 2.0D / 16.0D,  /*Z2*/ 12.0D / 16.0D),

            Shapes.box(/*X1*/ 5.0D / 16.0D,  /*Y1*/ 2.0D / 16.0D,  /*Z1*/ 6.0D / 16.0D,
                       /*X2*/ 11.0D / 16.0D, /*Y2*/ 6.0D / 16.0D,  /*Z2*/ 10.0D / 16.0D),

            Shapes.box(/*X1*/ 0.0D / 16.0D,  /*Y1*/ 6.0D / 16.0D,  /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 1.0D,          /*Y2*/ 8.0D / 16.0D,  /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 0.0D / 16.0D,  /*Y1*/ 10.0D / 16.0D, /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 1.0D,          /*Y2*/ 13.0D / 16.0D, /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 2.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 9.0D / 16.0D,
                       /*X2*/ 4.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 11.0D / 16.0D),

            Shapes.box(/*X1*/ 7.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 10.0D / 16.0D,
                       /*X2*/ 9.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 12.0D / 16.0D),

            Shapes.box(/*X1*/ 12.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 9.0D / 16.0D,
                       /*X2*/ 14.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 11.0D / 16.0D),

            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 4.0D / 16.0D,
                       /*X2*/ 5.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 6.0D / 16.0D),

            Shapes.box(/*X1*/ 7.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 5.0D / 16.0D,
                       /*X2*/ 9.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 7.0D / 16.0D),

            Shapes.box(/*X1*/ 11.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 4.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 6.0D / 16.0D)
    );


    VoxelShape EastShape = Shapes.or(
            Shapes.box(/*X1*/ 4.0D / 16.0D,  /*Y1*/ 0.0D / 16.0D,  /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 12.0D / 16.0D, /*Y2*/ 2.0D / 16.0D,  /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 6.0D / 16.0D,  /*Y1*/ 2.0D / 16.0D,  /*Z1*/ 5.0D / 16.0D,
                       /*X2*/ 10.0D / 16.0D, /*Y2*/ 6.0D / 16.0D,  /*Z2*/ 11.0D / 16.0D),

            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 6.0D / 16.0D,  /*Z1*/ 0.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 8.0D / 16.0D,  /*Z2*/ 1.0D),

            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 10.0D / 16.0D, /*Z1*/ 0.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 13.0D / 16.0D, /*Z2*/ 1.0D),

            Shapes.box(/*X1*/ 4.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 6.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 5.0D / 16.0D),

            Shapes.box(/*X1*/ 5.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 7.0D / 16.0D,
                       /*X2*/ 7.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 9.0D / 16.0D),

            Shapes.box(/*X1*/ 4.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 11.0D / 16.0D,
                       /*X2*/ 6.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 9.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 2.0D / 16.0D,
                       /*X2*/ 11.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 4.0D / 16.0D),

            Shapes.box(/*X1*/ 10.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 7.0D / 16.0D,
                       /*X2*/ 12.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 9.0D / 16.0D),

            Shapes.box(/*X1*/ 9.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 12.0D / 16.0D,
                       /*X2*/ 11.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 14.0D / 16.0D)
    );


    VoxelShape WestShape = Shapes.or(
            Shapes.box(/*X1*/ 4.0D / 16.0D,  /*Y1*/ 0.0D / 16.0D, /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 12.0D / 16.0D, /*Y2*/ 2.0D / 16.0D, /*Z2*/ 13.0D / 16.0D),

            Shapes.box(/*X1*/ 6.0D / 16.0D,  /*Y1*/ 2.0D / 16.0D, /*Z1*/ 5.0D / 16.0D,
                       /*X2*/ 10.0D / 16.0D, /*Y2*/ 6.0D / 16.0D, /*Z2*/ 11.0D / 16.0D),

            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 6.0D / 16.0D, /*Z1*/ 0.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 8.0D / 16.0D, /*Z2*/ 1.0D),

            Shapes.box(/*X1*/ 3.0D / 16.0D,  /*Y1*/ 10.0D / 16.0D, /*Z1*/ 0.0D / 16.0D,
                       /*X2*/ 13.0D / 16.0D, /*Y2*/ 13.0D / 16.0D, /*Z2*/ 1.0D),

            Shapes.box(/*X1*/ 5.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 2.0D / 16.0D,
                       /*X2*/ 7.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 4.0D / 16.0D),

            Shapes.box(/*X1*/ 4.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 7.0D / 16.0D,
                       /*X2*/ 6.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 9.0D / 16.0D),

            Shapes.box(/*X1*/ 5.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 12.0D / 16.0D,
                       /*X2*/ 7.0D / 16.0D,  /*Y2*/ 10.0D / 16.0D, /*Z2*/ 14.0D / 16.0D),

            Shapes.box(/*X1*/ 10.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 3.0D / 16.0D,
                       /*X2*/ 12.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 5.0D / 16.0D),

            Shapes.box(/*X1*/ 9.0D / 16.0D,  /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 7.0D / 16.0D,
                       /*X2*/ 11.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 9.0D / 16.0D),

            Shapes.box(/*X1*/ 10.0D / 16.0D, /*Y1*/ 8.0D / 16.0D,  /*Z1*/ 11.0D / 16.0D,
                       /*X2*/ 12.0D / 16.0D, /*Y2*/ 10.0D / 16.0D, /*Z2*/ 13.0D / 16.0D)
    );


    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        Direction facing = pState.getValue(FACING); // Получаем направление

        return switch (facing) {
            case NORTH -> NorthShape;
            case SOUTH -> SouthShape;
            case EAST -> EastShape;
            case WEST -> WestShape;
            default -> super.getShape(pState, pLevel, pPos, pContext);
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);

        return switch (facing) {
            case NORTH -> NorthShape;
            case SOUTH -> SouthShape;
            case EAST -> EastShape;
            case WEST -> WestShape;
            default -> super.getCollisionShape(state, world, pos, context);
        };
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
