package Hivens.hdu.Common.compat.jei;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.Common.Registry.ItemRegistry;
import jeresources.api.IDungeonRegistry;
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
        IDungeonRegistry dungeonRegistry = jerApi.getDungeonRegistry();
        if (dungeonRegistry!=null) {

        }
        
        IWorldGenRegistry worldGenRegistry = jerApi.getWorldGenRegistry();
        if (worldGenRegistry!=null){
            worldGenRegistry.register(
                    new ItemStack(BlockRegistry.ETHEREUM_ORE.get()),
                    new DistributionSquare(4, 1, -64, -50),
                    new LootDrop(new ItemStack(ItemRegistry.RAW_ETHEREUM.get()), 1, 1,
                            Conditional.affectedByFortune));
        }
    }
}
