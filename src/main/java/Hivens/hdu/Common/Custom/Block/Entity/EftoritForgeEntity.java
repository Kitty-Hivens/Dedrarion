package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import Hivens.hdu.Common.Registry.ModRecipes;
import Hivens.hdu.Common.recipe.EftoritForgeRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class EftoritForgeEntity extends BlockEntity implements Container {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    private int craftTimer = 0;
    private boolean isCrafting = false;

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.EFTORIT_FORGE_ENTITY.get(), pos, state);
    }

    public boolean isCrafting() {
        return isCrafting;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EftoritForgeEntity entity) {
        if (level.isClientSide) return;

        if (entity.isCrafting) {
            entity.craftTimer++;
            if (entity.craftTimer >= 100) {
                entity.craftItem();
                entity.isCrafting = false;
                entity.craftTimer = 0;
            }
        } else {
            entity.checkForRecipe();
        }
    }

    private void checkForRecipe() {
        assert level != null;
        Optional<EftoritForgeRecipe> match = level.getRecipeManager()
                .getRecipeFor(ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get(), this, level);
        match.ifPresent(recipe -> isCrafting = true);
    }

    private void craftItem() {
        Optional<EftoritForgeRecipe> match = level.getRecipeManager()
                .getRecipeFor(ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get(), this, level);
        match.ifPresent(recipe -> {
            clearInventory();
            spawnResult(recipe.getResultItem(level.registryAccess()));
        });
    }


    private void spawnResult(ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, stack);
        level.addFreshEntity(itemEntity);
    }

    private void clearInventory() {
        inventory.clear();
    }

    public void dropItems() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                spawnResult(stack);
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    // Реализация Container
    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
