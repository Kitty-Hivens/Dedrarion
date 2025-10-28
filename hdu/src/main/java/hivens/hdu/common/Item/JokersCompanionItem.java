package hivens.hdu.common.Item;

import hivens.hdu.common.entity.PrimedJokerBomb;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class JokersCompanionItem extends Item {
    public JokersCompanionItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        // Воспроизводим звук "шипения"
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);

        // Запускаем кулдаун, чтобы игрок не мог спамить
        pPlayer.getCooldowns().addCooldown(this, 40); // 2 секунды

        if (!pLevel.isClientSide) {
            // Создаём нашу сущность-таймер
            PrimedJokerBomb bomb = new PrimedJokerBomb(pLevel, pPlayer.getX(), pPlayer.getEyeY(), pPlayer.getZ(), pPlayer);
            pLevel.addFreshEntity(bomb);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1); // Забираем предмет у игрока
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}