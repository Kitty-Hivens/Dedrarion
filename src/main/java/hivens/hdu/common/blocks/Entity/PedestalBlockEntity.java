package hivens.hdu.common.blocks.Entity;

import hivens.hdu.common.registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public class PedestalBlockEntity extends BlockEntity {
    private ItemStack storedItem = ItemStack.EMPTY;

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.PEDESTAL_ENTITY.get(), pos, state);
    }

    public boolean hasItem() {
        return !storedItem.isEmpty();
    }

    public ItemStack getItem() {
        return storedItem;
    }

    public void setItem(ItemStack item) {
        this.storedItem = item;
        setChanged();
        assert level != null;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }


    public ItemStack removeItem() {
        ItemStack item = storedItem;
        storedItem = ItemStack.EMPTY;
        setChanged();
        assert level != null;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        return item;
    }


    // *** Сохранение в NBT ***
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag itemTag = new CompoundTag();
        storedItem.save(itemTag);
        tag.put("StoredItem", itemTag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("StoredItem")) {
            storedItem = ItemStack.of(tag.getCompound("StoredItem"));
        }
    }

    // *** Синхронизация с клиентом ***
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}
