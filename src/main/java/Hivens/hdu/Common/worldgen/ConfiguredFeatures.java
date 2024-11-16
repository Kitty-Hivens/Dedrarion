package Hivens.hdu.Common.worldgen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.HDU;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class ConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ETHEREUM_ORE_KEY = registerKey("ethereum_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_STONE_OF_HOPES_ORE_KEY = registerKey("stone_of_hopes");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        register(context,
                OVERWORLD_ETHEREUM_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        deepslateReplaceable,
                        BlockRegistry.ETHEREUM_ORE.get().defaultBlockState(),
                        3));

        register(context,
                OVERWORLD_STONE_OF_HOPES_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        deepslateReplaceable,
                        BlockRegistry.STONE_OF_HOPES.get().defaultBlockState(),
                        28));


    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(HDU.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                         ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));

    }


}
