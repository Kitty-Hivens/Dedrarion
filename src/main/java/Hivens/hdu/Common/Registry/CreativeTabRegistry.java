package Hivens.hdu.Common.Registry;

import Hivens.hdu.HDU;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HDU.MODID);

    public static final RegistryObject<CreativeModeTab> HDU_TAB = CREATIVE_MODE_TABS.register("hdu_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemRegistry.ETHEREUM.get()))
                    .title(Component.translatable("creativetab.hdu_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // Custom Item
                        pOutput.accept(ItemRegistry.METAL_DETECTOR.get());

                        // Misc
                        pOutput.accept(ItemRegistry.ETHEREUM.get());
                        pOutput.accept(ItemRegistry.RAW_ETHEREUM.get());
                        pOutput.accept(ItemRegistry.ETHEREUM_DUST.get());

                        // Fuel
                        pOutput.accept(ItemRegistry.FUEL_OF_PROMISES.get());

                        // Food
                        pOutput.accept(ItemRegistry.FORBIDDEN_FRUIT.get());

                        // Ore Blocks
                        pOutput.accept(BlockRegistry.ETHEREUM_ORE.get());
                        pOutput.accept(BlockRegistry.ETHEREUM_BLOCK.get());
                        pOutput.accept(BlockRegistry.RAW_ETHEREUM_BLOCK.get());


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
