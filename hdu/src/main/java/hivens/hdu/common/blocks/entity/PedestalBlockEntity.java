package hivens.hdu.common.blocks.entity;

import hivens.hdu.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PedestalBlockEntity extends BlockEntity {
    private ItemStack storedItem = ItemStack.EMPTY;

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PEDESTAL_ENTITY.get(), pos, state);
    }

    // --- Методы для доступа к инвентарю ---

    public boolean hasItem() {
        return !storedItem.isEmpty();
    }

    public ItemStack getItem() {
        return storedItem;
    }

    public void setItem(ItemStack item) {
        this.storedItem = item;
        setChanged(); // Помечаем для сохранения
        sync();       // Отправляем обновление клиентам
    }

    public ItemStack removeItem() {
        ItemStack item = this.storedItem;
        this.storedItem = ItemStack.EMPTY;
        setChanged();
        sync();
        return item;
    }

    // --- Централизованный метод синхронизации ---
    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // *** Сохранение и загрузка NBT ***
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        // Более короткий способ сохранить ItemStack
        tag.put("StoredItem", storedItem.save(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("StoredItem")) {
            storedItem = ItemStack.of(tag.getCompound("StoredItem"));
        }
    }

    // *** Стандартная реализация синхронизации с клиентом ***
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        // Этот метод автоматически вызывается getUpdatePacket()
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // Этот метод вызывается на клиенте при получении пакета
        if (pkt.getTag() != null) {
            handleUpdateTag(pkt.getTag());
        }
    }
}