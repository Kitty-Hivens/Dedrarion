package Hivens.hdu.Client;

import Hivens.hdu.Common.Custom.Block.Entity.EftoritForgeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTickHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;

        if (level != null) {
            for (int x = level.getMinSection(); x < level.getMaxSection(); x++) {
                for (int z = level.getMinSection(); z < level.getMaxSection(); z++) {
                    LevelChunk chunk = level.getChunkSource().getChunk(x, z, false);
                    if (chunk != null) {
                        for (BlockEntity be : chunk.getBlockEntities().values()) {
                            if (be instanceof EftoritForgeEntity forge) {
                                forge.clientTick();
                            }
                        }
                    }
                }
            }
        }


    }
}
