package hivens.hdu.common.registry;

import hivens.hdu.HDU;
import hivens.hdu.common.entity.DelayedExplosionEntity;
import hivens.hdu.common.entity.NullGuardianEntity;
import hivens.hdu.common.entity.PrimedJokerBomb;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HDU.MOD_ID);

    public static final RegistryObject<EntityType<PrimedJokerBomb>> PRIMED_JOKER_BOMB =
            ENTITY_TYPES.register("primed_joker_bomb",
                    () -> EntityType.Builder.<PrimedJokerBomb>of(PrimedJokerBomb::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F) // Маленький невидимый хитбокс
                            .clientTrackingRange(10)
                            .updateInterval(10)
                            .build("primed_joker_bomb"));

    public static final RegistryObject<EntityType<DelayedExplosionEntity>> DELAYED_EXPLOSION =
            ENTITY_TYPES.register("delayed_explosion",
                    () -> EntityType.Builder.<DelayedExplosionEntity>of(DelayedExplosionEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .clientTrackingRange(16)
                            .build("delayed_explosion"));

    public static final RegistryObject<EntityType<NullGuardianEntity>> NULL_GUARDIAN =
            ENTITY_TYPES.register("null_guardian",
                    () -> EntityType.Builder.of(NullGuardianEntity::new, MobCategory.MONSTER) // Фабрика и категория
                            .sized(1.5f, 2.0f) // Размер хитбокса (ширина, высота)
                            .build("null_guardian"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}