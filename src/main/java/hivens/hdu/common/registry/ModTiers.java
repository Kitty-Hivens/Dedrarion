package hivens.hdu.common.registry;

import hivens.hdu.common.util.ModTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier ARTIFACT = new ForgeTier(
            5,                      // Уровень добычи (5 = выше незерита)
            2500,                   // Прочность (у незерита 2031)
            10.0f,                  // Скорость добычи (не так важно для меча)
            5.0f,                   // БАЗОВЫЙ УРОН МАТЕРИАЛА (у незерита 4.0)
            20,                     // Зачаровываемость (у незерита 15, у золота 22)
            ModTags.Blocks.NEEDS_ARTIFACT_TOOL, // Тег блоков, которые можно добывать
            () -> Ingredient.of(ModItems.ETHEREUM.get()) // Чем чинить на наковальне
    );
}