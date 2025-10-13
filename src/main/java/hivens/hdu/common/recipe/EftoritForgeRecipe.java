package hivens.hdu.common.recipe;

import hivens.hdu.common.registry.ModRecipes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record EftoritForgeRecipe(ResourceLocation id, NonNullList<EftoritIngredient> ingredients,
                                 ItemStack output) implements Recipe<Container> {

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {

        List<ItemStack> inventoryItems = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            inventoryItems.add(container.getItem(i));
        }

        List<EftoritIngredient> requiredIngredients = new ArrayList<>(ingredients);


        for (ItemStack stack : inventoryItems) {
            requiredIngredients.removeIf(ingredient -> ingredient.ingredient().test(stack));
        }

        return requiredIngredients.isEmpty();
    }


    @Override
    public @NotNull ItemStack assemble(@NotNull Container p_44001_, @NotNull RegistryAccess p_267165_) {
        return output.copy();
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.EFTORIT_FORGE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<EftoritForgeRecipe> {
        @Override
        public @NotNull EftoritForgeRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            NonNullList<EftoritIngredient> ingredients = NonNullList.create();
            for (JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients")) {
                ingredients.add(EftoritIngredient.fromJson(element.getAsJsonObject())); // Используем метод
            }
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new EftoritForgeRecipe(id, ingredients, output);
        }


        @Override
        public EftoritForgeRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buffer) {
            NonNullList<EftoritIngredient> ingredients = NonNullList.create();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                ingredients.add(EftoritIngredient.fromNetwork(buffer)); // Добавляем вызов
            }
            ItemStack output = buffer.readItem();
            return new EftoritForgeRecipe(id, ingredients, output);
        }


        @Override
        public void toNetwork(FriendlyByteBuf buffer, EftoritForgeRecipe recipe) {
            buffer.writeVarInt(recipe.getEftoritIngredients().size());
            for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
                ingredient.toNetwork(buffer); // Добавляем этот вызов
            }
            buffer.writeItem(recipe.getResultItem(null));
        }


    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> vanillaList = NonNullList.create();
        for (EftoritIngredient eftoritIngredient : ingredients) {
            vanillaList.add(eftoritIngredient.ingredient()); // Возвращает Ingredient
        }
        return vanillaList;
    }

    // Новая версия для processRecipe (как EftoritIngredient)
    public NonNullList<EftoritIngredient> getEftoritIngredients() {
        return ingredients;
    }
}
