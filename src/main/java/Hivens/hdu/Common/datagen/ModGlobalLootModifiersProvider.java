package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.ItemRegistry;
import Hivens.hdu.Common.loot.AddItemModifier;
import Hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, HDU.MODID);
    }

    @Override
    protected void start() {
        /*
        add("test_from_test", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.AIR).build(),
                LootItemRandomChanceCondition.randomChance(0.3f).build()}, ItemRegistry.ETHEREUM.get()));

        add("fuel_from_promises_from_creeper", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "entities/creeper"
                )).build() },

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

         */

        add("metal_detector_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/ancient_city"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_ancient_city_ice_box", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/ancient_city_ice_box"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/jungle_temple"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_desert_pyramid", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/desert_pyramid"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_end_city_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/end_city_treasure"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_stronghold_corridor", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_library"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_stronghold_crossing", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_crossing"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));

        add("metal_detector_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_library"
                )).build()},

                ItemRegistry.METAL_DETECTOR.get()
        ));


        add("fuel_of_promises_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/ancient_city"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_ancient_city_ice_box", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/ancient_city_ice_box"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/jungle_temple"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_desert_pyramid", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/desert_pyramid"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_end_city_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/end_city_treasure"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_stronghold_corridor", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_library"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_stronghold_crossing", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_crossing"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_library"
                )).build()},

                ItemRegistry.FUEL_OF_PROMISES.get()
        ));

        add("ruby_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/ancient_city"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_ancient_city_ice_box", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/ancient_city_ice_box"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/jungle_temple"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_desert_pyramid", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/desert_pyramid"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_end_city_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/end_city_treasure"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_stronghold_corridor", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_library"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_stronghold_crossing", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_crossing"
                )).build()},

                ItemRegistry.RUBY.get()
        ));

        add("ruby_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation(
                        "chests/stronghold_library"
                )).build()},

                ItemRegistry.RUBY.get()
        ));
    }
}
