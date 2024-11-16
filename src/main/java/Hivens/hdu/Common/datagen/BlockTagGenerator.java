package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.Common.util.ModTags;
import Hivens.hdu.HDU;
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
        super(output, lookupProvider, HDU.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(BlockRegistry.ETHEREUM_ORE.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(BlockRegistry.ETHEREUM_ORE.get(),
                        BlockRegistry.RAW_ETHEREUM_BLOCK.get(),
                        BlockRegistry.ETHEREUM_BLOCK.get(),

                        BlockRegistry.STONE_OF_HOPES.get(),
                        BlockRegistry.STAIRS_FROM_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES.get(),

                        BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get(),

                        BlockRegistry.SMOOTH_STONE_OF_HOPES.get(),
                        BlockRegistry.STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES.get()

    );
        this.tag(BlockTags.NEEDS_STONE_TOOL);

        this.tag(BlockTags.NEEDS_IRON_TOOL);

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                .add(BlockRegistry.ETHEREUM_ORE.get(),
                        BlockRegistry.RAW_ETHEREUM_BLOCK.get(),
                        BlockRegistry.ETHEREUM_BLOCK.get(),

                        BlockRegistry.STONE_OF_HOPES.get(),
                        BlockRegistry.STAIRS_FROM_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES.get(),

                        BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get(),

                        BlockRegistry.SMOOTH_STONE_OF_HOPES.get(),
                        BlockRegistry.STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES.get()

                );

        this.tag(BlockTags.STAIRS)
                .add(BlockRegistry.STAIRS_FROM_THE_STONE_OF_HOPES.get(),
                    BlockRegistry.STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES.get(),
                    BlockRegistry.STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get());

        this.tag(BlockTags.STONE_BRICKS)
                .add(BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES.get(),
                        BlockRegistry.BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get());
    }
}
