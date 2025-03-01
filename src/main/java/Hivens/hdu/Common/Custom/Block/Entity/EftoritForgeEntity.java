package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EftoritForgeEntity extends BlockEntity implements Container {
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ContainerData data;
    private int progress = 0;
    private final int maxProgress = 100;

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.EFTORIT_FORGE_ENTITY.get(), pos, state);
        this.data = new SimpleContainerData(2);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EftoritForgeEntity entity) {
        if (level.isClientSide) return;
        RecipeManager recipeManager = level.getRecipeManager();
        SimpleContainer inventory = new SimpleContainer(entity.items.size());
        for (int i = 0; i < entity.items.size(); i++) {
            inventory.setItem(i, entity.items.get(i));
        }

        // Используем SMELTING, так как CRAFTING требует CraftingContainer
        Recipe<?> recipe = recipeManager.getRecipeFor(RecipeType.SMELTING, inventory, level).orElse(null);
        if (recipe != null) {
            entity.progress++;
            if (entity.progress >= entity.maxProgress) {
                entity.craftItem(recipe, level);
                entity.progress = 0;
            }
        } else {
            entity.progress = 0;
        }
    }

    private void craftItem(Recipe<?> recipe, Level level) {
        ItemStack result = recipe.getResultItem(level.registryAccess()); // Исправленный вызов
        items.set(2, result.copy());
        for (int i = 0; i < 2; i++) {
            items.get(i).shrink(1);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", progress);
        ContainerHelper.saveAllItems(tag, items);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        items.set(slot, stack);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
