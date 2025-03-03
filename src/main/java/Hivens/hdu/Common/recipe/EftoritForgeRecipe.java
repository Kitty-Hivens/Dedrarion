package Hivens.hdu.Common.recipe;

import Hivens.hdu.Common.Registry.ModRecipes;
import Hivens.hdu.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
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
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EftoritForgeRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final NonNullList<EftoritIngredient> ingredients;
    private final ItemStack output;

    public static final Logger LOGGER = LogUtils.getLogger();

    public EftoritForgeRecipe(ResourceLocation id, NonNullList<EftoritIngredient> ingredients, ItemStack output) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {

        if (Config.isDevMode())
            LOGGER.debug("Проверка рецепта {}...", this.getId());

        List<ItemStack> inventoryItems = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            inventoryItems.add(container.getItem(i));
        }

        List<EftoritIngredient> requiredIngredients = new ArrayList<>(ingredients);

        if (Config.isDevMode())
            LOGGER.debug("Требуемые ингредиенты: {}", requiredIngredients);

        for (ItemStack stack : inventoryItems) {
            if (Config.isDevMode())
                LOGGER.debug("Проверяем слот: {}", stack);
            requiredIngredients.removeIf(ingredient -> ingredient.ingredient().test(stack));
        }

        if (Config.isDevMode())
            if (requiredIngredients.isEmpty()) {
                LOGGER.info("Рецепт {} найден!", this.getId());
            } else {
                LOGGER.warn("Рецепт {} не совпадает. Остались неудалённые ингредиенты: {}", this.getId(), requiredIngredients);
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
    public @NotNull ItemStack getResultItem(RegistryAccess p_267052_) {
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
