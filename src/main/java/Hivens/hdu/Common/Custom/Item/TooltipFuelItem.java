package Hivens.hdu.Common.Custom.Item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipFuelItem extends FuelItem {
    private final String tooltipKey;

    public TooltipFuelItem(Item.Properties properties, int burnTime, String tooltipKey) {
        super(properties, burnTime);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable(this.tooltipKey));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}

