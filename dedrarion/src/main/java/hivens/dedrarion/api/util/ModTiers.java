package hivens.dedrarion.api.util;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier ARTIFACT = new ForgeTier(
            5,                      // Level
            2500,                   // Durability
            10.0f,                  // Speed
            5.0f,                   // Damage
            20,                     // Enchantability
            DedrarionTags.Blocks.NEEDS_ARTIFACT_TOOL,
            Ingredient::of
    );
}