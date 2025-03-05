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

                        // Custom Block
                        pOutput.accept(BlockRegistry.EFTORIT_FORGE.get());

                        // Misc
                        pOutput.accept(ItemRegistry.ETHEREUM.get());
                        pOutput.accept(ItemRegistry.RAW_ETHEREUM.get());
                        pOutput.accept(ItemRegistry.ETHEREUM_DUST.get());
                        pOutput.accept(ItemRegistry.RUBY.get());
                        pOutput.accept(ItemRegistry.EFTORIT.get());

                        // Fuel
                        pOutput.accept(ItemRegistry.FUEL_OF_PROMISES.get());

                        // Food
                        pOutput.accept(ItemRegistry.FORBIDDEN_FRUIT.get());

                        // Attack
                        pOutput.accept(ItemRegistry.MNEMOSYNE_ALETA.get());

                        // Ore Blocks
                        pOutput.accept(BlockRegistry.ETHEREUM_ORE.get());
                        pOutput.accept(BlockRegistry.ETHEREUM_BLOCK.get());
                        pOutput.accept(BlockRegistry.RAW_ETHEREUM_BLOCK.get());

                        // Ruby

                        pOutput.accept(BlockRegistry.RUBY_ORE.get());
                        pOutput.accept(BlockRegistry.DEEPSLATE_RUBY_ORE.get());
                        pOutput.accept(BlockRegistry.RUBY_BLOCK.get());

                        // Eftorit

                        pOutput.accept(BlockRegistry.EFTORIT_ORE.get());
                        pOutput.accept(BlockRegistry.DEEPSLATE_EFTORIT_ORE.get());
                        pOutput.accept(BlockRegistry.EFTORIT_BLOCK.get());


                        // Blocks
                        pOutput.accept(BlockRegistry.HOPE_STONE.get());
                        pOutput.accept(BlockRegistry.SMOOTH_HOPE_STONE.get());
                        pOutput.accept(BlockRegistry.HOPE_SHARDS.get());

                        pOutput.accept(BlockRegistry.HOPE_STONE_BRICKS.get());
                        pOutput.accept(BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get());
                        pOutput.accept(BlockRegistry.HOPE_SHARD_BRICKS.get());

                        pOutput.accept(BlockRegistry.HOPE_STONE_STAIRS.get());
                        pOutput.accept(BlockRegistry.SMOOTH_HOPE_STONE_STAIRS.get());
                        pOutput.accept(BlockRegistry.HOPE_SHARD_STAIRS.get());

                        pOutput.accept(BlockRegistry.HOPE_BRICK_STAIRS.get());
                        pOutput.accept(BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get());
                        pOutput.accept(BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get());

                        // Structure
                        pOutput.accept(BlockRegistry.BROKEN_PLANKS.get());
                        pOutput.accept(BlockRegistry.EXTINGUISHED_TORCH.get());
                        pOutput.accept(BlockRegistry.PEDESTAL.get());


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
