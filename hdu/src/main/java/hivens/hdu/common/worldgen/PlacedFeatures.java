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

        // Этериум: Самый редкий, встречается только глубоко. Пик на Y=-57.
        register(context, ETHEREUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_ETHEREUM_ORE_KEY),
                OrePlacement.commonOrePlacement(1, // 1 жила на чанк
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-50))));

        // Рубин: Очень распространен, но имеет свой пик. Пик на Y=8.
        register(context, RUBY_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_RUBY_ORE_KEY),
                OrePlacement.commonOrePlacement(7, // 7 жил на чанк
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        // Эфторит: Реже, чем рубин, и глубже. Пик на Y=-12.
        register(context, EFTORIT_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_EFTORIT_ORE_KEY),
                OrePlacement.commonOrePlacement(4, // 4 жилы на чанк
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(40))));

        // Камень надежды: Встречается только глубоко, большими залежами. Пик на Y=-47.
        register(context, HOPE_STONE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_HOPE_STONE_ORE_KEY),
                OrePlacement.commonOrePlacement(2, // 2 жилы на чанк
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-30))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}