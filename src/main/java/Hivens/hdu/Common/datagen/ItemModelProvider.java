package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.ItemRegistry;
import Hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HDU.MODID, "item/" + item.getId().getPath()));
    }
}
