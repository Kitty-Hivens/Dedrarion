package hivens.hdu.common.datagen;

import hivens.hdu.HDU;
import hivens.hdu.common.datagen.builder.EftoritForgeRecipeBuilder;
import hivens.hdu.common.recipe.EftoritIngredient;
import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import software.bernie.example.registry.ItemRegistry;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder {
    public static final List<ItemLike> ETHEREUM_SMELTABLES = List.of(
            ModItems.RAW_ETHEREUM.get(),
            ModBlocks.ETHEREUM_ORE.get()
    );

    public static final List<ItemLike> RUBY_SMELTABLES = List.of(
            ModBlocks.RUBY_ORE.get(),
            ModBlocks.DEEPSLATE_RUBY_ORE.get()
    );

    public static final List<ItemLike> EFTORIT_SMELTABLES = List.of(
            ModBlocks.EFTORIT_ORE.get(),
            ModBlocks.DEEPSLATE_EFTORIT_ORE.get()
    );



    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, ETHEREUM_SMELTABLES, RecipeCategory.MISC, ModItems.ETHEREUM.get(), 17f, 800, "ethereum");
        oreBlasting(pWriter, ETHEREUM_SMELTABLES, RecipeCategory.MISC, ModItems.ETHEREUM.get(), 23f, 600, "ethereum");

        oreSmelting(pWriter, RUBY_SMELTABLES, RecipeCategory.MISC, ModItems.RUBY.get(), 17f, 200, "ruby");
        oreBlasting(pWriter, RUBY_SMELTABLES, RecipeCategory.MISC, ModItems.RUBY.get(), 23f, 160, "ruby");

        oreSmelting(pWriter, EFTORIT_SMELTABLES, RecipeCategory.MISC, ModItems.EFTORIT.get(), 20f, 200, "eftorit");
        oreBlasting(pWriter, EFTORIT_SMELTABLES, RecipeCategory.MISC, ModItems.EFTORIT.get(), 28f, 160, "eftorit");



        // ===============================================================
        //
        //                        Shaped Craft
        //
        // ===============================================================

        // Ethereum

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ETHEREUM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.ETHEREUM.get())
                .unlockedBy(getHasName(ModItems.ETHEREUM.get()), has(ModItems.ETHEREUM.get()))
                .save(pWriter);

        // Ruby

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RUBY_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.RUBY.get())
                .unlockedBy(getHasName(ModItems.RUBY.get()), has(ModItems.RUBY.get()))
                .save(pWriter);

        // Eftorit

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EFTORIT_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.EFTORIT.get())
                .unlockedBy(getHasName(ModItems.EFTORIT.get()), has(ModItems.EFTORIT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EFTORIT_FORGE.get())
                .pattern("AAA")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', ModBlocks.EFTORIT_BLOCK.get())
                .define('B', ModItems.ETHEREUM.get())
                .define('C', ModBlocks.HOPE_STONE.get())
                .unlockedBy(getHasName(ModItems.EFTORIT.get()), has(ModItems.EFTORIT.get()))
                .unlockedBy(getHasName(ModItems.ETHEREUM.get()), has(ModItems.ETHEREUM.get()))
                .unlockedBy(getHasName(ModBlocks.HOPE_STONE.get()), has(ModBlocks.HOPE_STONE.get()))
                .save(pWriter);

        // Hope Stone

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPE_STONE_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', ModBlocks.HOPE_STONE.get())
                .unlockedBy(getHasName(ModBlocks.HOPE_STONE.get()), has(ModBlocks.HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPE_STONE_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.HOPE_STONE.get())
                .unlockedBy(getHasName(ModBlocks.HOPE_STONE.get()), has(ModBlocks.HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPE_BRICK_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', ModBlocks.HOPE_STONE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.HOPE_STONE_BRICKS.get()), has(ModBlocks.HOPE_STONE_BRICKS.get()))
                .save(pWriter);

// Smooth Hope Stone

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SMOOTH_HOPE_STONE_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', ModBlocks.SMOOTH_HOPE_STONE.get())
                .unlockedBy(getHasName(ModBlocks.SMOOTH_HOPE_STONE.get()), has(ModBlocks.SMOOTH_HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.SMOOTH_HOPE_STONE.get())
                .unlockedBy(getHasName(ModBlocks.SMOOTH_HOPE_STONE.get()), has(ModBlocks.SMOOTH_HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get()), has(ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get()))
                .save(pWriter);

// Hope Shards

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPE_SHARD_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', ModBlocks.HOPE_SHARDS.get())
                .unlockedBy(getHasName(ModBlocks.HOPE_SHARDS.get()), has(ModBlocks.HOPE_SHARDS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPE_SHARD_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.HOPE_SHARDS.get())
                .unlockedBy(getHasName(ModBlocks.HOPE_SHARDS.get()), has(ModBlocks.HOPE_SHARDS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.HOPE_SHARD_BRICK_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', ModBlocks.HOPE_SHARD_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.HOPE_SHARD_BRICKS.get()), has(ModBlocks.HOPE_SHARD_BRICKS.get()))
                .save(pWriter);




        // ===============================================================
        //
        //                      Shapeless Craft
        //
        // ===============================================================


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ETHEREUM.get(), 9)
                .requires(ModBlocks.ETHEREUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.ETHEREUM_BLOCK.get()), has(ModBlocks.ETHEREUM_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RUBY.get(), 9)
                .requires(ModBlocks.RUBY_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RUBY_BLOCK.get()), has(ModBlocks.RUBY_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EFTORIT.get(), 9)
                .requires(ModBlocks.EFTORIT_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.EFTORIT_BLOCK.get()), has(ModBlocks.EFTORIT_BLOCK.get()))
                .save(pWriter);


        // ===============================================================
        //
        //                      Stonecutter
        //
        // ===============================================================


        stonecutterResultFromBase(
                pWriter,
                RecipeCategory.MISC,
                ModItems.ETHEREUM_DUST.get(),
                ModItems.ETHEREUM.get()
        );


        // ===============================================================
        //
        //                    Smelting From Base
        //
        // ===============================================================

        smeltingResultFromBase(
                pWriter,
                ModBlocks.HOPE_STONE.get(),
                ModBlocks.HOPE_SHARDS.get()
        );

        smeltingResultFromBase(
                pWriter,
                ModBlocks.SMOOTH_HOPE_STONE.get(),
                ModBlocks.HOPE_STONE.get()
        );


        // ===============================================================
        //
        //                      Eftorit Forge
        //
        // ===============================================================

        // Добавляйте сюда другие рецепты по тому же принципу!
        // Например, рецепт починки инструмента, где инструмент не потребляется
        eftoritForgeRecipe(pWriter,
                "repair_diamond_pickaxe_with_nbt",
                net.minecraft.world.item.Items.DIAMOND_PICKAXE,
                List.of(
                        new EftoritIngredient(Ingredient.of(net.minecraft.world.item.Items.DIAMOND), true),
                        new EftoritIngredient(Ingredient.of(ModItems.EFTORIT.get()), true)
                ),
                Ingredient.of(net.minecraft.world.item.Items.DIAMOND_PICKAXE),
                net.minecraft.world.item.Items.DIAMOND_PICKAXE
        );

        eftoritForgeRecipe(pWriter,
                "mnemosyne_and_aleta_from_forge",      // Имя файла рецепта
                ModItems.MNEMOSYNE_ALETA.get(),      // Ваш меч в качестве результата
                List.of(
                        // Все ингредиенты будут потрачены
                        new EftoritIngredient(Ingredient.of(ModItems.ETHEREUM.get()), true),
                        new EftoritIngredient(Ingredient.of(Items.NETHERITE_INGOT), true),
                        new EftoritIngredient(Ingredient.of(Items.SPIDER_EYE), true),
                        new EftoritIngredient(Ingredient.of(Items.GLASS), true),
                        new EftoritIngredient(Ingredient.of(Items.STRING), true),
                        new EftoritIngredient(Ingredient.of(Items.IRON_SWORD), true)
                ),
                ModItems.ETHEREUM.get() // Рецепт откроется, когда игрок подберет Этериум
        );

    }


    protected static void stonecutterResultFromBase(
            @NotNull Consumer<FinishedRecipe> consumer,
            @NotNull RecipeCategory category,
            ItemLike result,
            ItemLike input
    ) {
        stonecutterResultFromBase(consumer, category, result, input, 1);
    }

    protected static void stonecutterResultFromBase(
            @NotNull Consumer<FinishedRecipe> consumer,
            @NotNull RecipeCategory category,
            ItemLike result,
            ItemLike input,
            int count
    ) {
        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(input),
                        category,
                        result,
                        count
                )
                .unlockedBy(getHasName(input), has(input))
                .save(consumer, getItemName(result) + "_from_stonecutting_" + getItemName(input));
    }

    /**
     * Создает рецепты плавки (furnace smelting) для списка предметов.
     */
    protected static void oreSmelting(
            @NotNull Consumer<FinishedRecipe> consumer,
            List<ItemLike> inputs,
            @NotNull RecipeCategory category,
            @NotNull ItemLike result,
            float experience,
            int cookTime,
            @NotNull String group
    ) {
        oreCooking(consumer, RecipeSerializer.SMELTING_RECIPE, inputs, category, result, experience, cookTime, group, "_from_smelting");
    }

    /**
     * Создает рецепты плавки в плавильне (blasting furnace).
     */
    protected static void oreBlasting(
            @NotNull Consumer<FinishedRecipe> consumer,
            List<ItemLike> inputs,
            @NotNull RecipeCategory category,
            @NotNull ItemLike result,
            float experience,
            int cookTime,
            @NotNull String group
    ) {
        oreCooking(consumer, RecipeSerializer.BLASTING_RECIPE, inputs, category, result, experience, cookTime, group, "_from_blasting");
    }

    /**
     * Универсальный метод для oreSmelting и oreBlasting.
     */
    protected static void oreCooking(
            @NotNull Consumer<FinishedRecipe> consumer,
            @NotNull RecipeSerializer<? extends AbstractCookingRecipe> serializer,
            List<ItemLike> inputs,
            @NotNull RecipeCategory category,
            @NotNull ItemLike result,
            float experience,
            int cookTime,
            @NotNull String group,
            String suffix
    ) {
        for (ItemLike input : inputs) {
            SimpleCookingRecipeBuilder.generic(
                            Ingredient.of(input),
                            category,
                            result,
                            experience,
                            cookTime,
                            serializer
                    )
                    .group(group)
                    .unlockedBy(getHasName(input), has(input))
                    .save(consumer, getItemName(result) + suffix + "_" + getItemName(input));
        }
    }

    /**
     * Простая версия smelting для одного предмета.
     */
    protected static void smeltingResultFromBase (
            @NotNull Consumer<FinishedRecipe> consumer,
            ItemLike result,
            ItemLike input
    ) {
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(input),
                        RecipeCategory.BUILDING_BLOCKS,
                        result,
                        0.1F,
                        200
                )
                .unlockedBy(getHasName(input), has(input))
                .save(consumer);
    }


    /**
     * Создает рецепт для Эфторитовой кузни.
     * @param consumer Потребитель готовых рецептов.
     * @param recipeName Уникальное имя файла для рецепта (например, "super_sword_from_forge").
     * @param result Предмет-результат.
     * @param ingredients Список ингредиентов. Используйте new EftoritIngredient(..., true/false).
     * @param unlockItem Предмет, наличие которого в инвентаре разблокирует рецепт.
     */
    protected void eftoritForgeRecipe(
            @NotNull Consumer<FinishedRecipe> consumer,
            @NotNull String recipeName,
            @NotNull ItemLike result,
            @NotNull List<EftoritIngredient> ingredients,
            @NotNull Ingredient nbtSourceIngredient, // <-- Новый параметр
            @NotNull ItemLike unlockItem
    ) {
        var builder = EftoritForgeRecipeBuilder.create(result);

        for (EftoritIngredient ingredient : ingredients) {
            builder.addIngredient(ingredient.ingredient(), ingredient.consume());
        }

        // Добавляем наш NBT-источник
        builder.addNbtSourceIngredient(nbtSourceIngredient);

        builder.unlockedBy(getHasName(unlockItem), has(unlockItem));
        builder.save(consumer, ResourceLocation.fromNamespaceAndPath("hdu", recipeName));
    }

    /**
     * Простая версия для создания НОВЫХ предметов в Эфторитовой кузне.
     */
    protected void eftoritForgeRecipe(
            @NotNull Consumer<FinishedRecipe> consumer,
            @NotNull String recipeName,
            @NotNull ItemLike result,
            @NotNull List<EftoritIngredient> ingredients,
            @NotNull ItemLike unlockItem
    ) {
        var builder = EftoritForgeRecipeBuilder.create(result);

        for (EftoritIngredient ingredient : ingredients) {
            builder.addIngredient(ingredient.ingredient(), ingredient.consume());
        }

        builder.unlockedBy(getHasName(unlockItem), has(unlockItem));
        builder.save(consumer, ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, recipeName));
    }



}
