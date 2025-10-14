package hivens.hdu.common.worldgen;

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.HDU;
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

import java.util.List;

public class ConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ETHEREUM_ORE_KEY = registerKey("ethereum_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RUBY_ORE_KEY = registerKey("ruby_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_EFTORIT_ORE_KEY = registerKey("eftorit_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_HOPE_STONE_ORE_KEY = registerKey("hope_stone");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldRubyOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.RUBY_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_RUBY_ORE.get().defaultBlockState()
                ));

        List<OreConfiguration.TargetBlockState> overworldEftoritOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.EFTORIT_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DEEPSLATE_EFTORIT_ORE.get().defaultBlockState()) // <-- ИСПРАВЛЕНО
        );


        register(context,
                OVERWORLD_ETHEREUM_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        deepslateReplaceable,
                        ModBlocks.ETHEREUM_ORE.get().defaultBlockState(),
                        3));

        register(context,
                OVERWORLD_RUBY_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        overworldRubyOres,
                        7));

        register(context,
                OVERWORLD_EFTORIT_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        overworldEftoritOres,
                        4));

        register(context,
                OVERWORLD_HOPE_STONE_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(
                        deepslateReplaceable,
                        ModBlocks.HOPE_STONE.get().defaultBlockState(),
                        64));


    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                         ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));

    }


}
