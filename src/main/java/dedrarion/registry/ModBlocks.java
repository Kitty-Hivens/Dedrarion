package dedrarion.registry;

import dedrarion.Dedrarion;
import dedrarion.content.block.EftoritForgeBlock;
import dedrarion.content.block.PedestalBlock;
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

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Dedrarion.MOD_ID);

    // Ethereum
    public static final RegistryObject<Block> ETHEREUM_ORE = registerBlock("ethereum_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE).sound(SoundType.NETHER_GOLD_ORE)
                    .strength(120f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(1, 1))
    );

    public static final RegistryObject<Block> ETHEREUM_BLOCK = registerBlock("ethereum_block",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.OBSIDIAN)
                    .sound(SoundType.NETHER_GOLD_ORE)
                    .strength(180f)
                    .requiresCorrectToolForDrops())
    );

    public static final RegistryObject<Block> RAW_ETHEREUM_BLOCK = registerBlock("raw_ethereum_block",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.IRON_ORE)
                    .sound(SoundType.TUFF))
    );


    // Ruby

    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE).sound(SoundType.METAL)
                    .strength(15f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(1, 3))
    );

    public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties
                    .copy(Blocks.LAPIS_ORE)
                    .sound(SoundType.METAL)
                    .strength(25f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(1, 3))
    );

    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block",
            () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.LAPIS_ORE)
                    .sound(SoundType.METAL)
                    .strength(40f)
                    .requiresCorrectToolForDrops())
    );

    // Eftorit

    public static  final RegistryObject<Block> EFTORIT_ORE = registerBlock("eftorit_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE)
                    .sound(SoundType.METAL)
                    .strength(25f)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 5))
    );

    public static  final RegistryObject<Block> DEEPSLATE_EFTORIT_ORE = registerBlock("deepslate_eftorit_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_ORE)
                    .sound(SoundType.METAL)
                    .strength(40f)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 5))
    );

    public static  final  RegistryObject<Block> EFTORIT_BLOCK = registerBlock("eftorit_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK)
                    .sound(SoundType.METAL)
                    .strength(40f)
                    .requiresCorrectToolForDrops())
    );

    public static  final  RegistryObject<Block> EFTORIT_FORGE = registerBlock("eftorit_forge",
            () -> new EftoritForgeBlock(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK)
                    .sound(SoundType.METAL)
                    .strength(120f)
                    .requiresCorrectToolForDrops())
    );

    // Eftorium
    public static  final  RegistryObject<Block> EFTORIUM_BLOCK = registerBlock("eftorium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK)
                    .sound(SoundType.METAL)
                    .strength(40f)
                    .requiresCorrectToolForDrops())
    );

    // Hope Stone

    public static final RegistryObject<Block> HOPE_STONE = registerBlock("hope_stone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .sound(SoundType.TUFF)
                    .strength(20)
                    .requiresCorrectToolForDrops())
    );

    public static final RegistryObject<Block> SMOOTH_HOPE_STONE = registerBlock("smooth_hope_stone",
            () -> new Block(BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<Block> HOPE_SHARDS = registerBlock("hope_shards",
            () -> new Block(BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    // Hope Stone Bricks

    public static final RegistryObject<Block> HOPE_STONE_BRICKS = registerBlock("hope_stone_bricks",
            () -> new Block(BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<Block> SMOOTH_HOPE_STONE_BRICKS = registerBlock("smooth_hope_bricks",
            () -> new Block(BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<Block> HOPE_SHARD_BRICKS = registerBlock("hope_shard_bricks",
            () -> new Block(BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    // Hope Stone Stairs
    public static final RegistryObject<StairBlock> HOPE_STONE_STAIRS = registerBlock("hope_stone_stairs",
            () -> new StairBlock(() -> ModBlocks.HOPE_STONE.get().defaultBlockState(), BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<StairBlock> SMOOTH_HOPE_STONE_STAIRS = registerBlock("smooth_hope_stairs",
            () -> new StairBlock(() -> ModBlocks.SMOOTH_HOPE_STONE.get().defaultBlockState(), BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<StairBlock> HOPE_SHARD_STAIRS = registerBlock("hope_shard_stairs",
            () -> new StairBlock(() -> ModBlocks.HOPE_SHARDS.get().defaultBlockState(), BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    // Hope Bricks Stairs
    public static final RegistryObject<StairBlock> HOPE_BRICK_STAIRS = registerBlock("hope_brick_stairs",
            () -> new StairBlock(() -> ModBlocks.HOPE_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<StairBlock> SMOOTH_HOPE_BRICK_STAIRS = registerBlock("smooth_hope_brick_stairs",
            () -> new StairBlock(() -> ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static final RegistryObject<StairBlock> HOPE_SHARD_BRICK_STAIRS = registerBlock("hope_shard_brick_stairs",
            () -> new StairBlock(() -> ModBlocks.HOPE_SHARD_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
                    .copy(ModBlocks.HOPE_STONE.get()))
    );

    public static  final  RegistryObject<Block> PEDESTAL = registerBlock("pedestal",
            () -> new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .sound(SoundType.METAL)
                    .instabreak()
            )
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private  static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.Items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
