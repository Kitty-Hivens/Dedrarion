package hivens.hdu.common.Item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnstableGunpowderItem extends Item {
    private static final String TAG_STABILITY = "Stability";
    private static final String TAG_LAST_YAW = "LastYaw";
    private static final String TAG_LAST_PITCH = "LastPitch";
    private static final String TAG_IS_INITIALIZED = "IsInitialized"; // Наш "ключ зажигания"
    private static final int MAX_STABILITY = 1000;

    public UnstableGunpowderItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!(pEntity instanceof Player player)) {
            return;
        }

        CompoundTag tag = pStack.getOrCreateTag();
        int stability = getStability(pStack);

        // Проверяем, находится ли предмет в активной руке (основной или второй)
        boolean isActive = player.getMainHandItem() == pStack || player.getOffhandItem() == pStack;

        if (isActive) {
            // --- ГЛАВНОЕ ИСПРАВЛЕНИЕ ЗДЕСЬ ---
            if (!tag.getBoolean(TAG_IS_INITIALIZED)) {
                // Это ПЕРВЫЙ ТИК. Мы не проверяем, а просто запоминаем позицию.
                tag.putFloat(TAG_LAST_YAW, player.getYRot());
                tag.putFloat(TAG_LAST_PITCH, player.getXRot());
                tag.putBoolean(TAG_IS_INITIALIZED, true);
                return; // Выходим, чтобы не проверять тряску в этот раз
            }

            float lastYaw = tag.getFloat(TAG_LAST_YAW);
            float lastPitch = tag.getFloat(TAG_LAST_PITCH);

            float deltaYaw = Mth.abs(player.getYRot() - lastYaw);
            float deltaPitch = Mth.abs(player.getXRot() - lastPitch);

            if (deltaYaw > 15 || deltaPitch > 15) {
                stability -= (int) ((deltaYaw + deltaPitch) / 2);
            }

            tag.putFloat(TAG_LAST_YAW, player.getYRot());
            tag.putFloat(TAG_LAST_PITCH, player.getXRot());

        } else {
            // Если предмет не в руке, сбрасываем "ключ зажигания"
            tag.putBoolean(TAG_IS_INITIALIZED, false);
        }

        // Медленное восстановление стабильности
        if (stability < MAX_STABILITY && pLevel.getGameTime() % 20 == 0) {
            stability += 5;
        }

        setStability(pStack, Mth.clamp(stability, 0, MAX_STABILITY));

        // Эффекты и взрыв (остаются без изменений)
        if (stability < MAX_STABILITY / 2 && pLevel.isClientSide && pLevel.random.nextFloat() < 0.1f) {
            pLevel.addParticle(ParticleTypes.SMOKE, player.getX(), player.getEyeY(), player.getZ(), 0, 0, 0);
        }
        if (stability <= 0 && !pLevel.isClientSide) {
            pLevel.explode(player, player.getX(), player.getY(), player.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
            pStack.shrink(1);
        }
    }
    
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pIsAdvanced) {
        int stability = getStability(pStack);
        
        if (stability < MAX_STABILITY / 3) {
            pTooltip.add(Component.translatable("tooltip.item.hdu.unstable_gunpowder.danger").withStyle(ChatFormatting.RED));
        } else if (stability < (MAX_STABILITY / 3) * 2) {
            pTooltip.add(Component.translatable("tooltip.item.hdu.unstable_gunpowder.unstable").withStyle(ChatFormatting.YELLOW));
        } else {
            pTooltip.add(Component.translatable("tooltip.item.hdu.unstable_gunpowder.stable").withStyle(ChatFormatting.GRAY));
        }
    }

    // --- Вспомогательные методы ---
    private int getStability(ItemStack stack) {
        if (stack.getOrCreateTag().contains(TAG_STABILITY)) {
            assert stack.getTag() != null;
            return stack.getTag().getInt(TAG_STABILITY);
        } else {
            return MAX_STABILITY;
        }
    }

    private void setStability(ItemStack stack, int stability) {
        stack.getOrCreateTag().putInt(TAG_STABILITY, stability);
    }
}