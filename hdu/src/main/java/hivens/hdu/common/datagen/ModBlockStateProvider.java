package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HDU.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.ETHEREUM_BLOCK);
        blockWithItem(ModBlocks.RAW_ETHEREUM_BLOCK);
        blockWithItem(ModBlocks.ETHEREUM_ORE);

        blockWithItem(ModBlocks.RUBY_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_RUBY_ORE);
        blockWithItem(ModBlocks.RUBY_BLOCK);

        blockWithItem(ModBlocks.EFTORIT_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_EFTORIT_ORE);
        blockWithItem(ModBlocks.EFTORIT_BLOCK);

        blockWithItem(ModBlocks.HOPE_STONE);
        blockWithItem(ModBlocks.SMOOTH_HOPE_STONE);
        blockWithItem(ModBlocks.HOPE_SHARDS);

        blockWithItem(ModBlocks.HOPE_STONE_BRICKS);
        blockWithItem(ModBlocks.SMOOTH_HOPE_STONE_BRICKS);
        blockWithItem(ModBlocks.HOPE_SHARD_BRICKS);

        stairsBlock(ModBlocks.HOPE_STONE_STAIRS.get(), blockTexture(ModBlocks.HOPE_STONE.get()));
        stairsBlock(ModBlocks.SMOOTH_HOPE_STONE_STAIRS.get(), blockTexture(ModBlocks.SMOOTH_HOPE_STONE.get()));
        stairsBlock(ModBlocks.HOPE_SHARD_STAIRS.get(), blockTexture(ModBlocks.HOPE_SHARDS.get()));

        stairsBlock(ModBlocks.HOPE_BRICK_STAIRS.get(), blockTexture(ModBlocks.HOPE_STONE_BRICKS.get()));
        stairsBlock(ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get(), blockTexture(ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get()));
        stairsBlock(ModBlocks.HOPE_SHARD_BRICK_STAIRS.get(), blockTexture(ModBlocks.HOPE_SHARD_BRICKS.get()));

        blockWithItem(ModBlocks.BROKEN_PLANKS);
        blockWithItem(ModBlocks.EXTINGUISHED_TORCH);
    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
