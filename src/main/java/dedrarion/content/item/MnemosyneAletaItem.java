package dedrarion.content.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dedrarion.config.ModConfigValues;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Mnemosyne and Aleta — a dual-natured artifact sword.
 *
 * <p>This is a transitional implementation. The full redesign is documented
 * in {@code MnemosyneAleta_concept.md}. Current behaviour preserves the
 * original day/night mode switching and essence accumulation while the
 * new scale-based system is being built.
 *
 * <p>TODO: Replace with scale [-2500, 2500] system, four phases
 * (Inactive / Aleta / Mnemosyne / Eclipse), chest drift via timestamp NBT,
 * and animated wave tooltip bar.
 */
public class MnemosyneAletaItem extends SwordItem {

    private static final String TAG_ESSENCE = "MemoryEssence";
    private static final String TAG_MODE    = "CurrentMode";
    private static final String MODE_DAY    = "Aleta";
    private static final String MODE_NIGHT  = "Mnemosyne";

    public MnemosyneAletaItem(Tier tier, int attackDamageModifier, float attackSpeedModifier,
                              Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // --- Config accessors ---

    private static int maxEssence() {
        return ModConfigValues.mnemosyneMaxEssence.get();
    }

    private static int aletaCooldown() {
        return ModConfigValues.mnemosyneAletaCooldown.get();
    }

    private static int essenceDecayRate() {
        return ModConfigValues.mnemosyneEssenceDecayRate.get();
    }

    // --- Attribute modifiers ---

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot,
                                                                        ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(
                super.getAttributeModifiers(slot, stack)
        );

        if (slot == EquipmentSlot.MAINHAND && getMode(stack).equals(MODE_NIGHT)) {
            int essence = getEssence(stack);
            if (essence > 0) {
                modifiers.removeAll(Attributes.ATTACK_DAMAGE);
                modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                        BASE_ATTACK_DAMAGE_UUID,
                        "Weapon modifier",
                        this.getDamage() + essence * 0.5f,
                        AttributeModifier.Operation.ADDITION
                ));
            }
        }

        return modifiers;
    }

    // --- Mode switching and essence decay ---

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level,
                              @NotNull Entity entity, int slot, boolean selected) {
        if (level.isClientSide) return;

        CompoundTag tag = stack.getOrCreateTag();
        String worldMode = level.isDay() ? MODE_DAY : MODE_NIGHT;

        // Sync stored mode with current day/night cycle.
        if (!Objects.equals(tag.getString(TAG_MODE), worldMode)) {
            tag.putString(TAG_MODE, worldMode);
        }

        if (worldMode.equals(MODE_NIGHT) && level.getGameTime() % essenceDecayRate() == 0) {
            addEssence(stack, -1);
        }
    }

    // --- Combat ---

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target,
                             @NotNull LivingEntity attacker) {
        if (!(attacker instanceof Player player)) {
            return super.hurtEnemy(stack, target, attacker);
        }

        @SuppressWarnings("resource")
        Level level = attacker.level();
        float base = this.getDamage();

        if (level.isDay()) {
            // Aleta mode — split physical/magic damage.
            target.hurt(player.damageSources().playerAttack(player), base * 0.75f);
            target.hurt(player.damageSources().magic(), base * 0.25f);
        } else {
            // Mnemosyne mode — bonus from essence, applies Wither.
            float bonus = getEssence(stack) * 0.5f;
            target.hurt(player.damageSources().playerAttack(player), base + bonus);
            if (getEssence(stack) > 0) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
            }
        }

        // Gain essence on kill (night only).
        if (!target.isAlive() && !level.isClientSide && !level.isDay()) {
            addEssence(stack, 1);
        }

        stack.hurtAndBreak(1, attacker, e -> e.broadcastBreakEvent(attacker.getUsedItemHand()));
        return true;
    }

    // --- Active ability (Aleta) ---

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isDay() || player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            // Wave of Insight — highlight all nearby enemies.
            level.getEntitiesOfClass(LivingEntity.class,
                            new AABB(player.blockPosition()).inflate(10),
                            e -> e != player && e.isAlive())
                    .forEach(e -> e.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0)));

            player.getCooldowns().addCooldown(this, aletaCooldown());
        }

        return InteractionResultHolder.success(stack);
    }

    // --- Tooltip ---

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        String mode = getMode(stack);
        if (mode.isEmpty()) {
            mode = (level != null && level.isDay()) ? MODE_DAY : MODE_NIGHT;
        }

        if (mode.equals(MODE_DAY)) {
            tooltip.add(Component.translatable("tooltip.dedrarion.mnemosyne_aleta.aleta_mode")
                    .withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("tooltip.dedrarion.mnemosyne_aleta.aleta_desc1")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.dedrarion.mnemosyne_aleta.aleta_desc2")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            int essence = getEssence(stack);
            tooltip.add(Component.translatable("tooltip.dedrarion.mnemosyne_aleta.mnemosyne_mode")
                    .withStyle(ChatFormatting.DARK_PURPLE));
            // TODO: Replace with animated wave bar (ITooltipComponent) in full redesign.
            tooltip.add(Component.translatable("tooltip.dedrarion.mnemosyne_aleta.essence",
                    essence, maxEssence()).withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("tooltip.dedrarion.mnemosyne_aleta.mnemosyne_desc")
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return getMode(stack).equals(MODE_NIGHT) && getEssence(stack) == maxEssence();
    }

    // --- NBT helpers ---

    private String getMode(ItemStack stack) {
        return stack.getOrCreateTag().getString(TAG_MODE);
    }

    private int getEssence(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TAG_ESSENCE);
    }

    private void addEssence(ItemStack stack, int amount) {
        CompoundTag tag = stack.getOrCreateTag();
        int current = tag.getInt(TAG_ESSENCE);
        tag.putInt(TAG_ESSENCE, Math.max(0, Math.min(maxEssence(), current + amount)));
    }
}
