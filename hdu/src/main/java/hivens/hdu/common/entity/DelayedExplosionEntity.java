package hivens.hdu.common.entity;

import hivens.hdu.common.registry.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DelayedExplosionEntity extends Entity {
    private static final EntityDataAccessor<Integer> FUSE = 
            SynchedEntityData.defineId(DelayedExplosionEntity.class, EntityDataSerializers.INT);
    
    // Сила взрыва 8.0F (очень большая, но без разрушения блоков)
    private static final float EXPLOSION_POWER = 8.0F; 
    private static final int INITIAL_FUSE = 60; // 3 секунды

    public DelayedExplosionEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
    }

    public DelayedExplosionEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ModEntityTypes.DELAYED_EXPLOSION.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.setFuse(INITIAL_FUSE);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FUSE, INITIAL_FUSE);
    }

    @Override
    public void tick() {
        super.tick();
        int fuse = this.getFuse();

        if (!this.level().isClientSide && fuse <= 0) {
            this.explode();
            this.discard();
        } else if (fuse > 0) {
            this.setFuse(fuse - 1);
            if (this.level().isClientSide) {
                // Визуальное предупреждение (Client-side)
                this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY(0.5), this.getZ(), 0.0, 0.0, 0.0);
                this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(0.5), this.getZ(), 0.0, 0.1, 0.0);
            }
        }
    }

    private void explode() {
        this.level().explode(
                null, // Нет поджигателя
                this.getX(),
                this.getY(0.5),
                this.getZ(),
                EXPLOSION_POWER,
                Level.ExplosionInteraction.NONE // Взрыв, который НЕ ЛОМАЕТ блоки
        );
    }

    public int getFuse() {
        return this.entityData.get(FUSE);
    }

    public void setFuse(int pFuse) {
        this.entityData.set(FUSE, pFuse);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        this.setFuse(pCompound.getInt("Fuse"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        pCompound.putInt("Fuse", this.getFuse());
    }
}