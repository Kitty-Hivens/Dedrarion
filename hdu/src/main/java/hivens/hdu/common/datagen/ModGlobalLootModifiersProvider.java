package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.ModItems;
import hivens.hdu.common.loot.AddItemModifier;
import hivens.hdu.HDU;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, HDU.MOD_ID);
    }

    @Override
    protected void start() {

        add("magic_detector_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse (
                        "chests/ancient_city"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("magic_detector_from_ancient_city_ice_box", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse (
                        "chests/ancient_city_ice_box"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("magic_detector_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/jungle_temple"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("magic_detector_from_desert_pyramid", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/desert_pyramid"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("magic_detector_from_end_city_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/end_city_treasure"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("magic_detector_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("fuel_of_promises_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));

        add("ruby_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.MAGIC_DETECTOR.get()
        ));


        add("fuel_of_promises_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/ancient_city"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_ancient_city_ice_box", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/ancient_city_ice_box"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/jungle_temple"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_desert_pyramid", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/desert_pyramid"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_end_city_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/end_city_treasure"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_stronghold_corridor", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_stronghold_crossing", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_crossing"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("fuel_of_promises_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.FUEL_OF_PROMISES.get()
        ));

        add("ruby_from_ancient_city", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/ancient_city"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_ancient_city_ice_box", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/ancient_city_ice_box"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/jungle_temple"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_desert_pyramid", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/desert_pyramid"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_end_city_treasure", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/end_city_treasure"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_stronghold_corridor", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_stronghold_crossing", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_crossing"
                )).build()},

                ModItems.RUBY.get()
        ));

        add("ruby_from_stronghold_library", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(ResourceLocation.parse(
                        "chests/stronghold_library"
                )).build()},

                ModItems.RUBY.get()
        ));
    }
}
