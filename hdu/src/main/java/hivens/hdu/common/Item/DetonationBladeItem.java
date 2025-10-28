package hivens.hdu.common.Item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DetonationBladeItem extends SwordItem {
    public DetonationBladeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        Level level = pAttacker.level();

        // --- ГЛАВНАЯ ЛОГИКА ---
        // Проверяем, что цель горит, и что мы на сервере
        if (pTarget.isOnFire() && !level.isClientSide) {
            // Создаём небольшой взрыв (сила 2.0F), который не ломает блоки
            level.explode(pAttacker, pTarget.getX(), pTarget.getY(0.5), pTarget.getZ(), 2.0F, Level.ExplosionInteraction.NONE);

            // Добавляем звуковые и визуальные эффекты для "сочности"
            level.playSound(null, pTarget.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.2F);
        }

        // Вызываем стандартное поведение меча (нанесение урона, потеря прочности)
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pIsAdvanced) {
        pTooltip.add(Component.translatable("tooltip.item.hdu.detonation_blade").withStyle(ChatFormatting.RED));
        super.appendHoverText(pStack, pLevel, pTooltip, pIsAdvanced);
    }
}