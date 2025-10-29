package hivens.dedrarion.api.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Утилитный класс для регистрации предмета вместе с подсказкой
 */
public class TooltipItem extends Item {
    private final String tooltipKey;

    /**
     * Конструктор класса TooltipItem
     * @param properties Свойства
     * @param tooltipKey Подсказка (Ключ). Например: "tooltip.item.hdu.ethereum"
     */
    public TooltipItem(Properties properties, String tooltipKey) {
        super(properties);
        this.tooltipKey = tooltipKey;
    }

    /**
     * Метод для применения подсказки на предмете
     * @param stack
     * @param level
     * @param tooltipComponents
     * @param flag
     */
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag flag) {
        tooltipComponents.add(Component.translatable(this.tooltipKey));
        super.appendHoverText(stack, level, tooltipComponents, flag);
    }
}

