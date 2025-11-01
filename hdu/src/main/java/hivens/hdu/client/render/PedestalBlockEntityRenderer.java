package hivens.hdu.client.render;

import hivens.hdu.common.blocks.entity.PedestalBlockEntity;
import hivens.hdu.common.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PedestalBlockEntityRenderer implements BlockEntityRenderer<PedestalBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PedestalBlockEntityRenderer() {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(PedestalBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemStack itemStack = pBlockEntity.getItem();
        if (itemStack.isEmpty()) return;

        // --- 1. Вспомогательные переменные для времени и анимации ---
        // Получаем общее время игры + частичный тик для плавности
        float time = Objects.requireNonNull(pBlockEntity.getLevel()).getGameTime() + pPartialTick;

        // Покачивание (боббинг): синус от времени
        float bob = Mth.sin(time / 8.0f) * 0.05f + 0.02f;
        // Вращение: время * скорость
        float rotation = time * 4.0f;


        // --- 2. ЛОГИКА СВЕЧЕНИЯ ---
        int customPackedLight = pPackedLight;

        // Определяем, является ли предмет АРТЕФАКТОМ или КЛЮЧЕВЫМ МАГИЧЕСКИМ ресурсом
        boolean isArtifact = itemStack.is(ModItems.ETHER_CORE.get()) // Энергетическое ядро
                || itemStack.is(ModItems.MNEMOSYNE_ALETA.get()) // Мнемозина и Алета
                || itemStack.is(ModItems.TETRALIN.get()) // Тетралин
                || itemStack.is(ModItems.DETONATION_BLADE.get()) // Меч Детонации
                || itemStack.is(ModItems.MAGIC_DETECTOR.get()) // Магический Детектор
                || itemStack.is(ModItems.ETHEREUM.get()); // Эфириум (ключевой компонент)

        if (isArtifact) {
            // Максимально усиливаем свет, чтобы светилось в любой темноте
            customPackedLight = LightTexture.FULL_BRIGHT;
            // Увеличиваем скорость вращения
            rotation = time * 6.0f;
        }

        // --- 3. Начинаем трансформации ---
        pPoseStack.pushPose();

        pPoseStack.translate(0.5f, 0.75f, 0.5f); // Центр пьедестала
        pPoseStack.translate(0.0f, bob, 0.0f); // Покачивание
        pPoseStack.scale(0.35f, 0.35f, 0.35f); // Масштаб

        pPoseStack.mulPose(Axis.YP.rotationDegrees(rotation)); // Вращение вокруг Y
        pPoseStack.mulPose(Axis.XP.rotationDegrees(90)); // Наклон для отображения предмета

        // --- 4. Рендерим предмет ---
        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                customPackedLight, // Используем динамический свет
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(),
                1
        );

        pPoseStack.popPose();
    }
}