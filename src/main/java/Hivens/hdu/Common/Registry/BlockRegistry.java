package Hivens.hdu.Common.Registry;

import Hivens.hdu.Common.Custom.Block.EftoritForgeBlock;
import Hivens.hdu.HDU;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
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



    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE).sound(SoundType.METAL)
                    .strength(1f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE).sound(SoundType.METAL)
                    .strength(1f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));
    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE).sound(SoundType.METAL)));

    public static  final RegistryObject<Block> EFTORIT_ORE = registerBlock("eftorit_ore",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE).sound(SoundType.COPPER)));
    public static  final RegistryObject<Block> DEEPSLATE_EFTORIT_ORE = registerBlock("deepslate_eftorit_ore",
            ()-> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE).sound(SoundType.COPPER)));
    public static  final  RegistryObject<Block> EFTORIT_BLOCK = registerBlock("eftorit_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK).sound(SoundType.METAL)));
    public static  final  RegistryObject<Block> EFTORIT_FORGE = registerBlock("eftorit_forge",
            () -> new EftoritForgeBlock(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK).sound(SoundType.METAL)));





    public static final RegistryObject<Block> STONE_OF_HOPES = registerBlock("stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> STAIRS_FROM_THE_STONE_OF_HOPES = registerBlock("stairs_from_the_stone_of_hopes",
            () -> new StairBlock(() -> BlockRegistry.STONE_OF_HOPES.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> BRICKS_FROM_THE_STONE_OF_HOPES = registerBlock("bricks_from_the_stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> STAIRS_FROM_THE_BRICKS_FROM_THE_STONE_OF_HOPES = registerBlock("stairs_from_the_bricks_from_the_stone_of_hopes",
            () -> new StairBlock(() -> BlockRegistry.SMOOTH_STONE_OF_HOPES.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));



    public static final RegistryObject<Block> SHARDS_OF_THE_STONE_OF_HOPES = registerBlock("shards_of_the_stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> STAIRS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES = registerBlock("stairs_from_the_shards_of_the_stone_of_hopes",
            () -> new StairBlock(() -> BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES = registerBlock("bricks_from_the_shards_of_the_stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> STAIRS_FROM_THE_BRICKS_FROM_THE_SHARDS_OF_THE_STONE_OF_HOPES = registerBlock("stairs_from_the_bricks_from_the_shards_of_the_stone_of_hopes",
            () -> new StairBlock(() -> BlockRegistry.SHARDS_OF_THE_STONE_OF_HOPES.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));



    public static final RegistryObject<Block> SMOOTH_STONE_OF_HOPES = registerBlock("smooth_stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> STAIRS_FROM_THE_SMOOTH_STONE_OF_HOPES = registerBlock("stairs_from_the_smooth_stone_of_hopes",
            () -> new StairBlock(() -> BlockRegistry.SMOOTH_STONE_OF_HOPES.get().defaultBlockState(),
            BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES = registerBlock("bricks_from_the_smooth_stone_of_hopes",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));

    public static final RegistryObject<Block> STAIRS_FROM_THE_BRICKS_FROM_THE_SMOOTH_STONE_OF_HOPES = registerBlock("stairs_from_the_bricks_from_the_smooth_stone_of_hopes",
            () -> new StairBlock(() -> BlockRegistry.SMOOTH_STONE_OF_HOPES.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).sound(SoundType.TUFF)));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private  static <T extends  Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ItemRegistry.Items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
