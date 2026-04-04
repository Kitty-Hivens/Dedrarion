package dedrarion.api.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

/**
 * Utility methods for recipe lookup.
 */
public final class RecipeUtils {

    private RecipeUtils() {}

    /**
     * Finds the first recipe of the given type that matches the provided container.
     *
     * @param level      the current level
     * @param type       recipe type to search
     * @param container  inventory snapshot to match against
     * @param <C>        container type
     * @param <R>        recipe type
     * @return first matching recipe, or empty
     */
    public static <C extends Container, R extends Recipe<C>> Optional<R> findMatch(
            Level level,
            RecipeType<R> type,
            C container
    ) {
        return level.getRecipeManager()
            .getAllRecipesFor(type)
            .stream()
            .filter(r -> r.matches(container, level))
            .findFirst();
    }

    /**
     * Returns all recipes of the given type that match the provided container.
     *
     * @param level      the current level
     * @param type       recipe type to search
     * @param container  inventory snapshot to match against
     * @param <C>        container type
     * @param <R>        recipe type
     * @return list of all matching recipes, may be empty
     */
    public static <C extends Container, R extends Recipe<C>> List<R> findAll(
            Level level,
            RecipeType<R> type,
            C container
    ) {
        return level.getRecipeManager()
            .getAllRecipesFor(type)
            .stream()
            .filter(r -> r.matches(container, level))
            .toList();
    }
}
