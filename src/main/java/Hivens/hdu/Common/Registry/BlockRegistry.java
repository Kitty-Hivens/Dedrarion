package Hivens.hdu.Common.Registry;

import Hivens.hdu.HDU;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HDU.MODID);

    public static final RegistryObject<Block> ETHEREUM_ORE = registerBlock("ethereum_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.NETHER_GOLD_ORE)
                    .strength(3f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> ETHEREUM_BLOCK = registerBlock("ethereum_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.NETHER_GOLD_ORE)));
    public static final RegistryObject<Block> RAW_ETHEREUM_BLOCK = registerBlock("raw_ethereum_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE).sound(SoundType.TUFF)));
    public static final RegistryObject<Block> STONE_OF_HOPES = registerBlock("stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));
    public static final RegistryObject<Block> SHARDS_OF_THE_STONE_OF_HOPES = registerBlock("shards_of_the_stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }



    private static <T extends Block> RegistryObject <Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemRegistry.Items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
