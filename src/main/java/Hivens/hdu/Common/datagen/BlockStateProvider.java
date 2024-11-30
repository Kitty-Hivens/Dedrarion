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

        blockWithItem(BlockRegistry.HOPE_STONE);
        blockWithItem(BlockRegistry.SMOOTH_HOPE_STONE);
        blockWithItem(BlockRegistry.HOPE_SHARDS);

        blockWithItem(BlockRegistry.HOPE_STONE_BRICKS);
        blockWithItem(BlockRegistry.SMOOTH_HOPE_STONE_BRICKS);
        blockWithItem(BlockRegistry.HOPE_SHARD_BRICKS);

        stairsBlock(((StairBlock) BlockRegistry.HOPE_STONE_STAIRS.get()), blockTexture(BlockRegistry.HOPE_STONE.get()));
        stairsBlock(((StairBlock) BlockRegistry.SMOOTH_HOPE_STONE_STAIRS.get()), blockTexture(BlockRegistry.SMOOTH_HOPE_STONE.get()));
        stairsBlock(((StairBlock) BlockRegistry.HOPE_SHARD_STAIRS.get()), blockTexture(BlockRegistry.HOPE_SHARDS.get()));

        stairsBlock(((StairBlock) BlockRegistry.HOPE_BRICK_STAIRS.get()), blockTexture(BlockRegistry.HOPE_STONE_BRICKS.get()));
        stairsBlock(((StairBlock) BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get()), blockTexture(BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get()));
        stairsBlock(((StairBlock) BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get()), blockTexture(BlockRegistry.HOPE_SHARD_BRICKS.get()));
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
