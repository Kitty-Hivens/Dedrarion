package hivens.hdu.common.worldgen;

import hivens.hdu.HDU;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class PlacedFeatures {
    public static final ResourceKey<PlacedFeature> ETHEREUM_ORE_PLACED_KEY = registerKey("ethereum_ore_placed");
    public static final ResourceKey<PlacedFeature> RUBY_ORE_PLACED_KEY = registerKey("ruby_ore_placed");
    public static final ResourceKey<PlacedFeature> EFTORIT_ORE_PLACED_KEY = registerKey("eftorit_ore_placed");
    public static final ResourceKey<PlacedFeature> HOPE_STONE_PLACED_KEY = registerKey("hope_stone_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ETHEREUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_ETHEREUM_ORE_KEY),
                OrePlacement.rareOrePlacement(1,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-50))));
        register(context, RUBY_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_RUBY_ORE_KEY),
                OrePlacement.commonOrePlacement(7,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));
        register(context, EFTORIT_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_EFTORIT_ORE_KEY),
                OrePlacement.commonOrePlacement(4,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(40))));
        register(context, HOPE_STONE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_HOPE_STONE_ORE_KEY),
                OrePlacement.rareOrePlacement(2,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-30))));

    }



    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(HDU.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
