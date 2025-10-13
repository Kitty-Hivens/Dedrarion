package hivens.hdu.common.util;

import hivens.hdu.common.registry.ItemRegistry;
import hivens.hdu.HDU;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HDU.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FoodParticleHandler {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        Player player = event.player;


        if (player.getUseItem().getItem() == ItemRegistry.FORBIDDEN_FRUIT.get()) {
            tickCounter++;

            if (tickCounter >= 15) {
                for (int i = 0; i < 5; i++) {
                    player.level().addParticle(
                            ParticleTypes.END_ROD,
                            player.getX() + (player.level().random.nextDouble() - 0.5) * 0.5,
                            player.getY() + 1.5,
                            player.getZ() + (player.level().random.nextDouble() - 0.5) * 0.5,
                            0.0, 0.0, 0.0
                    );
                }
                tickCounter = 0;
            }
        } else {
            tickCounter = 0;
        }
    }
}
