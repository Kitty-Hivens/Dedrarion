package hivens.hdu.common.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hivens.hdu.common.recipe.EftoritIngredient;
import hivens.hdu.common.registry.ModRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class EftoritForgeRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final List<EftoritIngredient> ingredients = new ArrayList<>();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    private EftoritForgeRecipeBuilder(ItemLike result, int count) {
        this.result = new ItemStack(result.asItem(), count);
    }

    /**
     * Создает новый билдер для рецепта Эфторитовой кузни.
     */
    public static EftoritForgeRecipeBuilder create(ItemLike result) {
        return new EftoritForgeRecipeBuilder(result, 1);
    }

    public static EftoritForgeRecipeBuilder create(ItemLike result, int count) {
        return new EftoritForgeRecipeBuilder(result, count);
    }

    /**
     * Добавляет ингредиент, который будет потребляться в процессе крафта.
     */
    public EftoritForgeRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, true);
    }

    public EftoritForgeRecipeBuilder addIngredient(ItemLike item) {
        return this.addIngredient(Ingredient.of(item), true);
    }

    /**
     * Добавляет ингредиент с явным указанием, будет ли он потребляться.
     * @param consume true, если предмет исчезнет, false - если останется.
     */
    public EftoritForgeRecipeBuilder addIngredient(Ingredient ingredient, boolean consume) {
        this.ingredients.add(new EftoritIngredient(ingredient, consume));
        return this;
    }

    public EftoritForgeRecipeBuilder addIngredient(ItemLike item, boolean consume) {
        return this.addIngredient(Ingredient.of(item), consume);
    }

    /**
     * Добавляет ингредиент, с которого будут скопированы NBT-теги (например, зачарования).
     */
    public void addNbtSourceIngredient(Ingredient ingredient) {
        this.ingredients.add(new EftoritIngredient(ingredient, true, true));
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String name, @NotNull CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return this.result.getItem();
    }


    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
        // Убеждаемся, что критерий для разблокировки рецепта существует
        this.advancement.parent(ResourceLocation.fromNamespaceAndPath("minecraft", "recipes/root"))
                // ИЗМЕНЕНИЕ ЗДЕСЬ:
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(id));

        consumer.accept(new Result(id, this.result, this.ingredients, this.group, this.advancement,
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/" + id.getPath())));
    }

    // Внутренний класс, который реализует FinishedRecipe и отвечает за сериализацию
        public record Result(ResourceLocation id, ItemStack result, List<EftoritIngredient> ingredients, String group,
                             Advancement.Builder advancement, ResourceLocation advancementId) implements FinishedRecipe {

        @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                if (this.group != null && !this.group.isEmpty()) {
                    json.addProperty("group", this.group);
                }

                JsonArray ingredientsJson = new JsonArray();
                for (EftoritIngredient ing : this.ingredients) {
                    JsonObject ingredientObject = ing.ingredient().toJson().getAsJsonObject();
                    // Явно указываем флаги, только если они не равны значению по умолчанию
                    if (!ing.consume()) {
                        ingredientObject.addProperty("consume", false);
                    }
                    if (ing.copyNbt()) {
                        ingredientObject.addProperty("copyNbt", true);
                    }
                    ingredientsJson.add(ingredientObject);
                }
                json.add("ingredients", ingredientsJson);

                JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
                if (this.result.getCount() > 1) {
                    resultJson.addProperty("count", this.result.getCount());
                }
                json.add("result", resultJson);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return this.id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return ModRecipes.EFTORIT_FORGE_SERIALIZER.get();
            }

            @Override
            public @NotNull JsonObject serializeAdvancement() { // <-- ИСПРАВЛЕНО
                return this.advancement.serializeToJson();
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return this.advancementId;
            }
        }
}