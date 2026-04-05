package dedrarion.network;

import dedrarion.client.render.OreHighlightTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Sent from server to client when the Magic Detector finds ore blocks.
 * Each entry carries a block position and a packed ARGB color.
 */
public class OreHighlightPacket {

    private final List<Entry> entries;

    public OreHighlightPacket(List<Entry> entries) {
        this.entries = entries;
    }

    // --- Serialization ---

    public static void encode(OreHighlightPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.entries.size());
        for (Entry e : packet.entries) {
            buf.writeBlockPos(e.pos());
            buf.writeInt(e.color());
        }
    }

    public static OreHighlightPacket decode(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            BlockPos pos = buf.readBlockPos();
            int color   = buf.readInt();
            entries.add(new Entry(pos, color));
        }
        return new OreHighlightPacket(entries);
    }

    // --- Handler ---

    public static void handle(OreHighlightPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            // Must run on the render thread.
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                packet.entries.forEach(e -> OreHighlightTarget.add(e.pos(), e.color()))
            )
        );
        ctx.get().setPacketHandled(true);
    }

    // --- Entry ---

    public record Entry(BlockPos pos, int color) {}
}
