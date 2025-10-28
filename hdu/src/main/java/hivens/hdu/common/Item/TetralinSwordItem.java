package hivens.hdu.common.Item;

import hivens.dedrarion.api.registry.ModEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class TetralinSwordItem extends SwordItem {
    private static final int SURGE_COOLDOWN_TICKS = 6 * 20; // 6 секунд
    private static final String TAG_LAST_CRUSH_TIME = "LastCrushTime";

    public TetralinSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties props) {
        super(tier, attackDamageModifier, attackSpeedModifier, props);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (!(attacker instanceof Player player)) {
            return super.hurtEnemy(stack, target, attacker);
        }

        // Получаем необходимые параметры
        float charge = player.getAttackStrengthScale(0.5F);
        boolean isInAir = !player.onGround();
        boolean hasResonanceBuff = player.hasEffect(ModEffects.RESONANCE.get());

        // Определяем фазу и применяем эффекты
        Phase phase = determinePhase(isInAir, charge, hasResonanceBuff);
        applyPhaseEffects(phase, player, target, charge);

        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    private enum Phase { CRUSH, SLASH, SURGE }

    private Phase determinePhase(boolean inAir, double charge, boolean hasBuff) {
        if (inAir) return Phase.CRUSH;
        if (charge >= 0.95 || hasBuff) return Phase.SURGE;
        return Phase.SLASH;
    }

    private void applyPhaseEffects(Phase phase, Player player, LivingEntity target, float charge) {
        Level level = player.level();
        if (level.isClientSide) return; // Вся логика - на сервере
        
        DamageSource damageSource = level.damageSources().playerAttack(player);
        float baseDamage = this.getDamage();

        switch (phase) {
            case CRUSH -> {
                float damage = (baseDamage + 2.0f) * 1.6f * charge; // Усиленный урон
                target.hurt(damageSource, damage);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 4)); // Почти полный стан
                target.knockback(0.4F, player.getX() - target.getX(), player.getZ() - target.getZ());
                
                // Запоминаем время для комбо
                player.getPersistentData().putLong(TAG_LAST_CRUSH_TIME, level.getGameTime());
                
                // Эффекты
                level.playSound(null, target.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0f, 0.8f);
                ((ServerLevel) level).sendParticles(ParticleTypes.CRIT, target.getX(), target.getEyeY(), target.getZ(), 15, 0.5, 0.5, 0.5, 0.2);
            }
            case SLASH -> {
                float damage = (baseDamage + 2.0f) * charge;
                target.hurt(damageSource, damage);
                target.addEffect(new MobEffectInstance(ModEffects.BLEEDING.get(), 80, 0));
                target.addEffect(new MobEffectInstance(ModEffects.ARMOR_REDUCTION.get(), 60, 0));
                
                // Эффекты
                level.playSound(null, target.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0f, 1.2f);
            }
            case SURGE -> {
                if (player.getCooldowns().isOnCooldown(this)) return; // Если в кулдауне, ничего не делаем

                float damage = (baseDamage + 2.0f) * 0.8f * charge;
                
                // Проверка комбо
                long lastCrushTime = player.getPersistentData().getLong(TAG_LAST_CRUSH_TIME);
                boolean isCombo = (level.getGameTime() - lastCrushTime) < 50; // 2.5 секунды
                
                if (isCombo) {
                    damage *= 1.25f; // Усиление урона от комбо
                    player.getCooldowns().addCooldown(this, SURGE_COOLDOWN_TICKS / 2); // Уменьшаем кулдаун
                    player.getPersistentData().remove(TAG_LAST_CRUSH_TIME); // Сбрасываем таймер комбо
                } else {
                    player.getCooldowns().addCooldown(this, SURGE_COOLDOWN_TICKS);
                }

                target.hurt(damageSource, damage);

                // AOE урон
                List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class,
                        target.getBoundingBox().inflate(3.0), e -> e != player && e.isAlive() && e != target);
                for (LivingEntity entity : list) {
                    entity.hurt(damageSource, damage * 0.5f);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
                }

                player.addEffect(new MobEffectInstance(ModEffects.PHASE_ENERGY.get(), 40, 0));

                // Эффекты
                level.playSound(null, target.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 1.0f, 1.5f);
                ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(), target.getZ(), 30, 1.0, 1.0, 1.0, 0.5);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        tooltip.add(Component.translatable("tooltip.hdu.tetralin.desc").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, context);
    }
}