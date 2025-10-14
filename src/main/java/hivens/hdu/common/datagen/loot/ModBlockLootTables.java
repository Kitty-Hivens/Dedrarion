package hivens.hdu.common.datagen.loot;

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.registry.ModItems;
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

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.ETHEREUM_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_ETHEREUM_BLOCK.get());

        this.dropSelf(ModBlocks.RUBY_BLOCK.get());
        this.dropSelf(ModBlocks.EFTORIT_BLOCK.get());
        this.dropSelf(ModBlocks.EFTORIT_FORGE.get());

        this.dropSelf(ModBlocks.SMOOTH_HOPE_STONE.get());
        this.dropSelf(ModBlocks.HOPE_SHARDS.get());

        this.dropSelf(ModBlocks.HOPE_STONE_BRICKS.get());
        this.dropSelf(ModBlocks.SMOOTH_HOPE_STONE_BRICKS.get());
        this.dropSelf(ModBlocks.HOPE_SHARD_BRICKS.get());

        this.dropSelf(ModBlocks.HOPE_STONE_STAIRS.get());
        this.dropSelf(ModBlocks.SMOOTH_HOPE_STONE_STAIRS.get());
        this.dropSelf(ModBlocks.HOPE_SHARD_STAIRS.get());

        this.dropSelf(ModBlocks.HOPE_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.SMOOTH_HOPE_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.HOPE_SHARD_BRICK_STAIRS.get());

        this.dropSelf(ModBlocks.PEDESTAL.get());
        this.dropSelf(ModBlocks.BROKEN_PLANKS.get());
        this.dropSelf(ModBlocks.EXTINGUISHED_TORCH.get());


        this.add(ModBlocks.ETHEREUM_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.ETHEREUM_ORE.get(), ModItems.RAW_ETHEREUM.get()));
        this.add(ModBlocks.RUBY_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.RUBY_ORE.get(), ModItems.RUBY.get()));
        this.add(ModBlocks.DEEPSLATE_RUBY_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.RUBY_ORE.get(), ModItems.RUBY.get()));
        this.add(ModBlocks.RUBY_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.RUBY_ORE.get(), ModItems.RUBY.get()));
        this.add(ModBlocks.DEEPSLATE_RUBY_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.RUBY_ORE.get(), ModItems.RUBY.get()));
        this.add(ModBlocks.EFTORIT_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.EFTORIT_ORE.get(), ModItems.EFTORIT.get()));
        this.add(ModBlocks.DEEPSLATE_EFTORIT_ORE.get(),
                block -> createRareLikeOreDrops(ModBlocks.EFTORIT_ORE.get(), ModItems.EFTORIT.get()));
        this.add(ModBlocks.HOPE_STONE.get(),
                block -> createLikeStoneDrops(ModBlocks.HOPE_STONE.get(), ModBlocks.HOPE_SHARDS.get()));
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
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
