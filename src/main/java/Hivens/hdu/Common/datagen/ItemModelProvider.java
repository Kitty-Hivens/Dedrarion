package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.Common.Registry.ItemRegistry;
import Hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HDU.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemRegistry.ETHEREUM);
        simpleItem(ItemRegistry.RAW_ETHEREUM);
        simpleItem(ItemRegistry.ETHEREUM_DUST);

        simpleItem(ItemRegistry.METAL_DETECTOR);
        simpleItem(ItemRegistry.FUEL_OF_PROMISES);
        simpleItem(ItemRegistry.FORBIDDEN_FRUIT);

        evenSimplerBlockItem(BlockRegistry.STAIRS_FROM_THE_STONE_OF_HOPES);
        evenSimplerBlockItem(BlockRegistry.STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES);
        evenSimplerBlockItem(BlockRegistry.STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES);
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HDU.MODID, "item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(HDU.MODID + ":" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(),
                modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(),
                modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath() + "_bottom"));
    }
}
