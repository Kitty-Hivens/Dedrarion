package Hivens.hdu.Common.Registry;

import Hivens.hdu.Common.Custom.Item.*;
import Hivens.hdu.Common.util.TooltipFoodItem;
import Hivens.hdu.Common.util.TooltipFuelItem;
import Hivens.hdu.Common.util.TooltipItem;
import Hivens.hdu.HDU;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {

    public static final DeferredRegister<Item> Items =
            DeferredRegister.create(ForgeRegistries.ITEMS, HDU.MODID);

    public static final RegistryObject<Item> RAW_ETHEREUM = Items.register("raw_ethereum",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ETHEREUM = Items.register("ethereum",
            () -> new TooltipItem(new Item.Properties(), "tooltip.item.hdu.ethereum"));

    public static final RegistryObject<Item> ETHEREUM_DUST = Items.register("ethereum_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MNEMOSYNE_ALETA = Items.register("mnemosyne_aleta",
            () -> new MnemosyneAletaItem(
                    Tiers.DIAMOND,  // Базовый уровень прочности (можно изменить)
                    -2,             // Модификатор урона (базовый урон минус 2)
                    -2.4f,          // Скорость атаки
                    new Item.Properties().stacksTo(1).durability(1000)
            )
    );

    public static final RegistryObject<Item> RUBY = Items.register("ruby",
            () -> new Item(new Item.Properties()));

    public  static final RegistryObject<Item> EFTORIT = Items.register("eftorit",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CATALYST_OF_DEBAUCHERY = Items.register("catalyst_of_debauchery",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METAL_DETECTOR = Items.register("metal_detector",
            () -> new MagicDetectorItem(new Item.Properties()));

    public static final RegistryObject<Item> FORBIDDEN_FRUIT = Items.register("forbidden_fruit",
            () -> new TooltipFoodItem(new Item.Properties().food(FoodRegistry.FORBIDDEN_FRUIT), "tooltip.item.hdu.forbidden_fruit"));

    public static final RegistryObject<Item> FUEL_OF_PROMISES = Items.register("fuel_of_promises",
            () -> new TooltipFuelItem(new Item.Properties(), 400, "tooltip.item.hdu.fuel_of_promises"));

    public static void register(IEventBus eventBus) {
            Items.register(eventBus);
    }
}
