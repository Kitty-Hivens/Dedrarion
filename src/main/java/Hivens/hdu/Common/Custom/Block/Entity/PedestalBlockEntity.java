package Hivens.hdu.Common.Custom.Block.Entity;

import Hivens.hdu.Common.Registry.BlockEntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
    }

    public ItemStack removeItem() {
        ItemStack item = storedItem;
        storedItem = ItemStack.EMPTY;
        setChanged();
        return item;
    }


}
