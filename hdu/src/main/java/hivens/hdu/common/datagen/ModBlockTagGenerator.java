package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.util.ModTags;
import hivens.hdu.HDU;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HDU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(
                        ModBlocks.ETHEREUM_ORE.get(),
                        ModBlocks.RUBY_ORE.get(),
                        ModBlocks.DEEPSLATE_RUBY_ORE.get(),
                        ModBlocks.EFTORIT_ORE.get(),
                        ModBlocks.DEEPSLATE_EFTORIT_ORE.get()
                );


        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        ModBlocks.BROKEN_PLANKS.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                   .add(
                           ModBlocks.ETHEREUM_ORE.get(),
                           ModBlocks.RAW_ETHEREUM_BLOCK.get(),
                           ModBlocks.ETHEREUM_BLOCK.get(),
                           ModBlocks.RUBY_ORE.get(),
                           ModBlocks.DEEPSLATE_RUBY_ORE.get(),
                           ModBlocks.RUBY_BLOCK.get(),

                           ModBlocks.EFTORIT_ORE.get(),
                           ModBlocks.DEEPSLATE_EFTORIT_ORE.get(),
                           ModBlocks.EFTORIT_BLOCK.get(),
                           ModBlocks.DEEPSLATE_EFTORIT_ORE.get(),
                           ModBlocks.EFTORIT_FORGE.get(),

                           ModBlocks.HOPE_STONE.get(),
                           ModBlocks.HOPE_STONE_STAIRS.get(),
                           ModBlocks.HOPE_STONE_BRICKS.get(),

                           ModBlocks.HOPE_SHARDS.get(),
                           ModBlocks.HOPE_SHARD_STAIRS.get(),
                           ModBlocks.HOPE_SHARD_BRICKS.get(),

                           ModBlocks.SMOOTH_HOPE_STONE.get(),
                           ModBlocks.SMOOTH_HOPE_STONE_STAIRS.get(),
                           ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get(),

                           ModBlocks.HOPE_BRICK_STAIRS.get(),
                           ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get(),
                           ModBlocks.HOPE_SHARD_BRICK_STAIRS.get(),

                           ModBlocks.PEDESTAL.get()
                   );
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                    .add(
                            ModBlocks.PEDESTAL.get()
                );

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                   .add(
                           ModBlocks.RUBY_ORE.get(),
                           ModBlocks.DEEPSLATE_RUBY_ORE.get(),
                           ModBlocks.RUBY_BLOCK.get(),
                           ModBlocks.EFTORIT_ORE.get(),
                           ModBlocks.DEEPSLATE_EFTORIT_ORE.get(),
                           ModBlocks.EFTORIT_BLOCK.get(),
                           ModBlocks.EFTORIT_FORGE.get()
                   );


        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(
                        ModBlocks.ETHEREUM_ORE.get(),
                        ModBlocks.RAW_ETHEREUM_BLOCK.get(),
                        ModBlocks.ETHEREUM_BLOCK.get(),

                        ModBlocks.HOPE_STONE.get(),
                        ModBlocks.SMOOTH_HOPE_STONE.get(),
                        ModBlocks.HOPE_SHARDS.get(),

                        ModBlocks.HOPE_STONE_BRICKS.get(),
                        ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get(),
                        ModBlocks.HOPE_SHARD_BRICKS.get(),

                        ModBlocks.HOPE_STONE_STAIRS.get(),
                        ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get(),
                        ModBlocks.HOPE_SHARD_STAIRS.get(),


                        ModBlocks.HOPE_BRICK_STAIRS.get(),
                        ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get(),
                        ModBlocks.HOPE_SHARD_BRICK_STAIRS.get()
                );

        this.tag(ModTags.Blocks.NEEDS_ARTIFACT_TOOL);


        this.tag(BlockTags.STAIRS)
                .add(
                        ModBlocks.HOPE_STONE_STAIRS.get(),
                        ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get(),
                        ModBlocks.HOPE_SHARD_STAIRS.get(),

                        ModBlocks.HOPE_BRICK_STAIRS.get(),
                        ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get(),
                        ModBlocks.HOPE_SHARD_BRICK_STAIRS.get()
                );

        this.tag(BlockTags.STONE_BRICKS)
                .add(
                        ModBlocks.HOPE_STONE_BRICKS.get(),
                        ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get(),
                        ModBlocks.HOPE_SHARD_BRICKS.get()
                );

    }
}
