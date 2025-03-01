package Hivens.hdu.Common.Registry;

import Hivens.hdu.Common.recipe.CustomRecipe;
import Hivens.hdu.HDU;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, HDU.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HDU.MODID);

    public static final RegistryObject<RecipeType<CustomRecipe>> CUSTOM_RECIPE_TYPE =
            RECIPE_TYPES.register("custom_recipe", () -> new RecipeType<>() {});

    public static final RegistryObject<RecipeSerializer<CustomRecipe>> CUSTOM_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("custom_recipe", CustomRecipe.Serializer::new);
}
