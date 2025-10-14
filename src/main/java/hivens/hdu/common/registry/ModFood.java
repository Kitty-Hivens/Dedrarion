package hivens.hdu.common.registry;

import net.minecraft.world.food.FoodProperties;

public class ModFood {
    public static final FoodProperties FORBIDDEN_FRUIT = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(2F)
            .build();
}
