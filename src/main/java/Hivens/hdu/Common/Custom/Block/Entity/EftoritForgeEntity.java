package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import Hivens.hdu.Common.Registry.ModRecipes;
import Hivens.hdu.Common.recipe.EftoritForgeRecipe;
import Hivens.hdu.Common.recipe.EftoritIngredient;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class EftoritForgeEntity extends BlockEntity implements Container {
    private List<ItemStack> inventory;
    private EftoritForgeRecipe currentRecipe;
    private int craftTimer = 0;
    private boolean isCrafting = false;

    public static final Logger LOGGER = LogUtils.getLogger();

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.EFTORIT_FORGE_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(32, ItemStack.EMPTY);
    }

    public List<ItemStack> getInventory() {
        return inventory;
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
                entity.sync(); //  Обновляем клиент после крафта
            }
        } else {
            entity.checkForRecipe();
        }
    }

    public List<ItemStack> getVisibleInventory() {
        return inventory.stream().filter(stack -> !stack.isEmpty()).toList();
    }



    private void checkForRecipe() {
        assert level != null;
        //LOGGER.debug("[checkForRecipe] Запуск проверки рецептов...");

        List<EftoritForgeRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get());

        //LOGGER.debug("[checkForRecipe] Найдено рецептов: {}", recipes.size());

        Optional<EftoritForgeRecipe> optionalRecipe = recipes.stream()
                .filter(recipe -> {
                    boolean match = recipe.matches(this, level);
                    //LOGGER.debug("[checkForRecipe] Проверяем рецепт {}: {}", recipe.getId(), match);
                    return match;
                })
                .findFirst();

        if (optionalRecipe.isPresent()) {
            currentRecipe = optionalRecipe.get();
            isCrafting = true;
            craftTimer = 0;
            setChanged();
            sync();
            //LOGGER.info("[checkForRecipe] Найден рецепт: {}", currentRecipe.getId());
        } else {
            //LOGGER.warn("[checkForRecipe] Рецепт не найден");
        }
    }





    private void craftItem() {
        if (currentRecipe == null) {
            //LOGGER.warn("Попытка создать предмет без рецепта!");
            return;
        }

        processRecipe(currentRecipe); // Используем ресурсы

        if (level == null) {
            LOGGER.error("Уровень равен null во время крафта!");
            return;
        }

        ItemStack result = currentRecipe.getResultItem(level.registryAccess());
        if (result.isEmpty()) {
            LOGGER.warn("Результат рецепта пуст!");
            return;
        }

        boolean added = false;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, result);
                added = true;
                LOGGER.info("Создан предмет {} и добавлен в слот {}", result.getItem(), i);
                break;
            }
        }

        if (!added) {
            LOGGER.warn("Инвентарь заполнен, предмет {} не может быть добавлен!", result.getItem());
        }

        currentRecipe = null;
        setChanged();
    }




    private void spawnResult(ItemStack stack) {
        assert level != null;
        ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, stack);
        level.addFreshEntity(itemEntity);
    }

    public void dropItems() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                spawnResult(stack);
            }
        }
    }


    public ItemStack removeLastItem() {
        for (int i = inventory.size() - 1; i >= 0; i--) {
            if (!inventory.get(i).isEmpty()) {
                ItemStack removed = inventory.get(i);
                inventory.set(i, ItemStack.EMPTY); // Очищаем слот

                setChanged();
                return removed; // Возвращаем предмет
            }
        }
        return ItemStack.EMPTY; // Если инвентарь пустой, возвращаем пустой стак
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag); // Сначала базовое сохранение
        System.out.println("Сохранение данных NBT для " + this.worldPosition);
        tag.put("Inventory", ContainerHelper.saveAllItems(new CompoundTag(), (NonNullList<ItemStack>) inventory));
    }


    public void processRecipe(EftoritForgeRecipe recipe) {
        for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
            for (ItemStack stack : inventory) {
                if (ingredient.ingredient().test(stack)) {
                    if (ingredient.consume()) {
                        stack.shrink(1);
                    }
                    break;
                }
            }
        }
    }




    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag); // Сначала базовая загрузка
        System.out.println("Загрузка данных NBT для " + this.worldPosition);

        // Не создаём новый список, а очищаем старый
        if (inventory == null || inventory.size() != 32) {
            inventory = NonNullList.withSize(32, ItemStack.EMPTY);
        } else {
            inventory.clear();
        }

        ContainerHelper.loadAllItems(tag.getCompound("Inventory"), (NonNullList<ItemStack>) inventory);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    public void sync() {
        if (level != null && !level.isClientSide) {
            System.out.println("[sync] Отправка данных клиенту для " + this.worldPosition);
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }



    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.load(tag);
        System.out.println("[handleUpdateTag] Клиент обновил инвентарь на " + this.worldPosition);
    }



    public boolean addItem(ItemStack stack, ItemEntity itemEntity) {
        if (stack.isEmpty()) {
            System.out.println("Попытка добавить пустой стак!");
            return false;
        }

        System.out.println("Попытка добавить: " + stack.getCount() + " " + stack.getItem());

        System.out.println("Текущее состояние инвентаря:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println("Слот " + i + ": " + inventory.get(i));
        }

        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                System.out.println("Свободный слот найден: " + i);

                // Берём один предмет из стака
                ItemStack singleItem = stack.copy();
                singleItem.setCount(1);
                inventory.set(i, singleItem);

                // Уменьшаем исходный стак на 1
                stack.shrink(1);

                // Обновляем `ItemEntity`
                if (stack.isEmpty()) {
                    System.out.println("Все предметы использованы, удаляем ItemEntity!");
                    itemEntity.discard();
                } else {
                    System.out.println("Обновляем ItemEntity, осталось: " + stack.getCount());
                    itemEntity.setItem(stack);
                }

                setChanged();
                return true;
            }
        }

        System.out.println("Нет свободных слотов для предмета!");
        return false; // Нет свободных слотов
    }







    public List<ItemStack> getItems() {
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
    public @NotNull ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
