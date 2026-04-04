package dedrarion.registry;

import dedrarion.Dedrarion;
import dedrarion.content.item.*;
import dedrarion.api.tier.ModTiers;
import dedrarion.api.item.TooltipFuelItem;
import dedrarion.api.item.TooltipItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> Items =
            DeferredRegister.create(ForgeRegistries.ITEMS, Dedrarion.MOD_ID);

    public static final RegistryObject<Item> RAW_ETHEREUM = Items.register("raw_ethereum",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ETHEREUM = Items.register("ethereum",
            () -> new TooltipItem(new Item.Properties(), "tooltip.item.dedrarion.ethereum"));

    public static final RegistryObject<Item> ETHEREUM_DUST = Items.register("ethereum_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRON_DUST = Items.register("iron_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EFTORIT_DUST = Items.register("eftorit_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EFTORIUM_DUST = Items.register("eftorium_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EFTORIUM_INGOT = Items.register("eftorium_ingot",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> MNEMOSYNE_ALETA = Items.register("mnemosyne_aleta",
            () -> new MnemosyneAletaItem(
                    ModTiers.ARTIFACT,
                    4,
                    -2.4f,
                    new Item.Properties().stacksTo(1).fireResistant()
            )
    );

    public static final RegistryObject<Item> TETRALIN = Items.register("tetralin",
            () -> new TetralinSwordItem(
                    ModTiers.ARTIFACT,
                    4,
                    -2.4f,
                    new Item.Properties().stacksTo(1).fireResistant()
            )
    );

    /**
     * Рубин. Классический самоцвет. Используется для различных крафтов. Встречается часто.
     */
    public static final RegistryObject<Item> RUBY = Items.register("ruby",
            () -> new Item(new Item.Properties()));

    /**
     * Эфторит. Классический самоцвет. Используется для различных крафтов. Встречается часто.
     */
    public  static final RegistryObject<Item> EFTORIT = Items.register("eftorit",
            () -> new Item(new Item.Properties()));

    /**
     * Катализатор забвения. На данный момент не используется.
     * TODO: Использовать на эфторитовой наковальне
     */
    public static final RegistryObject<Item> CATALYST_OF_DEBAUCHERY = Items.register("catalyst_of_debauchery",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAGIC_DETECTOR = Items.register("magic_detector",
            () -> new MagicDetectorItem(new Item.Properties()));

    public static final RegistryObject<Item> FORBIDDEN_FRUIT = Items.register("forbidden_fruit",
            () -> new TooltipItem(new Item.Properties().food(ModFood.FORBIDDEN_FRUIT), "tooltip.item.dedrarion.forbidden_fruit"));

    public static final RegistryObject<Item> FUEL_OF_PROMISES = Items.register("fuel_of_promises",
            () -> new TooltipFuelItem(new Item.Properties(), 400, "tooltip.item.dedrarion.fuel_of_promises"));

    public static void register(IEventBus eventBus) {
            Items.register(eventBus);
    }
}
