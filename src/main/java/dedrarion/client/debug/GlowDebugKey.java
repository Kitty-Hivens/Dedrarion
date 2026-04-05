package dedrarion.client.debug;

import dedrarion.client.render.OreGlowRenderer;
import dedrarion.client.render.OreHighlightTarget;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GlowDebugKey {

    private static final KeyMapping KEY = new KeyMapping(
            "key.dedrarion.glow_debug", GLFW.GLFW_KEY_F8, "key.categories.dedrarion"
    );
    private static boolean wasDown = false;

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Reg {
        @SubscribeEvent
        public static void onRegisterKeys(RegisterKeyMappingsEvent e) {
            e.register(KEY);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean down = KEY.isDown();
        if (down && !wasDown) {
            // 1. Принудительно добавляем тестовые хайлайты вокруг игрока
            BlockPos feet = mc.player.blockPosition();
            OreHighlightTarget.add(feet.offset( 4, 0,  0), OreHighlightTarget.COLOR_RUBY);
            OreHighlightTarget.add(feet.offset(-4, 0,  0), OreHighlightTarget.COLOR_EFTORIT);
            OreHighlightTarget.add(feet.offset( 0, 0,  4), OreHighlightTarget.COLOR_ETHEREUM);
            OreHighlightTarget.add(feet.offset( 0, 0, -4), OreHighlightTarget.COLOR_RUBY);
            OreHighlightTarget.add(feet.offset( 3, 2,  3), OreHighlightTarget.COLOR_EFTORIT);
            OreHighlightTarget.add(feet.offset(-3, 2, -3), OreHighlightTarget.COLOR_ETHEREUM);

            // 2. Дамп состояния рендерера в чат
            String status = OreGlowRenderer.debugStatus();
            mc.player.displayClientMessage(Component.literal("§b[GlowDebug] " + status), false);
            mc.player.displayClientMessage(
                Component.literal("§e[GlowDebug] entries=" + OreHighlightTarget.getEntries().size()), false);
        }
        wasDown = down;
    }
}
