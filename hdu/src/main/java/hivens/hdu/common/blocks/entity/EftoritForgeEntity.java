package hivens.hdu.common.blocks.entity;

import hivens.hdu.common.registry.ModBlockEntities;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ItemParticleOption;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EftoritForgeEntity extends BlockEntity {
    private static final int INVENTORY_SIZE = 16;
    private static final int OUTPUT_SLOT = 15;

    private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            sync();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot != OUTPUT_SLOT && super.isItemValid(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    private EftoritForgeRecipe currentRecipe;
    private int craftTimer = 0;
    private boolean isCrafting = false;
    private ItemStack resultStack = ItemStack.EMPTY;

    public EftoritForgeEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EFTORIT_FORGE_ENTITY.get(), pos, state);
    }

    public Container getRecipeMatcher() {
        SimpleContainer container = new SimpleContainer(INVENTORY_SIZE);
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }
        return container;
    }

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

    public static void tick(Level level, EftoritForgeEntity entity) {
        if (level == null || level.isClientSide) return;

        if (entity.isCrafting) {
            entity.craftTimer++;

            // Спавним частицы на серверной стороне
            if (entity.craftTimer < 100 && level.getGameTime() % 2 == 0) {
                entity.spawnCraftingParticles();
            }

            if (entity.craftTimer >= 100) {
                entity.craftItem();
            }
        } else {
            entity.checkForRecipe();
        }
    }

    // Спавнит частицы ингредиентов и вихревые частицы
    public void spawnCraftingParticles() {
        if (level == null || level.isClientSide || currentRecipe == null || !(level instanceof ServerLevel serverLevel)) return;

        currentRecipe.getEftoritIngredients().forEach(ingredient -> {
            ItemStack[] items = ingredient.ingredient().getItems();
            if (items.length == 0) return;
            ItemStack itemStack = items[0];

            // Координаты центра
            double x = worldPosition.getX() + 0.5;
            double y = worldPosition.getY() + 0.9;
            double z = worldPosition.getZ() + 0.5;

            // Случайная позиция для вихря
            double radius = 0.5;
            double angle = level.random.nextDouble() * 2 * Math.PI;
            double xOffset = Math.cos(angle) * radius * 0.5;
            double zOffset = Math.sin(angle) * radius * 0.5;

            // 1. Частицы самого предмета
            serverLevel.sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, itemStack),
                    x + xOffset,
                    y,
                    z + zOffset,
                    1,
                    0.0, 0.0, 0.0, 0.0
            );

            // 2. Магические частицы (визуальный вихрь)
            serverLevel.sendParticles(
                    ParticleTypes.GLOW,
                    x + xOffset,
                    y,
                    z + zOffset,
                    1,
                    0.0,
                    0.1,
                    0.0,
                    0.0
            );
        });
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
            // Рассчитываем и сохраняем результат для рендера
            resultStack = currentRecipe.assemble(getRecipeMatcher(), level.registryAccess());
            isCrafting = true;
            craftTimer = 0;
            setChanged();
            sync();
        }
    }

    private void craftItem() {
        if (currentRecipe == null || level == null) return;

        processRecipe(currentRecipe);

        ItemStack result = currentRecipe.assemble(getRecipeMatcher(), level.registryAccess());
        if (result.isEmpty()) return;

        boolean placed = false;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                itemHandler.setStackInSlot(i, result.copy());
                placed = true;
                break;
            }
        }

        if (!placed) {
            spawnResult(result.copy());
        }

        // Очищаем результат после завершения крафта
        resultStack = ItemStack.EMPTY;

        currentRecipe = null;
        isCrafting = false;
        craftTimer = 0;
    }


    public void processRecipe(EftoritForgeRecipe recipe) {
        for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (ingredient.ingredient().test(stack)) {
                    if (ingredient.consume()) {
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
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("CraftTimer", craftTimer);
        tag.putBoolean("IsCrafting", isCrafting);
        tag.put("ResultStack", resultStack.save(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        this.craftTimer = tag.getInt("CraftTimer");
        this.isCrafting = tag.getBoolean("IsCrafting");
        if (tag.contains("ResultStack")) {
            resultStack = ItemStack.of(tag.getCompound("ResultStack"));
        }
    }

    // --- ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ (ГЕТТЕРЫ) ---
    public boolean isCrafting() {
        return isCrafting;
    }

    public int getCraftTimer() {
        return craftTimer;
    }

    public ItemStack getResultStack() {
        return resultStack;
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