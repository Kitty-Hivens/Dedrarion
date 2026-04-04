package dedrarion.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for block entities that perform timed recipe crafting.
 * <p>
 * Extends {@link BaseContainerBlockEntity} with:
 * <ul>
 *   <li>Craft timer and configurable craft duration.</li>
 *   <li>Current recipe tracking with NBT persistence across world loads.</li>
 *   <li>Result stack for client-side rendering during crafting.</li>
 * </ul>
 *
 * <p>Subclasses must implement:
 * <ul>
 *   <li>{@link #getCraftTime()} — duration in ticks.</li>
 *   <li>{@link #checkForRecipe(Level)} — find and start a matching recipe.</li>
 *   <li>{@link #finishCrafting(Level)} — produce the result and reset state.</li>
 *   <li>{@link #restoreRecipeById(Level, ResourceLocation)} — re-attach recipe after world load.</li>
 *   <li>{@link #onCraftingTick(Level)} — optional per-tick effects (particles, sounds).</li>
 * </ul>
 *
 * @param <R> recipe type
 */
public abstract class BaseCraftingBlockEntity<R extends Recipe<?>> extends BaseContainerBlockEntity {

    @Nullable protected R currentRecipe;
    @Nullable private ResourceLocation pendingRecipeId;

    protected int craftTimer = 0;
    protected boolean isCrafting = false;
    protected ItemStack resultStack = ItemStack.EMPTY;

    protected BaseCraftingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // --- Abstract API ---

    /** Total ticks required to complete one craft cycle. */
    protected abstract int getCraftTime();

    /**
     * Called every tick while not crafting.
     * Check if the current inventory matches any recipe and call {@link #startCrafting(R, ItemStack)}
     * to begin if a match is found.
     */
    protected abstract void checkForRecipe(Level level);

    /**
     * Called when {@link #craftTimer} reaches {@link #getCraftTime()}.
     * Assemble the result, consume ingredients, place or spawn output, then call {@link #resetCrafting()}.
     */
    protected abstract void finishCrafting(Level level);

    /**
     * Re-attaches {@link #currentRecipe} from a saved {@link ResourceLocation} after world load.
     * The level's recipe manager is available at this point.
     *
     * @param level    the server level
     * @param recipeId saved recipe id
     */
    protected abstract void restoreRecipeById(Level level, ResourceLocation recipeId);

    /**
     * Called every other tick while crafting is active.
     * Override to spawn particles, play sounds, etc. No-op by default.
     */
    protected void onCraftingTick(Level level) {}

    // --- Tick ---

    /**
     * Main tick entry point. Call this from your static {@code tick(Level, T)} method.
     */
    protected void tick(Level level) {
        if (pendingRecipeId != null) {
            restoreRecipeById(level, pendingRecipeId);
            pendingRecipeId = null;
        }

        if (isCrafting) {
            tickCrafting(level);
        } else {
            checkForRecipe(level);
        }
    }

    private void tickCrafting(Level level) {
        if (level.getGameTime() % 2 == 0) {
            onCraftingTick(level);
        }
        if (++craftTimer >= getCraftTime()) {
            finishCrafting(level);
        }
    }

    // --- State helpers ---

    /**
     * Starts a crafting cycle with the given recipe and pre-assembled result.
     *
     * @param recipe the matched recipe
     * @param result pre-assembled result stack (call {@code recipe.assemble()} before consuming)
     */
    protected void startCrafting(R recipe, ItemStack result) {
        currentRecipe = recipe;
        resultStack = result;
        isCrafting = true;
        craftTimer = 0;
        sync();
    }

    /**
     * Resets all crafting state. Call at the end of {@link #finishCrafting(Level)}
     * or when cancelling mid-craft.
     */
    protected void resetCrafting() {
        currentRecipe = null;
        isCrafting = false;
        craftTimer = 0;
        resultStack = ItemStack.EMPTY;
        sync();
    }

    // --- Public API ---

    public boolean isCrafting()       { return isCrafting; }
    public int getCraftTimer()        { return craftTimer; }
    public ItemStack getResultStack() { return resultStack; }

    // --- NBT ---

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CraftTimer", craftTimer);
        tag.putBoolean("IsCrafting", isCrafting);
        tag.put("ResultStack", resultStack.save(new CompoundTag()));
        if (currentRecipe != null) {
            tag.putString("CurrentRecipe", currentRecipe.getId().toString());
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        craftTimer = tag.getInt("CraftTimer");
        isCrafting = tag.getBoolean("IsCrafting");
        resultStack = tag.contains("ResultStack")
            ? ItemStack.of(tag.getCompound("ResultStack"))
            : ItemStack.EMPTY;
        if (tag.contains("CurrentRecipe")) {
            pendingRecipeId = ResourceLocation.tryParse(tag.getString("CurrentRecipe"));
        }
    }
}
