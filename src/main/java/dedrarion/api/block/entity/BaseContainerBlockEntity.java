package dedrarion.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for block entities that hold an item inventory.
 * <p>
 * Handles boilerplate: capability exposure, NBT serialization,
 * client sync, inventory snapshot, and content dropping.
 * <p>
 * Subclasses override {@link #createHandler()} to configure slot count and rules.
 */
public abstract class BaseContainerBlockEntity extends BlockEntity {

    protected final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    protected BaseContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // --- Handler factory ---

    /**
     * Creates the item handler for this block entity.
     * Override to set slot count and custom rules (validity, slot limits, etc.).
     *
     * @return configured {@link ItemStackHandler}
     */
    @NotNull
    protected ItemStackHandler createHandler() {
        return new ItemStackHandler(getInventorySize()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    /**
     * Returns the number of inventory slots.
     * Used by the default {@link #createHandler()} implementation.
     */
    protected abstract int getInventorySize();

    // --- Capability ---

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    // --- Sync ---

    /**
     * Marks the block entity as changed and sends a block update to clients.
     * Call this when state visible to the client changes (start/stop crafting, item added/removed).
     * Do NOT call on every inventory tick — prefer {@link #setChanged()} for that.
     */
    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            setChanged();
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

    // --- NBT ---

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("inventory", itemHandler.serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
    }

    // --- Inventory helpers ---

    /**
     * Returns a {@link SimpleContainer} snapshot of the current inventory.
     * Used for recipe matching without exposing the live handler.
     */
    protected Container snapshot() {
        SimpleContainer container = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            container.setItem(i, itemHandler.getStackInSlot(i));
        }
        return container;
    }

    /**
     * Drops all non-empty inventory contents into the world and clears the inventory.
     */
    public void dropContents() {
        if (level == null) return;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                spawnItem(stack);
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    /**
     * Spawns an item entity above this block.
     *
     * @param stack stack to spawn
     */
    protected void spawnItem(ItemStack stack) {
        if (level == null) return;
        level.addFreshEntity(new ItemEntity(
            level,
            worldPosition.getX() + 0.5,
            worldPosition.getY() + 1.0,
            worldPosition.getZ() + 0.5,
            stack.copy()
        ));
    }
}
