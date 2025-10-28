package hivens.hdu.common.registry;

import hivens.hdu.common.recipe.EftoritForgeRecipe;
import hivens.hdu.HDU;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, HDU.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HDU.MOD_ID);



    public static final RegistryObject<RecipeType<EftoritForgeRecipe>> EFTORIT_FORGE_RECIPE_TYPE =
            RECIPE_TYPES.register("eftorit_forge", () -> new RecipeType<>() {});

    public static final RegistryObject<RecipeSerializer<EftoritForgeRecipe>> EFTORIT_FORGE_SERIALIZER =
            RECIPE_SERIALIZERS.register("eftorit_forge", EftoritForgeRecipe.Serializer::new);
}
