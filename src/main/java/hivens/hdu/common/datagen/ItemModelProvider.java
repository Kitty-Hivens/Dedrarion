package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.BlockRegistry;
import hivens.hdu.common.registry.ItemRegistry;
import hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HDU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemRegistry.ETHEREUM);
        simpleItem(ItemRegistry.RAW_ETHEREUM);
        simpleItem(ItemRegistry.ETHEREUM_DUST);

        simpleItem(ItemRegistry.RUBY);

        simpleItem(ItemRegistry.EFTORIT);

        simpleItem(ItemRegistry.METAL_DETECTOR);
        simpleItem(ItemRegistry.FUEL_OF_PROMISES);
        simpleItem(ItemRegistry.FORBIDDEN_FRUIT);

        evenSimplerBlockItem(BlockRegistry.HOPE_STONE_STAIRS);
        evenSimplerBlockItem(BlockRegistry.SMOOTH_HOPE_STONE_STAIRS);
        evenSimplerBlockItem(BlockRegistry.HOPE_SHARD_STAIRS);
        evenSimplerBlockItem(BlockRegistry.HOPE_BRICK_STAIRS);
        evenSimplerBlockItem(BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS);
        evenSimplerBlockItem(BlockRegistry.HOPE_SHARD_BRICK_STAIRS);

    }
    private void simpleItem(RegistryObject<Item> item){
        withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HDU.MOD_ID, "item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<StairBlock> block) {
        this.withExistingParent(HDU.MOD_ID + ":" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(),
                modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(),
                modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath() + "_bottom"));
    }
}
