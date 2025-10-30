package hivens.hdu.common.registry;

import hivens.hdu.HDU;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HDU.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HDU_TAB = CREATIVE_MODE_TABS.register("hdu_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ETHEREUM.get()))
                    .title(Component.translatable("creativetab.hdu_tab"))
                    .displayItems((pParameters, pOutput) -> {


                        // Custom Item
                        pOutput.accept(ModItems.MAGIC_DETECTOR.get());
                        pOutput.accept(ModItems.ETHER_CORE.get());
                        pOutput.accept(ModItems.BROKEN_CARBON_PLATES.get());
                        pOutput.accept(ModItems.UNSTABLE_GUNPOWDER.get());
                        pOutput.accept(ModItems.MECHANICAL_PARTS.get());


                        // Custom Block
                        pOutput.accept(ModBlocks.EFTORIT_FORGE.get());

                        // Misc
                        pOutput.accept(ModItems.ETHEREUM.get());
                        pOutput.accept(ModItems.RAW_ETHEREUM.get());
                        pOutput.accept(ModItems.ETHEREUM_DUST.get());
                        pOutput.accept(ModItems.IRON_DUST.get());
                        pOutput.accept(ModItems.EFTORIT_DUST.get());
                        pOutput.accept(ModItems.EFTORIUM_DUST.get());
                        pOutput.accept(ModItems.EFTORIUM_INGOT.get());
                        pOutput.accept(ModItems.FOSSIL.get());
                        pOutput.accept(ModItems.RUBY.get());
                        pOutput.accept(ModItems.EFTORIT.get());

                        // Fuel
                        pOutput.accept(ModItems.FUEL_OF_PROMISES.get());

                        // Food
                        pOutput.accept(ModItems.FORBIDDEN_FRUIT.get());

                        // Attack
                        pOutput.accept(ModItems.MNEMOSYNE_ALETA.get());
                        pOutput.accept(ModItems.TETRALIN.get());
                        pOutput.accept(ModItems.DETONATION_BLADE.get());

                        // Ore Blocks
                        pOutput.accept(ModBlocks.ETHEREUM_ORE.get());
                        pOutput.accept(ModBlocks.ETHEREUM_BLOCK.get());
                        pOutput.accept(ModBlocks.RAW_ETHEREUM_BLOCK.get());

                        // Ruby

                        pOutput.accept(ModBlocks.RUBY_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_RUBY_ORE.get());
                        pOutput.accept(ModBlocks.RUBY_BLOCK.get());

                        // Eftorit

                        pOutput.accept(ModBlocks.EFTORIT_ORE.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_EFTORIT_ORE.get());
                        pOutput.accept(ModBlocks.EFTORIT_BLOCK.get());

                        // Eftorium
                        pOutput.accept(ModBlocks.EFTORIUM_BLOCK.get());


                        // Blocks
                        pOutput.accept(ModBlocks.HOPE_STONE.get());
                        pOutput.accept(ModBlocks.SMOOTH_HOPE_STONE.get());
                        pOutput.accept(ModBlocks.HOPE_SHARDS.get());

                        pOutput.accept(ModBlocks.HOPE_STONE_BRICKS.get());
                        pOutput.accept(ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get());
                        pOutput.accept(ModBlocks.HOPE_SHARD_BRICKS.get());

                        pOutput.accept(ModBlocks.HOPE_STONE_STAIRS.get());
                        pOutput.accept(ModBlocks.SMOOTH_HOPE_STONE_STAIRS.get());
                        pOutput.accept(ModBlocks.HOPE_SHARD_STAIRS.get());

                        pOutput.accept(ModBlocks.HOPE_BRICK_STAIRS.get());
                        pOutput.accept(ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get());
                        pOutput.accept(ModBlocks.HOPE_SHARD_BRICK_STAIRS.get());

                        // Structure
                        pOutput.accept(ModBlocks.BROKEN_PLANKS.get());
                        pOutput.accept(ModBlocks.EXTINGUISHED_TORCH.get());
                        pOutput.accept(ModBlocks.PEDESTAL.get());
                        pOutput.accept(ModBlocks.CONCUSSIVE_DYNAMITE.get());


                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
