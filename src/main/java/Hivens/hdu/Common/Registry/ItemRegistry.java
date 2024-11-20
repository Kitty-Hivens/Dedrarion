package Hivens.hdu.Common.Registry;

import Hivens.hdu.Common.Custom.Item.*;
import Hivens.hdu.HDU;
import net.minecraft.world.item.Item;
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

    public static final RegistryObject<Item> RUBY = Items.register("ruby",
            () -> new Item(new Item.Properties()));

    public  static final RegistryObject<Item> EFTORIT = Items.register("eftorit",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> METAL_DETECTOR = Items.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties()));

    public static final RegistryObject<Item> FORBIDDEN_FRUIT = Items.register("forbidden_fruit",
            () -> new TooltipFoodItem(new Item.Properties().food(FoodRegistry.FORBIDDEN_FRUIT), "tooltip.item.hdu.forbidden_fruit"));

    public static final RegistryObject<Item> FUEL_OF_PROMISES = Items.register("fuel_of_promises",
            () -> new TooltipFuelItem(new Item.Properties(), 400, "tooltip.item.hdu.fuel_of_promises"));

    public static void register(IEventBus eventBus) {
            Items.register(eventBus);
    }
}
