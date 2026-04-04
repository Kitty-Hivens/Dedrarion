package dedrarion.content.block.entity;

import dedrarion.api.block.entity.BaseContainerBlockEntity;
import dedrarion.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block entity for the Pedestal — holds a single item for display.
 */
public class PedestalBlockEntity extends BaseContainerBlockEntity {

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PEDESTAL_ENTITY.get(), pos, state);
    }

    @Override
    protected int getInventorySize() {
        return 1;
    }

    public boolean hasItem() {
        return !itemHandler.getStackInSlot(0).isEmpty();
    }

    public ItemStack getItem() {
        return itemHandler.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        itemHandler.setStackInSlot(0, stack);
        sync();
    }

    public ItemStack removeItem() {
        ItemStack stored = itemHandler.getStackInSlot(0).copy();
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        sync();
        return stored;
    }
}
