package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.registry.ModItems;
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

public class ModItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HDU.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.ETHEREUM);
        simpleItem(ModItems.RAW_ETHEREUM);
        simpleItem(ModItems.ETHEREUM_DUST);

        simpleItem(ModItems.RUBY);

        simpleItem(ModItems.EFTORIT);

        simpleItem(ModItems.METAL_DETECTOR);
        simpleItem(ModItems.FUEL_OF_PROMISES);
        simpleItem(ModItems.FORBIDDEN_FRUIT);

        simpleItem(ModItems.MNEMOSYNE_ALETA);

        evenSimplerBlockItem(ModBlocks.HOPE_STONE_STAIRS);
        evenSimplerBlockItem(ModBlocks.SMOOTH_HOPE_STONE_STAIRS);
        evenSimplerBlockItem(ModBlocks.HOPE_SHARD_STAIRS);
        evenSimplerBlockItem(ModBlocks.HOPE_BRICK_STAIRS);
        evenSimplerBlockItem(ModBlocks.SMOOTH_HOPE_BRICK_STAIRS);
        evenSimplerBlockItem(ModBlocks.HOPE_SHARD_BRICK_STAIRS);

    }
    private void simpleItem(RegistryObject<Item> item){
        assert item.getId() != null;
        withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "item/" + item.getId().getPath()));
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
