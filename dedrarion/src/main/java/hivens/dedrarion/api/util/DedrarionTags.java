package hivens.dedrarion.api.util;

import hivens.dedrarion.Dedrarion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class DedrarionTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_ARTIFACT_TOOL = tag("needs_artifact_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, name));
        }

    }

    public static class Items {

        private static TagKey<Item> tag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, name));
        }

    }
}
