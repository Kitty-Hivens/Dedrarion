package hivens.hdu.common.blocks.model; // Убедись, что пакет правильный

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity; // <-- Изменяем импорт
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ConcussiveDynamiteBlock extends Block {
    public ConcussiveDynamiteBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState pState,
            @NotNull Level pLevel,
            @NotNull BlockPos pPos,
            Player pPlayer,
            @NotNull InteractionHand pHand,
            @NotNull BlockHitResult pHit
    ) {
        if (pPlayer.getItemInHand(pHand).is(Items.FLINT_AND_STEEL)) {
            // Игрок является и поджигателем, и сущностью
            explode(pLevel, pPos, pPlayer);
            pLevel.removeBlock(pPos, false);
            // Уменьшаем прочность зажигалки
            pPlayer.getItemInHand(pHand).hurtAndBreak(1, pPlayer, (player) -> player.broadcastBreakEvent(pHand));
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            // Теперь мы можем безопасно передать любой Entity
            explode(level, pos, explosion.getExploder());
            level.removeBlock(pos, false);
        }
    }

    // --- ИСПРАВЛЕНИЕ ЗДЕСЬ ---
    // Теперь метод принимает любой Entity, как и ванильный level.explode
    private void explode(Level level, BlockPos pos, @Nullable Entity igniter) {
        if (level.isClientSide) return;

        level.explode(igniter, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 4.0F, Level.ExplosionInteraction.NONE);
        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, 1.0F);
    }
}