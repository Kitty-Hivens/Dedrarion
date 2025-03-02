package Hivens.hdu.Common.recipe;

import Hivens.hdu.Common.Registry.ModRecipes;
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

import java.util.ArrayList;
import java.util.List;

public class EftoritForgeRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;

    public EftoritForgeRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    public boolean matches(Container container, Level level) {
        NonNullList<ItemStack> items = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            items.add(container.getItem(i));
        }
        return matchesIngredients(items);
    }


    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
        return output.copy();
    }

    private boolean matchesIngredients(NonNullList<ItemStack> items) {
        List<ItemStack> input = new ArrayList<>(items);
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (ItemStack stack : input) {
                if (ingredient.test(stack)) {
                    input.remove(stack);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return input.isEmpty();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EFTORIT_FORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<EftoritForgeRecipe> {
        @Override
        public EftoritForgeRecipe fromJson(ResourceLocation id, JsonObject json) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients")) {
                ingredients.add(Ingredient.fromJson(element));
            }
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new EftoritForgeRecipe(id, ingredients, output);
        }

        @Override
        public EftoritForgeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                ingredients.add(Ingredient.fromNetwork(buffer));
            }
            ItemStack output = buffer.readItem();
            return new EftoritForgeRecipe(id, ingredients, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EftoritForgeRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.output);
        }
    }
}
