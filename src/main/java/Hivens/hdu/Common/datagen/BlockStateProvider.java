package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
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

        blockWithItem(BlockRegistry.STONE_OF_HOPES);
        blockWithItem(BlockRegistry.STAIRS_FROM_THE_STONE_OF_HOPES);
        blockWithItem(BlockRegistry.BRICKS_FROM_THE_STONE_OF_HOPES);

        blockWithItem(BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES);
        blockWithItem(BlockRegistry.STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES);
        blockWithItem(BlockRegistry.BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES);

        blockWithItem(BlockRegistry.SMOOTH_STONE_OF_HOPES);
        blockWithItem(BlockRegistry.STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES);
        blockWithItem(BlockRegistry.BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES);


    }


    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
