package dedrarion.api.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Utility methods for {@link VoxelShape} manipulation.
 */
public final class ShapeUtils {

    private ShapeUtils() {}

    /**
     * Rotates a {@link VoxelShape} to face the given horizontal {@link Direction}.
     * The input shape is assumed to face {@link Direction#NORTH}.
     *
     * @param shape  source shape (NORTH-facing)
     * @param facing target direction
     * @return rotated shape
     */
    public static VoxelShape rotate(VoxelShape shape, Direction facing) {
        return switch (facing) {
            case EAST  -> rotate90(shape, 1);
            case SOUTH -> rotate90(shape, 2);
            case WEST  -> rotate90(shape, 3);
            default    -> shape;
        };
    }

    /**
     * Rotates a {@link VoxelShape} 90 degrees clockwise around the Y-axis,
     * repeated {@code steps} times.
     *
     * @param shape source shape
     * @param steps number of 90-degree clockwise rotations (0–3)
     * @return rotated shape
     */
    public static VoxelShape rotate90(VoxelShape shape, int steps) {
        VoxelShape result = shape;
        for (int i = 0; i < steps; i++) {
            result = rotate90(result);
        }
        return result;
    }

    /**
     * Rotates a {@link VoxelShape} exactly 90 degrees clockwise around the Y-axis.
     * Each AABB is transformed: {@code (x, y, z) → (1-z, y, x)}.
     *
     * @param shape source shape
     * @return rotated shape
     */
    public static VoxelShape rotate90(VoxelShape shape) {
        return getVoxelShape(shape);
    }

    @NotNull
    public static VoxelShape getVoxelShape(VoxelShape shape) {
        VoxelShape result = Shapes.empty();
        for (AABB aabb : shape.toAabbs()) {
            result = Shapes.or(result, Shapes.box(
                1 - aabb.maxZ, aabb.minY, aabb.minX,
                1 - aabb.minZ, aabb.maxY, aabb.maxX
            ));
        }
        return result;
    }
}
