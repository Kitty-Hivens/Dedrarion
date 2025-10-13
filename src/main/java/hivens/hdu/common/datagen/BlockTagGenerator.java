package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.BlockRegistry;
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

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HDU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(
                        BlockRegistry.ETHEREUM_ORE.get(),
                        BlockRegistry.RUBY_ORE.get(),
                        BlockRegistry.DEEPSLATE_RUBY_ORE.get(),
                        BlockRegistry.EFTORIT_ORE.get(),
                        BlockRegistry.DEEPSLATE_EFTORIT_ORE.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        BlockRegistry.BROKEN_PLANKS.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                   .add(
                           BlockRegistry.ETHEREUM_ORE.get(),
                           BlockRegistry.RAW_ETHEREUM_BLOCK.get(),
                           BlockRegistry.ETHEREUM_BLOCK.get(),
                           BlockRegistry.RUBY_ORE.get(),
                           BlockRegistry.RUBY_BLOCK.get(),
                           BlockRegistry.RUBY_BLOCK.get(),

                           BlockRegistry.EFTORIT_ORE.get(),
                           BlockRegistry.DEEPSLATE_EFTORIT_ORE.get(),
                           BlockRegistry.EFTORIT_BLOCK.get(),
                           BlockRegistry.DEEPSLATE_EFTORIT_ORE.get(),
                           BlockRegistry.EFTORIT_FORGE.get(),

                           BlockRegistry.HOPE_STONE.get(),
                           BlockRegistry.HOPE_STONE_STAIRS.get(),
                           BlockRegistry.HOPE_STONE_BRICKS.get(),

                           BlockRegistry.HOPE_SHARDS.get(),
                           BlockRegistry.HOPE_SHARD_STAIRS.get(),
                           BlockRegistry.HOPE_SHARD_BRICKS.get(),

                           BlockRegistry.SMOOTH_HOPE_STONE.get(),
                           BlockRegistry.SMOOTH_HOPE_STONE_STAIRS.get(),
                           BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get(),

                           BlockRegistry.HOPE_BRICK_STAIRS.get(),
                           BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get(),
                           BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get(),

                           BlockRegistry.PEDESTAL.get()
                   );
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                    .add(
                            BlockRegistry.PEDESTAL.get()
                );

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                   .add(
                           BlockRegistry.RUBY_ORE.get(),
                           BlockRegistry.DEEPSLATE_RUBY_ORE.get(),
                           BlockRegistry.RUBY_BLOCK.get(),
                           BlockRegistry.EFTORIT_ORE.get(),
                           BlockRegistry.DEEPSLATE_EFTORIT_ORE.get(),
                           BlockRegistry.EFTORIT_BLOCK.get(),
                           BlockRegistry.EFTORIT_FORGE.get()
                   );


        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(
                        BlockRegistry.ETHEREUM_ORE.get(),
                        BlockRegistry.RAW_ETHEREUM_BLOCK.get(),
                        BlockRegistry.ETHEREUM_BLOCK.get(),

                        BlockRegistry.HOPE_STONE.get(),
                        BlockRegistry.SMOOTH_HOPE_STONE.get(),
                        BlockRegistry.HOPE_SHARDS.get(),

                        BlockRegistry.HOPE_STONE_BRICKS.get(),
                        BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get(),
                        BlockRegistry.HOPE_SHARD_BRICKS.get(),

                        BlockRegistry.HOPE_STONE_STAIRS.get(),
                        BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get(),
                        BlockRegistry.HOPE_SHARD_STAIRS.get(),


                        BlockRegistry.HOPE_BRICK_STAIRS.get(),
                        BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get(),
                        BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get()
                );

        this.tag(BlockTags.STAIRS)
                .add(
                        BlockRegistry.HOPE_STONE_STAIRS.get(),
                        BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get(),
                        BlockRegistry.HOPE_SHARD_STAIRS.get(),

                        BlockRegistry.HOPE_BRICK_STAIRS.get(),
                        BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get(),
                        BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get()
                );

        this.tag(BlockTags.STONE_BRICKS)
                .add(
                        BlockRegistry.HOPE_STONE_BRICKS.get(),
                        BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get(),
                        BlockRegistry.HOPE_SHARD_BRICKS.get()
                );

    }
}
