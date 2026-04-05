package dedrarion.content.util;

import dedrarion.Dedrarion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        /** All blocks detectable by any Magic Detector mode. */
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");

        /** Common ores — Ruby, Eftorit and their deepslate variants. (RAW mode) */
        public static final TagKey<Block> METAL_DETECTOR_RAW = tag("metal_detector_raw");

        /** Rare ores — Ethereum. (ETHEREAL mode) */
        public static final TagKey<Block> METAL_DETECTOR_ETHEREAL = tag("metal_detector_ethereal");

        public static final TagKey<Block> NEEDS_ARTIFACT_TOOL = tag("needs_artifact_tool");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CATALYSTS = tag("catalysts");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, name));
        }
    }
}
