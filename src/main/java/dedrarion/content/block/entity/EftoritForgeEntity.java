package dedrarion.content.block.entity;

import dedrarion.api.block.entity.BaseCraftingBlockEntity;
import dedrarion.api.util.RecipeUtils;
import dedrarion.content.recipe.EftoritForgeRecipe;
import dedrarion.content.recipe.EftoritIngredient;
import dedrarion.registry.ModBlockEntities;
import dedrarion.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EftoritForgeEntity extends BaseCraftingBlockEntity<EftoritForgeRecipe> {

    private static final int INVENTORY_SIZE = 15;
    private static final int CRAFT_TIME     = 100; // 5 seconds

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EFTORIT_FORGE_ENTITY.get(), pos, state);
    }

    // --- BaseCraftingBlockEntity ---

    @Override
    protected int getInventorySize() {
        return INVENTORY_SIZE;
    }

    @Override
    protected int getCraftTime() {
        return CRAFT_TIME;
    }

    @Override
    protected @NotNull ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return !isCrafting;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    protected void checkForRecipe(Level level) {
        Container snap = snapshot();
        RecipeUtils.findMatch(level, ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get(), snap)
                .ifPresent(r -> startCrafting(r, r.assemble(snap, level.registryAccess())));
    }

    @Override
    protected void finishCrafting(Level level) {
        if (currentRecipe == null) return;

        // Assemble BEFORE consuming — NBT source ingredient may be consumed next.
        ItemStack result = currentRecipe.assemble(snapshot(), level.registryAccess());
        processRecipe(currentRecipe);

        if (!result.isEmpty() && !tryPlaceResult(result)) {
            spawnItem(result);
        }

        resetCrafting();
    }

    @Override
    protected void restoreRecipeById(Level level, ResourceLocation recipeId) {
        RecipeUtils.findAll(level, ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get(), snapshot())
                .stream()
                .filter(r -> r.getId().equals(recipeId))
                .findFirst()
                .ifPresent(r -> currentRecipe = r);
    }

    @Override
    protected void onCraftingTick(Level level) {
        if (!(level instanceof ServerLevel serverLevel) || currentRecipe == null) return;

        double x = worldPosition.getX() + 0.5;
        double y = worldPosition.getY() + 0.9;
        double z = worldPosition.getZ() + 0.5;

        for (EftoritIngredient ingredient : currentRecipe.getEftoritIngredients()) {
            ItemStack[] items = ingredient.ingredient().getItems();
            if (items.length == 0) continue;

            double angle = level.random.nextDouble() * 2 * Math.PI;
            double xOff  = Math.cos(angle) * 0.25;
            double zOff  = Math.sin(angle) * 0.25;

            serverLevel.sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, items[0]),
                    x + xOff, y, z + zOff, 1, 0, 0, 0, 0
            );
            serverLevel.sendParticles(
                    ParticleTypes.GLOW,
                    x + xOff, y, z + zOff, 1, 0, 0.1, 0, 0
            );
        }
    }

    // --- Tick entry point ---

    public static void tick(Level level, EftoritForgeEntity entity) {
        entity.tick(level);
    }

    // --- Helpers ---

    /** Tries to place the result into the first empty slot. */
    private boolean tryPlaceResult(ItemStack result) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                itemHandler.setStackInSlot(i, result.copy());
                return true;
            }
        }
        return false;
    }

    /** Consumes ingredients marked as consumable in the recipe. */
    private void processRecipe(EftoritForgeRecipe recipe) {
        for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
            if (!ingredient.consume()) continue;
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (ingredient.ingredient().test(stack)) {
                    itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    // --- Public API ---

    /** Returns all non-empty stacks currently in the inventory. */
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) items.add(stack);
        }
        return items;
    }

    /** Removes and returns the last non-empty item from the inventory. */
    public ItemStack removeLastItem() {
        for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return itemHandler.extractItem(i, 1, false);
            }
        }
        return ItemStack.EMPTY;
    }

    /** Drops all inventory contents. Delegates to {@link #dropContents()}. */
    public void dropItems() {
        dropContents();
    }
}
