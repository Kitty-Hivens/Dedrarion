package hivens.hdu.common.blocks.Entity;

import hivens.hdu.common.registry.BlockEntitiesRegistry;
import hivens.hdu.common.registry.ModRecipes;
import hivens.hdu.common.recipe.EftoritForgeRecipe;
import hivens.hdu.common.recipe.EftoritIngredient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EftoritForgeEntity extends BlockEntity { // Убираем 'implements Container'
    private static final int INVENTORY_SIZE = 16;
    private static final int OUTPUT_SLOT = 15;

    // --- НОВЫЙ СПОСОБ УПРАВЛЕНИЯ ИНВЕНТАРЁМ ---
    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // Этот метод вызывается АВТОМАТИЧЕСКИ при любом изменении инвентаря
            // Это решает проблему с рендером раз и навсегда!
            setChanged();
            sync();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // Запрещаем вставлять предметы в выходной слот
            return slot != OUTPUT_SLOT && super.isItemValid(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            // ГЛАВНОЕ: Ограничиваем каждый слот ОДНИМ предметом
            return 1;
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    // --- КОНЕЦ НОВОГО БЛОКА ---

    private EftoritForgeRecipe currentRecipe;
    private int craftTimer = 0;
    private boolean isCrafting = false;

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.EFTORIT_FORGE_ENTITY.get(), pos, state);
    }

    // Этот метод теперь нужен для проверки рецепта, т.к. мы убрали 'implements Container'
    public Container getRecipeMatcher() {
        SimpleContainer container = new SimpleContainer(INVENTORY_SIZE);
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }
        return container;
    }

    // --- ОБЯЗАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ CAPABILITIES ---
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    // --- КОНЕЦ ОБЯЗАТЕЛЬНЫХ МЕТОДОВ ---

    public static void tick(Level level, EftoritForgeEntity entity) {
        if (level == null || level.isClientSide) return;

        if (entity.isCrafting) {
            entity.craftTimer++;
            if (entity.craftTimer >= 100) {
                entity.craftItem();
                // isCrafting и craftTimer сбросятся внутри craftItem()
            }
        } else {
            entity.checkForRecipe();
        }
    }

    private void checkForRecipe() {
        if (level == null) return;

        Optional<EftoritForgeRecipe> optional = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get())
                .stream()
                .filter(r -> r.matches(getRecipeMatcher(), level))
                .findFirst();

        if (optional.isPresent()) {
            currentRecipe = optional.get();
            isCrafting = true;
            craftTimer = 0;
            // onContentsChanged() вызовется при крафте, так что sync() здесь не нужен
            setChanged();
        }
    }

    private void craftItem() {
        if (currentRecipe == null || level == null) return;

        processRecipe(currentRecipe); // Уменьшаем ингредиенты

        ItemStack result = currentRecipe.getResultItem(level.registryAccess());
        if (result.isEmpty()) return;

        // Кладём результат в выходной слот. Так как у нас 1 предмет на слот, логика упрощается
        if (itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, result.copy());
        } else {
            // Если выход занят, спавним в мир
            spawnResult(result.copy());
        }

        currentRecipe = null;
        isCrafting = false;
        craftTimer = 0;
        // setChanged() и sync() будут вызваны автоматически через onContentsChanged
    }

    public void processRecipe(EftoritForgeRecipe recipe) {
        for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (ingredient.ingredient().test(stack)) {
                    if (ingredient.consume()) {
                        // Просто удаляем предмет из слота
                        itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    }
                    break;
                }
            }
        }
    }

    // --- СОХРАНЕНИЕ И ЗАГРУЗКА ---
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        // Сохраняем инвентарь через itemHandler
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("CraftTimer", craftTimer);
        tag.putBoolean("IsCrafting", isCrafting);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        // Загружаем инвентарь
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        this.craftTimer = tag.getInt("CraftTimer");
        this.isCrafting = tag.getBoolean("IsCrafting");
    }

    // --- ВСПОМОГАТЕЛЬНЫЕ И СТАРЫЕ МЕТОДЫ (ОБНОВЛЕННЫЕ) ---

    public boolean isCrafting() {
        return isCrafting;
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
        return items;
    }

    public ItemStack removeLastItem() {
        for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                // extractItem вызовет onContentsChanged, который сделает sync()
                return itemHandler.extractItem(i, 1, false);
            }
        }
        return ItemStack.EMPTY;
    }

    public void dropItems() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                spawnResult(stack);
            }
        }
    }

    private void spawnResult(ItemStack stack) {
        if (level == null) return;
        ItemEntity itemEntity = new ItemEntity(level,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 1,
                worldPosition.getZ() + 0.5, stack);
        level.addFreshEntity(itemEntity);
    }

    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
