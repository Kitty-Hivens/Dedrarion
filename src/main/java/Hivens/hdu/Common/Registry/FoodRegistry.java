package Hivens.hdu.Common.Registry;

import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties FORBIDDEN_FRUIT = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(2F)
            .build();
}
