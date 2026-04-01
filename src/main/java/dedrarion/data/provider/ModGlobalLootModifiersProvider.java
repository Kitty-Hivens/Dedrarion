package dedrarion.data.provider;

import dedrarion.Dedrarion;
import dedrarion.content.loot.AddItemModifier;
import dedrarion.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {

    private static final List<ResourceLocation> LOOT_TABLES = List.of(
            ResourceLocation.parse("chests/ancient_city"),
            ResourceLocation.parse("chests/ancient_city_ice_box"),
            ResourceLocation.parse("chests/jungle_temple"),
            ResourceLocation.parse("chests/desert_pyramid"),
            ResourceLocation.parse("chests/end_city_treasure"),
            ResourceLocation.parse("chests/stronghold_library"),
            ResourceLocation.parse("chests/stronghold_crossing"),
            ResourceLocation.parse("chests/stronghold_corridor")
    );

    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Dedrarion.MOD_ID);
    }

    @Override
    protected void start() {
        addToAllLootTables(ModItems.RUBY.get());
        addToAllLootTables(ModItems.FUEL_OF_PROMISES.get());
        addToAllLootTables(ModItems.MAGIC_DETECTOR.get());
    }

    private void addToAllLootTables(Item item) {
        String itemName = item.getDescriptionId()
                .replace("item.dedrarion.", "");

        LOOT_TABLES.forEach(table -> {
            String locationName = table.getPath().replace("chests/", "");
            add(
                    itemName + "_from_" + locationName,
                    new AddItemModifier(
                            new LootItemCondition[]{
                                    new LootTableIdCondition.Builder(table).build()
                            },
                            item
                    )
            );
        });
    }
}
