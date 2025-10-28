package hivens.dedrarion.api.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {
    public BleedingEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        // Каждую секунду наносим 1 урон, игнорирующий броню
        pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1.0F);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        // Эффект срабатывает каждые 20 тиков (1 секунда)
        return pDuration % 20 == 0;
    }
}