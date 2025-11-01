package hivens.hdu.common.entity;

import hivens.hdu.common.registry.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PrimedJokerBomb extends Entity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedJokerBomb.class, EntityDataSerializers.INT);
    @Nullable
    private LivingEntity owner;

    public PrimedJokerBomb(EntityType<? extends PrimedJokerBomb> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.blocksBuilding = true;
    }

    public PrimedJokerBomb(Level pLevel, double pX, double pY, double pZ, @Nullable LivingEntity pOwner) {
        this(ModEntityTypes.PRIMED_JOKER_BOMB.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.owner = pOwner;
        this.setFuse(40); // 2 секунды (40 тиков)
    }

    // ИСПРАВЛЕНИЕ: Добавлен метод shoot
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)pInaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)pInaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)pInaccuracy).scale(pVelocity);
        this.setDeltaMovement(vec3);
        float f = Mth.sqrt((float) (vec3.x * vec3.x + vec3.z * vec3.z));
        this.setYRot( (float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot( (float)(Mth.atan2(vec3.y, f) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);
        if (fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            }
        }
    }

    private void explode() {
        // Взрыв силой 3, который не ломает блоки и НЕ наносит урон владельцу (т.к. он передан как igniter)
        this.level().explode(this.owner, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
    }

    // --- Стандартные методы для сущности ---
    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FUSE_ID, 40);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public void setFuse(int pFuse) {
        this.entityData.set(DATA_FUSE_ID, pFuse);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag pCompound) {}
    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag pCompound) {}
}