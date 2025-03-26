package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import Hivens.hdu.Common.Registry.ModRecipes;
import Hivens.hdu.Common.recipe.EftoritForgeRecipe;
import Hivens.hdu.Common.recipe.EftoritIngredient;
import Hivens.hdu.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class EftoritForgeEntity extends BlockEntity implements Container {
    private static BlockPos pos;
    private static BlockState state;
    private final List<ItemStack> inventory;
    private EftoritForgeRecipe currentRecipe;
    private int craftTimer = 0;
    private boolean isCrafting = false;

    public static final Logger LOGGER = LogUtils.getLogger();

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.EFTORIT_FORGE_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(32, ItemStack.EMPTY);
    }

    public static BlockState getState() {
        return state;
    }

    public boolean isCrafting() {
        return isCrafting;
    }



    public static void tick(Level level, BlockPos pos, BlockState state, EftoritForgeEntity entity) {
        EftoritForgeEntity.pos = pos;
        EftoritForgeEntity.state = state;
        if (level.isClientSide) return;

        if (entity.isCrafting) {
            entity.craftTimer++;
            if (entity.craftTimer >= 100) {
                entity.craftItem();
                entity.isCrafting = false;
                entity.craftTimer = 0;
                entity.sync(); // Обновляем клиент после крафта
            }
        } else {
            entity.checkForRecipe();
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }


    public void clientTick() {
        if (isCrafting) {
            assert level != null;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    }

    private void checkForRecipe() {
        assert level != null;

        // Если инвентарь пуст, нет смысла продолжать
        if (isEmpty()) {
            return;
        }

        if (Config.isDevMode()) {
            LOGGER.debug("[checkForRecipe] Запуск проверки рецептов...");
        }

        List<EftoritForgeRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get());
        if (Config.isDevMode()) {
            LOGGER.debug("[checkForRecipe] Найдено рецептов: {}", recipes.size());
        }

        Optional<EftoritForgeRecipe> optionalRecipe = recipes.stream()
                .filter(recipe -> {
                    boolean match = recipe.matches(this, level);

                    if (Config.isDevMode()) {
                        LOGGER.debug("[checkForRecipe] Проверяем рецепт {}: {}", recipe.getId(), match);
                    }

                    return match;
                })
                .findFirst();

        if (optionalRecipe.isPresent()) {
            currentRecipe = optionalRecipe.get();
            isCrafting = true;
            craftTimer = 0;
            setChanged();
            sync();

            if (Config.isDevMode()) {
                LOGGER.info("[checkForRecipe] Найден рецепт: {}", currentRecipe.getId());
            }

        } else if (Config.isDevMode()) {
            LOGGER.warn("[checkForRecipe] Рецепт не найден");
        }
    }


    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }




    private void craftItem() {
        if (currentRecipe == null) {
            if (Config.isDevMode()) {
                LOGGER.warn("Попытка создать предмет без рецепта!");
            }
            return;
        }

        processRecipe(currentRecipe); // Используем ресурсы

        if (level == null) {
            if (Config.isDevMode()) {
                LOGGER.error("Уровень равен null во время крафта!");
            }
            return;
        }

        ItemStack result = currentRecipe.getResultItem(level.registryAccess());
        if (result.isEmpty()) {
            if (Config.isDevMode()) {
                LOGGER.warn("Результат рецепта пуст!");
            }
            return;
        }

        boolean added = false;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                inventory.set(i, result);
                added = true;

                if (Config.isDevMode()) {
                    LOGGER.info("Создан предмет {} и добавлен в слот {}", result.getItem(), i);
                }

                break;
            }
        }

        if (!added && Config.isDevMode()) {
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
                inventory.set(i, ItemStack.EMPTY);

                setChanged();
                return removed;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (Config.isDevMode()) {
            LOGGER.debug("Сохранение данных NBT для {}", this.worldPosition);
        }

        CompoundTag inventoryTag = new CompoundTag();
        ContainerHelper.saveAllItems(inventoryTag, (NonNullList<ItemStack>) inventory);
        tag.put("Inventory", inventoryTag);
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
        super.load(tag);

        if (Config.isDevMode()) {
            LOGGER.debug("Загрузка данных NBT для {}", this.worldPosition);
        }

        inventory.clear();
        ContainerHelper.loadAllItems(tag.getCompound("Inventory"), (NonNullList<ItemStack>) inventory);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public void sync() {
        if (level != null && !level.isClientSide) {
            LOGGER.debug("[sync] Отправка данных клиенту для {}", this.worldPosition);
            setChanged();
            requestModelDataUpdate();  // Принудительное обновление
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    public boolean addItem(ItemStack stack, ItemEntity itemEntity) {
        if (stack.isEmpty()) {
            if (Config.isDevMode()) {
                LOGGER.warn("Попытка добавить пустой стак!");
            }
            return false;
        }

        if (Config.isDevMode()) {
            LOGGER.debug("Попытка добавить: {} {}", stack.getCount(), stack.getItem());
            LOGGER.debug("Текущее состояние инвентаря:");
            for (int i = 0; i < inventory.size(); i++) {
                LOGGER.debug("Слот {}: {}", i, inventory.get(i));
            }
        }

        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).isEmpty()) {
                if (Config.isDevMode()) {
                    LOGGER.debug("Свободный слот найден: {}", i);
                }

                // Берём один предмет из стака
                ItemStack singleItem = stack.copy();
                singleItem.setCount(1);
                inventory.set(i, singleItem);

                // Уменьшаем исходный стак на 1
                stack.shrink(1);

                // Обновляем `ItemEntity`
                if (stack.isEmpty()) {
                    if (Config.isDevMode()) {
                        LOGGER.debug("Все предметы использованы, удаляем ItemEntity!");
                    }
                    itemEntity.discard();
                } else {
                    if (Config.isDevMode()) {
                        LOGGER.debug("Обновляем ItemEntity, осталось: {}", stack.getCount());
                    }
                    itemEntity.setItem(stack);
                }

                setChanged();  // Флаг, что данные изменились
                if (level != null && !level.isClientSide) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                }

                return true;
            }
        }

        return false;
    }

    public List<ItemStack> getItems() {
        assert level != null;
        if (Config.isDevMode())
            System.out.println("getItems() вызван на " + (level.isClientSide ? "клиенте" : "сервере"));
        return inventory;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
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
        this.setChanged();
        sync();
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
