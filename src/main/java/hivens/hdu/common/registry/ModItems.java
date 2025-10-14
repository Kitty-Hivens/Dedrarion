package hivens.hdu.common.registry;

import hivens.hdu.common.Item.MagicDetectorItem;
import hivens.hdu.common.Item.MnemosyneAletaItem;
import hivens.hdu.common.Item.TetralinSwordItem;
import hivens.hdu.common.util.ModTooltipFoodItem;
import hivens.hdu.common.util.TooltipFuelItem;
import hivens.hdu.common.util.TooltipItem;
import hivens.hdu.HDU;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> Items =
            DeferredRegister.create(ForgeRegistries.ITEMS, HDU.MOD_ID);

    public static final RegistryObject<Item> RAW_ETHEREUM = Items.register("raw_ethereum",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ETHEREUM = Items.register("ethereum",
            () -> new TooltipItem(new Item.Properties(), "tooltip.item.hdu.ethereum"));

    public static final RegistryObject<Item> ETHEREUM_DUST = Items.register("ethereum_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MNEMOSYNE_ALETA = Items.register("mnemosyne_aleta",
            () -> new MnemosyneAletaItem(
                    ModTiers.ARTIFACT,  // Наш новый, мощный материал
                    4,                  // Модификатор урона (5.0 от тира + 4 = 9.0 урона)
                    -2.4f,              // Стандартная скорость атаки меча (1.6)
                    new Item.Properties().stacksTo(1).fireResistant() // Обязательно огнестойкий!
            )
    );

    public static final RegistryObject<Item> TETRALIN = Items.register("tetralin",
            () -> new TetralinSwordItem(
                    ModTiers.ARTIFACT, // Наш мощный материал
                    4,                 // Модификатор урона (5.0 + 4 = 9.0)
                    -2.4f,             // Стандартная скорость меча
                    new Item.Properties().stacksTo(1).fireResistant()
            )
    );

    /**
     * Ключевой компонент, выпавший из уничтоженного Стража.
     * Сделаем его редким и светящимся.
     */
    public static final RegistryObject<Item> ETHER_CORE = Items.register("ether_core",
            () -> new TooltipItem(new Item.Properties().rarity(Rarity.RARE), "tooltip.item.hdu.ether_core"));

    /**
     * Нестабильный порошок, используемый в особых взрывчатых веществах.
     */
    public static final RegistryObject<Item> UNSTABLE_GUNPOWDER = Items.register("unstable_gunpowder",
            () -> new TooltipItem(new Item.Properties(), "tooltip.item.hdu.unstable_gunpowder"));

    /**
     * Базовые механические детали.
     */
    public static final RegistryObject<Item> MECHANICAL_PARTS = Items.register("mechanical_parts",
            () -> new Item(new Item.Properties()));

    /**
     * Фрагменты внеземной брони. Невероятно прочные.
     */
    public static final RegistryObject<Item> BROKEN_CARBON_PLATES = Items.register("broken_carbon_plates",
            () -> new TooltipItem(new Item.Properties(), "tooltip.item.hdu.broken_carbon_plates"));

    public static final RegistryObject<Item> RUBY = Items.register("ruby",
            () -> new Item(new Item.Properties()));

    public  static final RegistryObject<Item> EFTORIT = Items.register("eftorit",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CATALYST_OF_DEBAUCHERY = Items.register("catalyst_of_debauchery",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METAL_DETECTOR = Items.register("metal_detector",
            () -> new MagicDetectorItem(new Item.Properties()));

    public static final RegistryObject<Item> FORBIDDEN_FRUIT = Items.register("forbidden_fruit",
            () -> new ModTooltipFoodItem(new Item.Properties().food(ModFood.FORBIDDEN_FRUIT), "tooltip.item.hdu.forbidden_fruit"));

    public static final RegistryObject<Item> FUEL_OF_PROMISES = Items.register("fuel_of_promises",
            () -> new TooltipFuelItem(new Item.Properties(), 400, "tooltip.item.hdu.fuel_of_promises"));

    public static void register(IEventBus eventBus) {
            Items.register(eventBus);
    }
}
