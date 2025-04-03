package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.Common.Registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider implements IConditionBuilder {
    public static final List<ItemLike> ETHEREUM_SMELTABLES = List.of(
            ItemRegistry.RAW_ETHEREUM.get(),
            BlockRegistry.ETHEREUM_ORE.get()
    );

    public static final List<ItemLike> RUBY_SMELTABLES = List.of(
            BlockRegistry.RUBY_ORE.get(),
            BlockRegistry.DEEPSLATE_RUBY_ORE.get()
    );

    public static final List<ItemLike> EFTORIT_SMELTABLES = List.of(
            BlockRegistry.EFTORIT_ORE.get(),
            BlockRegistry.DEEPSLATE_EFTORIT_ORE.get()
    );



    public RecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        oreSmelting(pWriter, ETHEREUM_SMELTABLES, RecipeCategory.MISC, ItemRegistry.ETHEREUM.get(), 17f, 800, "ethereum");
        oreBlasting(pWriter, ETHEREUM_SMELTABLES, RecipeCategory.MISC, ItemRegistry.ETHEREUM.get(), 23f, 600, "ethereum");

        oreSmelting(pWriter, RUBY_SMELTABLES, RecipeCategory.MISC, ItemRegistry.RUBY.get(), 17f, 200, "ruby");
        oreBlasting(pWriter, RUBY_SMELTABLES, RecipeCategory.MISC, ItemRegistry.RUBY.get(), 23f, 160, "ruby");

        oreSmelting(pWriter, EFTORIT_SMELTABLES, RecipeCategory.MISC, ItemRegistry.EFTORIT.get(), 20f, 200, "eftorit");
        oreBlasting(pWriter, EFTORIT_SMELTABLES, RecipeCategory.MISC, ItemRegistry.EFTORIT.get(), 28f, 160, "eftorit");



        // ===============================================================
        //
        //                        Shaped Craft
        //
        // ===============================================================

        // Ethereum

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.ETHEREUM_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ItemRegistry.ETHEREUM.get())
                .unlockedBy(getHasName(ItemRegistry.ETHEREUM.get()), has(ItemRegistry.ETHEREUM.get()))
                .save(pWriter);

        // Ruby

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.RUBY_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ItemRegistry.RUBY.get())
                .unlockedBy(getHasName(ItemRegistry.RUBY.get()), has(ItemRegistry.RUBY.get()))
                .save(pWriter);

        // Eftorit

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.EFTORIT_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ItemRegistry.EFTORIT.get())
                .unlockedBy(getHasName(ItemRegistry.EFTORIT.get()), has(ItemRegistry.EFTORIT.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.EFTORIT_FORGE.get())
                .pattern("AAA")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', BlockRegistry.EFTORIT_BLOCK.get())
                .define('B', ItemRegistry.ETHEREUM.get())
                .define('C', BlockRegistry.HOPE_STONE.get())
                .unlockedBy(getHasName(ItemRegistry.EFTORIT.get()), has(ItemRegistry.EFTORIT.get()))
                .unlockedBy(getHasName(ItemRegistry.ETHEREUM.get()), has(ItemRegistry.ETHEREUM.get()))
                .unlockedBy(getHasName(BlockRegistry.HOPE_STONE.get()), has(BlockRegistry.HOPE_STONE.get()))
                .save(pWriter);

        // Hope Stone

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HOPE_STONE_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', BlockRegistry.HOPE_STONE.get())
                .unlockedBy(getHasName(BlockRegistry.HOPE_STONE.get()), has(BlockRegistry.HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HOPE_STONE_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', BlockRegistry.HOPE_STONE.get())
                .unlockedBy(getHasName(BlockRegistry.HOPE_STONE.get()), has(BlockRegistry.HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HOPE_BRICK_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', BlockRegistry.HOPE_STONE_BRICKS.get())
                .unlockedBy(getHasName(BlockRegistry.HOPE_STONE_BRICKS.get()), has(BlockRegistry.HOPE_STONE_BRICKS.get()))
                .save(pWriter);

// Smooth Hope Stone

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_HOPE_STONE_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', BlockRegistry.SMOOTH_HOPE_STONE.get())
                .unlockedBy(getHasName(BlockRegistry.SMOOTH_HOPE_STONE.get()), has(BlockRegistry.SMOOTH_HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', BlockRegistry.SMOOTH_HOPE_STONE.get())
                .unlockedBy(getHasName(BlockRegistry.SMOOTH_HOPE_STONE.get()), has(BlockRegistry.SMOOTH_HOPE_STONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get())
                .unlockedBy(getHasName(BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get()), has(BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get()))
                .save(pWriter);

// Hope Shards

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HOPE_SHARD_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', BlockRegistry.HOPE_SHARDS.get())
                .unlockedBy(getHasName(BlockRegistry.HOPE_SHARDS.get()), has(BlockRegistry.HOPE_SHARDS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HOPE_SHARD_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', BlockRegistry.HOPE_SHARDS.get())
                .unlockedBy(getHasName(BlockRegistry.HOPE_SHARDS.get()), has(BlockRegistry.HOPE_SHARDS.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get(), 8)
                .pattern("  S")
                .pattern(" SS")
                .pattern("SSS")
                .define('S', BlockRegistry.HOPE_SHARD_BRICKS.get())
                .unlockedBy(getHasName(BlockRegistry.HOPE_SHARD_BRICKS.get()), has(BlockRegistry.HOPE_SHARD_BRICKS.get()))
                .save(pWriter);




        // ===============================================================
        //
        //                      Shapeless Craft
        //
        // ===============================================================


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.ETHEREUM.get(), 9)
                .requires(BlockRegistry.ETHEREUM_BLOCK.get())
                .unlockedBy(getHasName(BlockRegistry.ETHEREUM_BLOCK.get()), has(BlockRegistry.ETHEREUM_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.RUBY.get(), 9)
                .requires(BlockRegistry.RUBY_BLOCK.get())
                .unlockedBy(getHasName(BlockRegistry.RUBY_BLOCK.get()), has(BlockRegistry.RUBY_BLOCK.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegistry.EFTORIT.get(), 9)
                .requires(BlockRegistry.EFTORIT_BLOCK.get())
                .unlockedBy(getHasName(BlockRegistry.EFTORIT_BLOCK.get()), has(BlockRegistry.EFTORIT_BLOCK.get()))
                .save(pWriter);


        // Other


        stonecutterResultFromBase(
                pWriter,
                RecipeCategory.MISC,
                ItemRegistry.ETHEREUM_DUST.get(),
                ItemRegistry.ETHEREUM.get()
        );

        smeltingResultFromBase(
                pWriter,
                BlockRegistry.HOPE_STONE.get(),
                BlockRegistry.HOPE_SHARDS.get()
        );

        smeltingResultFromBase(
                pWriter,
                BlockRegistry.SMOOTH_HOPE_STONE.get(),
                BlockRegistry.HOPE_STONE.get()
        );
    }


    protected static void stonecutterResultFromBase(@NotNull Consumer<FinishedRecipe> p_251589_, @NotNull RecipeCategory p_248911_, ItemLike p_251265_, ItemLike p_250033_) {
        stonecutterResultFromBase(p_251589_, p_248911_, p_251265_, p_250033_, 1);
    }

    protected static void oreSmelting(@NotNull Consumer<FinishedRecipe> p_250654_, List<ItemLike> p_250172_, @NotNull RecipeCategory p_250588_, @NotNull ItemLike p_251868_, float p_250789_, int p_252144_, @NotNull String p_251687_) {
        oreCooking(p_250654_, RecipeSerializer.SMELTING_RECIPE, p_250172_, p_250588_, p_251868_, p_250789_, p_252144_, p_251687_, "_from_smelting");
    }

    protected static void oreBlasting(@NotNull Consumer<FinishedRecipe> p_248775_, List<ItemLike> p_251504_, @NotNull RecipeCategory p_248846_, @NotNull ItemLike p_249735_, float p_248783_, int p_250303_, @NotNull String p_251984_) {
        oreCooking(p_248775_, RecipeSerializer.BLASTING_RECIPE, p_251504_, p_248846_, p_249735_, p_248783_, p_250303_, p_251984_, "_from_blasting");
    }

    protected static void oreCooking(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> p_249619_, @NotNull RecipeCategory p_251154_, @NotNull ItemLike p_250066_, float p_251871_, int p_251316_, @NotNull String p_251450_, String p_249236_) {
        for(ItemLike itemlike : p_249619_) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), p_251154_, p_250066_, p_251871_, p_251316_, pCookingSerializer).group(p_251450_).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, getItemName(p_250066_) + p_249236_ + "_" + getItemName(itemlike));
        }

    }



    protected static void smeltingResultFromBase(@NotNull Consumer<FinishedRecipe> p_176740_, ItemLike p_176741_, ItemLike p_176742_) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(p_176742_), RecipeCategory.BUILDING_BLOCKS, p_176741_, 0.1F, 200).unlockedBy(getHasName(p_176742_), has(p_176742_)).save(p_176740_);
    }


}
