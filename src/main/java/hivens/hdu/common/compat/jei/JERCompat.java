package hivens.hdu.common.compat.jei; // Убедитесь, что пакет правильный

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.registry.ModItems;
import jeresources.api.IJERAPI;
import jeresources.api.IWorldGenRegistry;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.api.JERAPI;
import net.minecraft.world.item.ItemStack;

public class JERCompat {
    public static void init() {
        IJERAPI jerApi = JERAPI.getInstance();
        IWorldGenRegistry worldGenRegistry = jerApi.getWorldGenRegistry();

        if (worldGenRegistry == null) {
            return; // Безопасный выход
        }

        // --- 1. Регистрация Этериума ---
        worldGenRegistry.register(
                new ItemStack(ModBlocks.ETHEREUM_ORE.get()), // <-- ИСПРАВЛЕНО
                new DistributionSquare(1, 3, -64, -50),
                new LootDrop(new ItemStack(ModItems.RAW_ETHEREUM.get()), 1, 1, Conditional.affectedByFortune) // <-- ИСПРАВЛЕНО
        );

        // --- 2. Регистрация Рубинов ---
        DistributionSquare rubyDistribution = new DistributionSquare(7, 7, -64, 80);
        LootDrop rubyDrop = new LootDrop(new ItemStack(ModItems.RUBY.get()), 1, 1, Conditional.affectedByFortune); // <-- ИСПРАВЛЕНО

        worldGenRegistry.register(new ItemStack(ModBlocks.RUBY_ORE.get()), rubyDistribution, rubyDrop); // <-- ИСПРАВЛЕНО
        worldGenRegistry.register(new ItemStack(ModBlocks.DEEPSLATE_RUBY_ORE.get()), rubyDistribution, rubyDrop); // <-- ИСПРАВЛЕНО

        // --- 3. Регистрация Эфторита ---
        DistributionSquare eftoritDistribution = new DistributionSquare(4, 4, -64, 40);
        LootDrop eftoritDrop = new LootDrop(new ItemStack(ModItems.EFTORIT.get()), 1, 1, Conditional.affectedByFortune); // <-- ИСПРАВЛЕНО

        worldGenRegistry.register(new ItemStack(ModBlocks.EFTORIT_ORE.get()), eftoritDistribution, eftoritDrop); // <-- ИСПРАВЛЕНО
        worldGenRegistry.register(new ItemStack(ModBlocks.DEEPSLATE_EFTORIT_ORE.get()), eftoritDistribution, eftoritDrop); // <-- ИСПРАВЛЕНО

        // --- 4. Регистрация Камня надежды ---
        worldGenRegistry.register(
                new ItemStack(ModBlocks.HOPE_STONE.get()), // <-- ИСПРАВЛЕНО
                new DistributionSquare(2, 20, -64, -30),
                new LootDrop(new ItemStack(ModBlocks.HOPE_STONE.get())) // <-- ИСПРАВЛЕНО
        );
    }
}