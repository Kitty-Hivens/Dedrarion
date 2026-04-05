package dedrarion.content.item;

import dedrarion.client.render.OreHighlightTarget;
import dedrarion.content.util.ModTags;
import dedrarion.network.ModNetwork;
import dedrarion.network.OreHighlightPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The Magic Detector — a detection tool that reacts to nearby magical ores.
 *
 * <h3>Modes (switched via Shift + RMB)</h3>
 * <ul>
 *   <li><b>RAW</b> — common ores (Ruby, Eftorit). Warm orange highlight.</li>
 *   <li><b>ETHEREAL</b> — rare ores (Ethereum). Cold white/gold highlight.</li>
 *   <li><b>FULL</b> — all registered valuables. Mixed highlights.</li>
 * </ul>
 *
 * <h3>Detection (RMB)</h3>
 * Scans a cube of radius {@link #SCAN_RADIUS} around the player's eye position.
 * All matching blocks are highlighted via {@link OreHighlightTarget} for
 * {@link OreHighlightTarget#HIGHLIGHT_DURATION_MS} milliseconds.
 * A particle trail is sent via ServerLevel#sendParticles so it is
 * visible to all nearby players, not just the activating client.
 *
 * <p>TODO: passive firefly particles reacting to nearby ore count.
 * <p>TODO: puddle system for visible ores.
 */
public class MagicDetectorItem extends Item {

    private static final String TAG_MODE    = "DetectorMode";
    private static final int    SCAN_RADIUS = 16;

    // --- Detection modes ---

    public enum Mode {
        RAW     ("tooltip.dedrarion.magic_detector.mode_raw",      ChatFormatting.GOLD),
        ETHEREAL("tooltip.dedrarion.magic_detector.mode_ethereal", ChatFormatting.WHITE),
        FULL    ("tooltip.dedrarion.magic_detector.mode_full",     ChatFormatting.LIGHT_PURPLE);

        final String langKey;
        final ChatFormatting color;

        Mode(String langKey, ChatFormatting color) {
            this.langKey = langKey;
            this.color   = color;
        }

        Mode next() {
            Mode[] values = Mode.values();
            return values[(this.ordinal() + 1) % values.length];
        }
    }

    public MagicDetectorItem(Properties properties) {
        super(properties);
    }

    // --- Interaction ---

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Shift + RMB — cycle detection mode.
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                Mode next = getMode(stack).next();
                setMode(stack, next);
                player.displayClientMessage(
                        Component.translatable(next.langKey).withStyle(next.color), true
                );
            }
            return InteractionResultHolder.success(stack);
        }

        // RMB — scan and highlight (server only).
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            scan(serverLevel, player, stack);
        }

        return InteractionResultHolder.success(stack);
    }

    // --- Scan logic ---

    private void scan(ServerLevel level, Player player, ItemStack stack) {
        Vec3 eye = player.getEyePosition();
        BlockPos center = new BlockPos(
                (int) Math.floor(eye.x),
                (int) Math.floor(eye.y),
                (int) Math.floor(eye.z)
        );

        Mode mode = getMode(stack);
        List<OreHighlightPacket.Entry> found = new ArrayList<>();

        for (int x = -SCAN_RADIUS; x <= SCAN_RADIUS; x++) {
            for (int y = -SCAN_RADIUS; y <= SCAN_RADIUS; y++) {
                for (int z = -SCAN_RADIUS; z <= SCAN_RADIUS; z++) {
                    BlockPos pos     = center.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (matchesMode(state, mode)) {
                        found.add(new OreHighlightPacket.Entry(pos, resolveColor(state)));
                        spawnTrail(level, eye, pos);
                    }
                }
            }
        }

        if (!found.isEmpty()) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0f, 1.0f);

            if (player instanceof ServerPlayer serverPlayer) {
                ModNetwork.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new OreHighlightPacket(found)
                );
            }
        }
    }

    private boolean matchesMode(BlockState state, Mode mode) {
        return switch (mode) {
            case RAW      -> state.is(ModTags.Blocks.METAL_DETECTOR_RAW);
            case ETHEREAL -> state.is(ModTags.Blocks.METAL_DETECTOR_ETHEREAL);
            case FULL     -> state.is(ModTags.Blocks.METAL_DETECTOR_VALUABLES);
        };
    }

    private int resolveColor(BlockState state) {
        if (state.is(ModTags.Blocks.METAL_DETECTOR_ETHEREAL)) return OreHighlightTarget.COLOR_ETHEREUM;
        if (state.is(ModTags.Blocks.METAL_DETECTOR_RAW))      return OreHighlightTarget.COLOR_RUBY;
        return OreHighlightTarget.COLOR_ETHEREUM;
    }

    /**
     * Spawns a GLOW particle trail from the player's eye to the target block.
     * Starts at step 1 (not 0) to avoid spawning a particle directly in the player's face.
     * Uses ServerLevel#sendParticles so all nearby clients see it.
     */
    private void spawnTrail(ServerLevel level, Vec3 from, BlockPos to) {
        double dx   = to.getX() + 0.5 - from.x;
        double dy   = to.getY() + 0.5 - from.y;
        double dz   = to.getZ() + 0.5 - from.z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        int steps   = Math.max(1, (int) (dist / 1.5));

        // Start at i=1 to skip the particle directly at the player's eye.
        for (int i = 1; i <= steps; i++) {
            double t = (double) i / steps;
            level.sendParticles(
                    ParticleTypes.GLOW,
                    from.x + dx * t,
                    from.y + dy * t,
                    from.z + dz * t,
                    1, 0, 0, 0, 0
            );
        }
    }

    // --- Tooltip ---

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.item.dedrarion.magic_detector")
                .withStyle(ChatFormatting.AQUA));

        Mode mode = getMode(stack);
        tooltip.add(Component.translatable("tooltip.dedrarion.magic_detector.current_mode")
                .append(Component.translatable(mode.langKey).withStyle(mode.color)));

        tooltip.add(Component.translatable("tooltip.dedrarion.magic_detector.switch_hint")
                .withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(stack, level, tooltip, flag);
    }

    // --- NBT helpers ---

    public Mode getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(TAG_MODE)) return Mode.FULL;
        try {
            return Mode.valueOf(tag.getString(TAG_MODE));
        } catch (IllegalArgumentException e) {
            return Mode.FULL;
        }
    }

    private void setMode(ItemStack stack, Mode mode) {
        stack.getOrCreateTag().putString(TAG_MODE, mode.name());
    }
}
