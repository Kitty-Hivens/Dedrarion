package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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
        System.out.println("Item set: " + item); // Отладочное сообщение
        setChanged();
    }

    public ItemStack removeItem() {
        ItemStack item = storedItem;
        storedItem = ItemStack.EMPTY;
        setChanged();
        return item;
    }

    // ✅ Сохранение данных в NBT (для перезахода)
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("StoredItem", storedItem.save(new CompoundTag())); // Сохраняем предмет
    }

    // ✅ Загрузка данных из NBT
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        storedItem = ItemStack.of(tag.getCompound("StoredItem")); // Восстанавливаем предмет
    }
}
