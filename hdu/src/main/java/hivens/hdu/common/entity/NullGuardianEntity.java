package hivens.hdu.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;

public class NullGuardianEntity extends Monster implements GeoEntity, RangedAttackMob {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Внутренние цели для управления фазами
    private RangedAttackGoal rangedAttackGoal;
    private AvoidEntityGoal<Player> avoidPlayerGoal;
    private MeleeAttackGoal meleeAttackGoal;

    // Состояние босса
    private int phase = 1;
    private int rangedAttackCooldown = 0; // NEW: Поле для кулдауна атаки

    public NullGuardianEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    // --- Фазовый ИИ и Логика ---
    @Override
    protected void registerGoals() {
        // Базовые цели
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));

        // Инициализация целей. RangedAttackGoal будет срабатывать каждый тик (интервал 1),
        // а фактический кулдаун мы контролируем в performRangedAttack.
        this.rangedAttackGoal = new RangedAttackGoal(this, 1.0D, 1, 15.0F);
        this.avoidPlayerGoal = new AvoidEntityGoal<>(this, Player.class, 10.0F, 1.0D, 1.5D);
        this.meleeAttackGoal = new MeleeAttackGoal(this, 1.5D, false);

        // Фаза I (Начальная/Дальний бой)
        this.goalSelector.addGoal(1, this.avoidPlayerGoal);
        this.goalSelector.addGoal(2, this.rangedAttackGoal);
    }

    // Логика переключения фаз, вызываемая каждый тик
    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            float healthRatio = this.getHealth() / this.getMaxHealth();
            this.handlePhases(healthRatio);

            // Снижение кулдауна
            if (this.rangedAttackCooldown > 0) {
                this.rangedAttackCooldown--;
            }
        }
    }

    private void handlePhases(float healthRatio) {
        int newPhase;
        if (healthRatio <= 0.10F) {
            newPhase = 3; // Отчаяние
        } else if (healthRatio <= 0.50F) {
            newPhase = 2; // Хаос
        } else {
            newPhase = 1; // Дальний бой
        }

        if (newPhase != this.phase) {
            this.phase = newPhase;

            // 1. Сначала убираем ВСЕ фазовые цели
            this.goalSelector.removeGoal(this.avoidPlayerGoal);
            this.goalSelector.removeGoal(this.rangedAttackGoal);
            this.goalSelector.removeGoal(this.meleeAttackGoal);

            // 2. Добавляем цели согласно новой фазе и устанавливаем скорость
            if (this.phase == 3) {
                // ФАЗА III: Отчаяние (Ближний бой)
                this.goalSelector.addGoal(1, this.meleeAttackGoal);
                Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.40D); // Ускорение
                Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.0D); // Увеличенный урон
            } else {
                // ФАЗА I/II: Дальний бой/Хаос
                this.goalSelector.addGoal(1, this.avoidPlayerGoal); // P1
                this.goalSelector.addGoal(2, this.rangedAttackGoal); // P2

                if (this.phase == 2) {
                    // ФАЗА II: Хаос
                    Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.35D); // Средняя скорость
                } else {
                    // ФАЗА I: Дальний бой
                    Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.25D); // Базовая скорость
                }
                Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(6.0D); // Сброс урона
            }
        }
    }


    // --- Метод для атаки на расстоянии (бросаем JokerBomb) ---
    // Теперь контролируется внутренним кулдауном
    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pVelocity) {
        int attackInterval = (this.phase == 2) ? 20 : 60; // 1 сек в P2, 3 сек в P1

        if (this.rangedAttackCooldown > 0) {
            return; // Ждем кулдаун
        }

        PrimedJokerBomb bomb = new PrimedJokerBomb(this.level(), this.getX(), this.getEyeY(), this.getZ(), this);

        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - bomb.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));

        // Метод shoot теперь существует в PrimedJokerBomb
        bomb.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 12.0F);

        this.level().addFreshEntity(bomb);
        this.rangedAttackCooldown = attackInterval; // Устанавливаем кулдаун после выстрела
    }

    // --- Массивный взрыв после смерти ---
    @Override
    public void die(net.minecraft.world.damagesource.@NotNull DamageSource pCause) {
        if (!this.level().isClientSide) {
            // Создаем сущность отложенного взрыва на месте смерти
            DelayedExplosionEntity explosion = new DelayedExplosionEntity(
                    this.level(), this.getX(), this.getY(), this.getZ()
            );
            this.level().addFreshEntity(explosion);
        }
        super.die(pCause);
    }


    // --- Методы GeckoLib ---
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        // Оставим это на 4-й этап, когда у нас будет модель и анимации.
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}