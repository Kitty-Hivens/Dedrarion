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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
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


    /**
     * Константа. Хранит информацию о предметах, которые могут быть починены через эфторитовую наковальню
     */
    public static final List<ItemLike> TOOLS_TO_REPAIR = List.of(
            // Алмазные инструменты
            Items.DIAMOND_PICKAXE,
            Items.DIAMOND_AXE,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_SWORD,
            Items.DIAMOND_HOE,
            // Незеритовые инструменты
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_AXE,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_HOE,
            // Алмазная броня
            Items.DIAMOND_HELMET,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_BOOTS,
            // Незеритовая броня
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS,
            // Вещи с Dedrarion
            ModItems.MAGIC_DETECTOR.get(),
            ModItems.MNEMOSYNE_ALETA.get(),
            ModItems.TETRALIN.get(),
            ModItems.DETONATION_BLADE.get()
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
                .pattern(" A ")
                .pattern("CBC")
                .pattern("DDD")
                .define('A', ModItems.ETHEREUM.get())
                .define('B', ModItems.FOSSIL.get())
                .define('C', ModBlocks.EFTORIUM_BLOCK.get())
                .define('D', ModBlocks.HOPE_STONE.get())
                .unlockedBy(getHasName(ModItems.FOSSIL.get()), has(ModItems.FOSSIL.get()))
                .unlockedBy(getHasName(ModItems.ETHEREUM.get()), has(ModItems.ETHEREUM.get()))
                .unlockedBy(getHasName(ModBlocks.EFTORIUM_BLOCK.get()), has(ModBlocks.EFTORIUM_BLOCK.get()))
                .save(pWriter);

        // Eftorium
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.EFTORIUM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.EFTORIUM_INGOT.get())
                .unlockedBy(getHasName(ModItems.EFTORIUM_INGOT.get()), has(ModItems.EFTORIUM_INGOT.get()))
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

        // NullGuardian Items
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.CONCUSSIVE_DYNAMITE.get())
                .pattern("SPS")
                .pattern("PGP")
                .pattern("SPS")
                .define('S', Items.SAND)
                .define('P', ModItems.UNSTABLE_GUNPOWDER.get())
                .define('G', Items.GUNPOWDER)
                .unlockedBy(getHasName(ModItems.UNSTABLE_GUNPOWDER.get()), has(ModItems.UNSTABLE_GUNPOWDER.get()))
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


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.JOKERS_COMPANION.get())
                .requires(ModItems.ETHER_CORE.get())
                .requires(ModItems.UNSTABLE_GUNPOWDER.get())
                .requires(ModItems.BROKEN_CARBON_PLATES.get())
                .requires(ModItems.BROKEN_CARBON_PLATES.get())
                .unlockedBy(getHasName(ModItems.ETHER_CORE.get()), has(ModItems.ETHER_CORE.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.EFTORIUM_DUST.get(), 9)
                .requires(ModItems.EFTORIT_DUST.get())
                .requires(ModItems.EFTORIT_DUST.get())
                .requires(ModItems.EFTORIT_DUST.get())
                .requires(ModItems.ETHEREUM_DUST.get())
                .requires(ModItems.ETHEREUM_DUST.get())
                .requires(ModItems.IRON_DUST.get())
                .requires(ModItems.IRON_DUST.get())
                .requires(ModItems.IRON_DUST.get())
                .requires(ModItems.IRON_DUST.get())
                .unlockedBy(getHasName(ModItems.ETHEREUM_DUST.get()), has(ModItems.ETHEREUM_DUST.get()))
                .save(pWriter); // 4 железных слитка, 2/3 эфириума, 3 эфторита...



        // ===============================================================
        //
        //                      Stonecutter
        //
        // ===============================================================


        stonecutterResultFromBase(
                pWriter,
                RecipeCategory.MISC,
                ModItems.ETHEREUM_DUST.get(),
                ModItems.ETHEREUM.get(),
                3
        );

        stonecutterResultFromBase(
                pWriter,
                RecipeCategory.MISC,
                ModItems.IRON_DUST.get(),
                Items.IRON_INGOT
        );

        stonecutterResultFromBase(
                pWriter,
                RecipeCategory.MISC,
                ModItems.EFTORIT_DUST.get(),
                ModItems.EFTORIT.get()
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


        autoGenerateForgeRepair(pWriter);

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
                // TODO: Переделать. Эфириум и так будет доступен к моменту крафта
        );

        eftoritForgeRecipe(pWriter,
                "tetralin_from_forge",
                ModItems.TETRALIN.get(),
                List.of(
                        new EftoritIngredient(Ingredient.of(Items.NETHERITE_INGOT), true),
                        new EftoritIngredient(Ingredient.of(ModItems.ETHEREUM.get()), true), // Заменил Shards на слиток, так логичнее
                        new EftoritIngredient(Ingredient.of(ModItems.ETHEREUM.get()), true),
                        new EftoritIngredient(Ingredient.of(Items.PHANTOM_MEMBRANE), true),
                        new EftoritIngredient(Ingredient.of(Items.HEART_OF_THE_SEA), false), // Сердце моря - катализатор!
                        new EftoritIngredient(Ingredient.of(Items.DIAMOND), true),
                        new EftoritIngredient(Ingredient.of(Items.DIAMOND), true)
                ),
                Items.HEART_OF_THE_SEA // Разблокируется, когда у игрока есть сердце моря
        );



        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), // Шаблон улучшения
                        Ingredient.of(Items.NETHERITE_SWORD),                      // Что улучшаем
                        Ingredient.of(ModItems.ETHER_CORE.get()),                  // Чем улучшаем
                        RecipeCategory.COMBAT,
                        ModItems.DETONATION_BLADE.get()                            // Результат
                ).unlocks(getHasName(ModItems.ETHER_CORE.get()), has(ModItems.ETHER_CORE.get()))
                .save(pWriter, "detonation_blade_from_smithing");
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
     * Простая версия для создания НОВЫХ предметов в Эфторитовой кузне.
     */
    protected static void eftoritForgeRecipe(
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

    /**
     * Автоматически генерирует рецепты ремонта для Эфторитовой кузни
     * для списка предметов. Рецепт: 1-2 Эфторита + Предмет с NBT = Починенный предмет с NBT.
     */
    protected static void autoGenerateForgeRepair(@NotNull Consumer<FinishedRecipe> consumer) {
        for (ItemLike repairItem : ModRecipeProvider.TOOLS_TO_REPAIR) {
            String itemName = getItemName(repairItem);
            String itemPath = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(repairItem.asItem())).getPath();

            int eftoritCount = getEftoritCount(itemPath);

            var builder = EftoritForgeRecipeBuilder.create(repairItem);

            // Добавляем Эфторит в нужном количестве
            for (int i = 0; i < eftoritCount; i++) {
                builder.addIngredient(ModItems.EFTORIT.get(), true);
            }

            // Ключевой шаг: Указываем, что чинимый предмет является источником NBT
            // Ингредиент не потребляется, но его NBT копируются, а прочность чинится
            builder.addNbtSourceIngredient(Ingredient.of(repairItem));

            builder.unlockedBy("has_" + itemName, has(repairItem));

            // Используем специальное имя для рецепта, чтобы было понятно, что это ремонт
            builder.save(consumer, ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "repair_" + itemName + "_with_eftorit"));
        }
    }

    private static int getEftoritCount(String itemPath) {
        int eftoritCount;

        // 1. Проверка на АРТЕФАКТЫ.
        // Используем уникальные имена Ваших артефактов (Мнемозина/Алета, Тетралин)
        if (itemPath.contains("mnemosyne_aleta") || itemPath.contains("tetralin") ||
            itemPath.contains("detonation_blade")|| itemPath.contains("magic_detector")) {
            eftoritCount = 3; // Самая высокая цена для ARTIFACT tier (Уровень 5)
        }
        // 2. Проверка на НЕЗЕРИТ
        else if (itemPath.contains("netherite")) {
            eftoritCount = 2; // Стандартная высокая цена (Уровень 4)
        }
        // 3. Остальные (Алмаз, Железо и т.д.)
        else {
            eftoritCount = 1; // Базовая цена
        }
        return eftoritCount;
    }



}
