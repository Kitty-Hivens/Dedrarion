package hivens.hdu.common.entity; // Убедись, что пакет правильный

import hivens.hdu.common.registry.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NullGuardianEntity extends Monster implements GeoEntity {
    // Это специальный объект от GeckoLib для кэширования данных анимации. Просто создаём его.
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NullGuardianEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * Здесь мы определяем базовые характеристики нашего моба.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D) // Здоровье (как у Эндермена)
                .add(Attributes.MOVEMENT_SPEED, 0.25D) // Скорость (пока не важна, т.к. он будет летать)
                .add(Attributes.ATTACK_DAMAGE, 3.0D) // Урон в ближнем бою (для фазы отчаяния)
                .add(Attributes.FOLLOW_RANGE, 35.0D); // Дальность, на которой он вас заметит
    }

    // --- Искусственный интеллект ---
    @Override
    protected void registerGoals() {
        super.registerGoals();
        // ПОКА ЗДЕСЬ ПУСТО. Сюда мы добавим его поведение (полёт, атака) на 4-м этапе.
    }


    // --- Методы для GeckoLib (Анимация) ---

    // Этот метод связывает нашу сущность с анимациями.
    // Пока мы просто создаём пустой контроллер, который ничего не делает.
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        // Оставим это на 4-й этап, когда у нас будет модель и анимации.
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}