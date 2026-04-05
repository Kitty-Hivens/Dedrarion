package dedrarion.network;

import dedrarion.Dedrarion;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        ResourceLocation.fromNamespaceAndPath(Dedrarion.MOD_ID, "main"),
        () -> PROTOCOL,
        PROTOCOL::equals,
        PROTOCOL::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.messageBuilder(OreHighlightPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(OreHighlightPacket::encode)
            .decoder(OreHighlightPacket::decode)
            .consumerMainThread(OreHighlightPacket::handle)
            .add();
    }
}
