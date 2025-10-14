package hivens.hdu.common.Item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class MnemosyneAletaItem extends SwordItem {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String TAG_MEMORY_ESSENCE = "MemoryEssence";
    private static final String TAG_MODE = "CurrentMode";
    private static final String MODE_DAY = "Aleta";
    private static final String MODE_NIGHT = "Mnemosyne";
    private static final int MAX_ESSENCE = 10;
    private static final int ALETA_COOLDOWN_TICKS = 300; // 15 секунд

    public MnemosyneAletaItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // --- Отображение динамического урона ---
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        // Получаем оригинальный, НЕИЗМЕНЯЕМЫЙ multimap
        Multimap<Attribute, AttributeModifier> defaultModifiers = super.getAttributeModifiers(slot, stack);
        // Создаем НОВУЮ, ИЗМЕНЯЕМУЮ копию
        Multimap<Attribute, AttributeModifier> mutableModifiers = HashMultimap.create(defaultModifiers);

        if (slot == EquipmentSlot.MAINHAND) {
            String currentMode = stack.getOrCreateTag().getString(TAG_MODE);

            if (currentMode.equals(MODE_NIGHT)) {
                int essence = getMemoryEssence(stack);
                if (essence > 0) {
                    float bonusDamage = essence * 0.5f;
                    // Безопасно работаем с ИЗМЕНЯЕМОЙ копией
                    mutableModifiers.removeAll(Attributes.ATTACK_DAMAGE);
                    mutableModifiers.put(Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage() + bonusDamage, AttributeModifier.Operation.ADDITION));
                }
            }
        }
        // Возвращаем нашу измененную копию
        return mutableModifiers;
    }

    // --- Надежная смена режима и затухание зарядов ---
    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        // Вся логика теперь на стороне СЕРВЕРА
        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            String storedMode = tag.getString(TAG_MODE);
            String currentMode = level.isDay() ? MODE_DAY : MODE_NIGHT;

            // Если режим в мире отличается от того, что записано в мече, обновляем NBT
            if (!Objects.equals(storedMode, currentMode)) {
                tag.putString(TAG_MODE, currentMode);
            }

            // Логика затухания зарядов
            if (currentMode.equals(MODE_NIGHT)) {
                int essence = getMemoryEssence(stack);
                if (essence > 0 && level.getGameTime() % 200 == 0) { // Каждые 10 секунд
                    addMemoryEssence(stack, -1);
                }
            }
        }
    }

    // --- Основная логика атаки и счётчик убийств ---
    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (!(attacker instanceof Player player)) {
            return super.hurtEnemy(stack, target, attacker);
        }

        Level level = attacker.level();
        float baseDamage = this.getDamage();

        if (level.isDay()) {
            target.invulnerableTime = 0;
            target.hurt(player.damageSources().playerAttack(player), baseDamage * 0.75f);
            target.hurt(player.damageSources().magic(), baseDamage * 0.25f);
        } else {
            int essence = getMemoryEssence(stack);
            float bonusDamage = essence * 0.5f;
            target.hurt(player.damageSources().playerAttack(player), baseDamage + bonusDamage);
            if (essence > 0) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
            }
        }

        // Проверяем, жив ли моб ПОСЛЕ удара
        if (!target.isAlive() && !level.isClientSide && !level.isDay()) {
            addMemoryEssence(stack, 1);
            LOGGER.info("[HDU-KILL-COUNTER] Mnemosyne killed a mob! Essence is now: {}", getMemoryEssence(stack));
        }

        stack.hurtAndBreak(1, attacker, (e) -> e.broadcastBreakEvent(attacker.getUsedItemHand()));
        return true;
    }

    // --- Активная способность Алеты ---
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isDay() && !player.getCooldowns().isOnCooldown(this)) {
            if (!level.isClientSide) {
                List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class,
                        new AABB(player.blockPosition()).inflate(10),
                        (e) -> e != player && e.isAlive());

                for (LivingEntity target : targets) {
                    target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                }
                player.getCooldowns().addCooldown(this, ALETA_COOLDOWN_TICKS);
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    // --- Динамическая подсказка ---
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        CompoundTag tag = stack.getOrCreateTag();
        String currentMode = tag.getString(TAG_MODE);
        if (currentMode.isEmpty()) currentMode = (level != null && level.isDay()) ? MODE_DAY : MODE_NIGHT;

        if (currentMode.equals(MODE_DAY)) {
            tooltip.add(Component.translatable("tooltip.hdu.mnemosyne_aleta.aleta_mode").withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable("tooltip.hdu.mnemosyne_aleta.aleta_desc1").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.hdu.mnemosyne_aleta.aleta_desc2").withStyle(ChatFormatting.GRAY));
        } else {
            int essence = getMemoryEssence(stack);
            tooltip.add(Component.translatable("tooltip.hdu.mnemosyne_aleta.mnemosyne_mode").withStyle(ChatFormatting.DARK_PURPLE));
            tooltip.add(Component.literal(ChatFormatting.GRAY + "Эхо Души: " + ChatFormatting.LIGHT_PURPLE + essence + "/" + MAX_ESSENCE));
            tooltip.add(Component.translatable("tooltip.hdu.mnemosyne_aleta.mnemosyne_desc").withStyle(ChatFormatting.GRAY));
        }

        // Вызываем super в конце, чтобы игра сама добавила стандартные подсказки (урон, скорость атаки)
        super.appendHoverText(stack, level, tooltip, context);
    }

    // --- Свечение при максимальном заряде ---
    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        String currentMode = stack.getOrCreateTag().getString(TAG_MODE);
        return currentMode.equals(MODE_NIGHT) && getMemoryEssence(stack) == MAX_ESSENCE;
    }

    // --- Вспомогательные методы для работы с NBT ---
    private int getMemoryEssence(ItemStack stack) {
        return stack.getOrCreateTag().getInt(TAG_MEMORY_ESSENCE);
    }

    private void addMemoryEssence(ItemStack stack, int amount) {
        CompoundTag tag = stack.getOrCreateTag();
        int currentEssence = tag.getInt(TAG_MEMORY_ESSENCE);
        int newEssence = Math.max(0, Math.min(MAX_ESSENCE, currentEssence + amount));
        tag.putInt(TAG_MEMORY_ESSENCE, newEssence);
    }
}