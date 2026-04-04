package dedrarion.content.recipe;

import dedrarion.registry.ModRecipes;
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
import java.util.Optional;

public record EftoritForgeRecipe(ResourceLocation id, NonNullList<EftoritIngredient> ingredients,
                                 ItemStack output) implements Recipe<Container> {

    /**
     * Checks whether the container satisfies all required ingredients.
     * <p>
     * Each inventory slot is matched against the remaining unmatched ingredients
     * one-to-one — a single stack can only satisfy one ingredient entry,
     * even if multiple entries accept the same item type.
     */
    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        List<ItemStack> inventoryItems = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) inventoryItems.add(stack);
        }

        // Work on a mutable copy so we consume matched entries one-by-one.
        List<EftoritIngredient> remaining = new ArrayList<>(ingredients);

        for (ItemStack stack : inventoryItems) {
            for (int i = 0; i < remaining.size(); i++) {
                if (remaining.get(i).ingredient().test(stack)) {
                    remaining.remove(i);
                    break; // One stack satisfies at most one ingredient entry.
                }
            }
        }

        return remaining.isEmpty();
    }

    /**
     * Finds the ingredient marked as NBT source and returns the matching
     * stack from the container, if present.
     */
    private Optional<ItemStack> findNbtSourceIngredient(Container container) {
        Optional<EftoritIngredient> nbtIngredient = getEftoritIngredients().stream()
                .filter(EftoritIngredient::copyNbt)
                .findFirst();

        if (nbtIngredient.isPresent()) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (nbtIngredient.get().ingredient().test(stack)) {
                    return Optional.of(stack);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Assembles the result item, copying NBT tags from the NBT source ingredient
     * if one is present, and restoring durability to full.
     */
    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        final ItemStack result = getResultItem(registryAccess).copy();

        findNbtSourceIngredient(container).ifPresent(sourceStack -> {
            if (sourceStack.hasTag()) {
                result.setTag(sourceStack.getTag().copy());
            }
            result.setDamageValue(0);
        });

        return result;
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

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> vanillaList = NonNullList.create();
        for (EftoritIngredient eftoritIngredient : ingredients) {
            vanillaList.add(eftoritIngredient.ingredient());
        }
        return vanillaList;
    }

    public NonNullList<EftoritIngredient> getEftoritIngredients() {
        return ingredients;
    }

    // --- Serializer ---

    public static class Serializer implements RecipeSerializer<EftoritForgeRecipe> {

        @Override
        public @NotNull EftoritForgeRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            NonNullList<EftoritIngredient> ingredients = NonNullList.create();
            for (JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients")) {
                ingredients.add(EftoritIngredient.fromJson(element.getAsJsonObject()));
            }
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new EftoritForgeRecipe(id, ingredients, output);
        }

        @Override
        public EftoritForgeRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buffer) {
            NonNullList<EftoritIngredient> ingredients = NonNullList.create();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                ingredients.add(EftoritIngredient.fromNetwork(buffer));
            }
            return new EftoritForgeRecipe(id, ingredients, buffer.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EftoritForgeRecipe recipe) {
            buffer.writeVarInt(recipe.getEftoritIngredients().size());
            for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItem(recipe.getResultItem(null));
        }
    }
}
