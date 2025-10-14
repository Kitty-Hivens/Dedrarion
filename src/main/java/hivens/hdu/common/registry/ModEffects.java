package hivens.hdu.common.registry; // Убедись, что пакет правильный

import hivens.hdu.HDU;
import hivens.hdu.common.effect.BleedingEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HDU.MOD_ID);

    public static final RegistryObject<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding",
            () -> new BleedingEffect(MobEffectCategory.HARMFUL, 0x990000));

    // Добавляем {} для создания анонимного подкласса
    public static final RegistryObject<MobEffect> ARMOR_REDUCTION = MOB_EFFECTS.register("armor_reduction",
            () -> new MobEffect(MobEffectCategory.HARMFUL, 0x555555) {} // <-- Вот оно!
                    .addAttributeModifier(Attributes.ARMOR, "7f7c46a6-01d8-4a02-8531-54ab1370e0a8", -2.0D, AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<MobEffect> PHASE_ENERGY = MOB_EFFECTS.register("phase_energy",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x6495ED) {}); // <-- И здесь!

    public static final RegistryObject<MobEffect> RESONANCE = MOB_EFFECTS.register("resonance",
            () -> new MobEffect(MobEffectCategory.BENEFICIAL, 0x8A2BE2) {}); // <-- И здесь!

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}