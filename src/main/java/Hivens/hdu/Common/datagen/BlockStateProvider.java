package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HDU.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockRegistry.ETHEREUM_BLOCK);
        blockWithItem(BlockRegistry.RAW_ETHEREUM_BLOCK);
        blockWithItem(BlockRegistry.ETHEREUM_ORE);

        blockWithItem(BlockRegistry.RUBY_ORE);
        blockWithItem(BlockRegistry.DEEPSLATE_RUBY_ORE);
        blockWithItem(BlockRegistry.RUBY_BLOCK);

        blockWithItem(BlockRegistry.EFTORIT_ORE);
        blockWithItem(BlockRegistry.DEEPSLATE_EFTORIT_ORE);
        blockWithItem(BlockRegistry.EFTORIT_BLOCK);

        blockWithItem(BlockRegistry.STONE_OF_HOPES);
        stairsBlock(((StairBlock) BlockRegistry.STAIRS_FROM_THE_STONE_OF_HOPES.get()), blockTexture(BlockRegistry.STONE_OF_HOPES.get()));
        blockWithItem(BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES);

        blockWithItem(BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES);
        stairsBlock(((StairBlock) BlockRegistry.STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get()), blockTexture(BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES.get()));
        blockWithItem(BlockRegistry.BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES);

        blockWithItem(BlockRegistry.SMOOTH_STONE_OF_HOPES);
        stairsBlock(((StairBlock) BlockRegistry.STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES.get()), blockTexture(BlockRegistry.SMOOTH_STONE_OF_HOPES.get()));
        blockWithItem(BlockRegistry.BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES);

        stairsBlock(((StairBlock) BlockRegistry.STAIRS_FROM_THE_BRICKS_FROM_THE_STONE_OF_HOPES.get()), blockTexture(BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES.get()));
        stairsBlock(((StairBlock) BlockRegistry.STAIRS_FROM_THE_BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES.get()), blockTexture(BlockRegistry.BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES.get()));
        stairsBlock(((StairBlock) BlockRegistry.STAIRS_FROM_THE_BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get()), blockTexture(BlockRegistry.BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES.get()));
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
