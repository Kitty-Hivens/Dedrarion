package Hivens.hdu.Common.entities.TheDestroyedMechanism;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LaserProjectile extends Projectile {
    private float damage; // Урон лазера
    private LivingEntity target; // Цель для направляемого лазера

    public LaserProjectile(EntityType<? extends Projectile> type, Level world) {
        super(type, world);
    }

    // Конструктор с указанием цели и урона
    public LaserProjectile(Level world, LivingEntity shooter, LivingEntity target, float damage) {
        super(EntityType.ARROW, world); // Замените на свой EntityType
        this.setOwner(shooter);
        this.target = target;
        this.damage = damage;
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ()); // Устанавливаем начальную позицию
    }

    @Override
    public void tick() {
        super.tick();

        if (target != null && target.isAlive()) {
            Vec3 targetPos = target.position();
            Vec3 currentPos = this.position();

            // Вычисляем направление к цели
            Vec3 motion = targetPos.subtract(currentPos).normalize().scale(0.5); // Скорость можно настроить
            this.setDeltaMovement(motion);
        }

        // Движение снаряда
        Vec3 movement = this.getDeltaMovement();
        this.setPos(
                this.getX() + movement.x,
                this.getY() + movement.y,
                this.getZ() + movement.z
        );

        // Логика для проверки столкновения и удаления снаряда
        if (this.tickCount > 100) { // Например, через 5 секунд (100 тиков)
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityResult = (EntityHitResult) result;
            if (entityResult.getEntity() instanceof LivingEntity hitEntity) {
                hitEntity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), damage);
            }
        }
        this.discard();
    }

    @Override
    protected void defineSynchedData() {
        // Необходимый метод для синхронизации данных
    }

    // Геттеры и сеттеры
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }
}