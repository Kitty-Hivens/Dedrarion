package hivens.hdu.common.Item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MnemosyneAletaItem extends SwordItem {
    // Базовые параметры оружия
    private static final float BASE_DAMAGE = 6.0F;
    private static final float MNEMOSYNE_DAMAGE_MULTIPLIER = 1.0F;
    private static final float ALETA_DAMAGE_MULTIPLIER = 1.33F;

    public MnemosyneAletaItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Простой расчет урона - 50-100% от базового
        float attackProgress = (float) (0.5F + (Math.random() * 0.5F));

        // Урон Мнемозины с эффектом замедления
        float mnemosyneDamage = BASE_DAMAGE * MNEMOSYNE_DAMAGE_MULTIPLIER * attackProgress;
        if (mnemosyneDamage > 0) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                    (int)(40 * attackProgress), 4)); // Длительность и уровень замедления зависят от случайного прогресса

            target.hurt(attacker.damageSources().mobAttack(attacker), mnemosyneDamage);
        }

        // Урон Алеты с эффектом слабости
        float aletaDamage = BASE_DAMAGE * ALETA_DAMAGE_MULTIPLIER * attackProgress;
        if (aletaDamage > 0) {
            target.hurt(attacker.damageSources().mobAttack(attacker), aletaDamage);

            // Эффект слабости зависит от случайного прогресса
            if (attackProgress > 0.7F) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,
                        (int)(60 * attackProgress), 2)); // Длительность и уровень слабости
            }
        }

        return true;
    }

    // Метод для получения описания урона/эффектов
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, List<Component> tooltip, @NotNull TooltipFlag context) {
        tooltip.add(Component.literal("§9Мнемозина: Замедление при атаке"));
        tooltip.add(Component.literal("§cАлета: Урон с эффектом слабости"));
        tooltip.add(Component.literal("§7Урон зависит от случайного фактора"));
    }
}