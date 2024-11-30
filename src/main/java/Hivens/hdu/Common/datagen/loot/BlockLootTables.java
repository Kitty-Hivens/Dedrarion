package Hivens.hdu.Common.datagen.loot;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.Common.Registry.ItemRegistry;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {

    public BlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.ETHEREUM_BLOCK.get());
        this.dropSelf(BlockRegistry.RAW_ETHEREUM_BLOCK.get());

        this.dropSelf(BlockRegistry.RUBY_BLOCK.get());
        this.dropSelf(BlockRegistry.EFTORIT_BLOCK.get());
        this.dropSelf(BlockRegistry.EFTORIT_FORGE.get());

        this.dropSelf(BlockRegistry.SMOOTH_HOPE_STONE.get());
        this.dropSelf(BlockRegistry.HOPE_SHARDS.get());

        this.dropSelf(BlockRegistry.HOPE_STONE_BRICKS.get());
        this.dropSelf(BlockRegistry.SMOOTH_HOPE_STONE_BRICKS.get());
        this.dropSelf(BlockRegistry.HOPE_SHARD_BRICKS.get());

        this.dropSelf(BlockRegistry.HOPE_STONE_STAIRS.get());
        this.dropSelf(BlockRegistry.SMOOTH_HOPE_STONE_STAIRS.get());
        this.dropSelf(BlockRegistry.HOPE_SHARD_STAIRS.get());

        this.dropSelf(BlockRegistry.HOPE_BRICK_STAIRS.get());
        this.dropSelf(BlockRegistry.SMOOTH_HOPE_BRICK_STAIRS.get());
        this.dropSelf(BlockRegistry.HOPE_SHARD_BRICK_STAIRS.get());


        this.add(BlockRegistry.ETHEREUM_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.ETHEREUM_ORE.get(), ItemRegistry.RAW_ETHEREUM.get()));
        this.add(BlockRegistry.RUBY_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.RUBY_ORE.get(), ItemRegistry.RUBY.get()));
        this.add(BlockRegistry.DEEPSLATE_RUBY_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.RUBY_ORE.get(), ItemRegistry.RUBY.get()));
        this.add(BlockRegistry.RUBY_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.RUBY_ORE.get(), ItemRegistry.RUBY.get()));
        this.add(BlockRegistry.DEEPSLATE_RUBY_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.RUBY_ORE.get(), ItemRegistry.RUBY.get()));
        this.add(BlockRegistry.EFTORIT_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.EFTORIT_ORE.get(), ItemRegistry.EFTORIT.get()));
        this.add(BlockRegistry.DEEPSLATE_EFTORIT_ORE.get(),
                block -> createRareLikeOreDrops(BlockRegistry.EFTORIT_ORE.get(), ItemRegistry.EFTORIT.get()));
        this.add(BlockRegistry.HOPE_STONE.get(),
                block -> createLikeStoneDrops(BlockRegistry.HOPE_STONE.get(), BlockRegistry.HOPE_SHARDS.get()));
    }


    protected LootTable.Builder createRareLikeOreDrops(@NotNull Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(ApplyBonusCount
                                        .addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    protected LootTable.Builder createLikeStoneDrops(@NotNull Block pBlock, Block block) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(block)));
    }


    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
