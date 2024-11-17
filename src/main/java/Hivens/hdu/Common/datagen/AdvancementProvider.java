package Hivens.hdu.Common.datagen;

import Hivens.hdu.Common.Registry.BlockRegistry;
import Hivens.hdu.Common.Registry.ItemRegistry;
import Hivens.hdu.HDU;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;

public class AdvancementProvider extends ForgeAdvancementProvider {
    public AdvancementProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> registries,
                               ExistingFileHelper fileHelper) {
        super(output, registries, fileHelper,
                List.of(new ModAdvancementSubProvider()));
    }

    private static class ModAdvancementSubProvider implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.@NotNull Provider provider,
                             @NotNull Consumer<Advancement> consumer,
                             @NotNull ExistingFileHelper existingFileHelper) {

            Advancement SEEKER_OF_MAGIC = Advancement.Builder.advancement()
                    .display(
                            ItemRegistry.ETHEREUM.get(),
                            Component.translatable("advancement.hdu.seeker_of_magic.title"),
                            Component.translatable("advancement.hdu.seeker_of_magic.description"),
                            new ResourceLocation(HDU.MODID, "textures/block/ethereum_block.png"),
                            FrameType.TASK,
                            true, // Показывать всплывающее уведомление
                            true, // Объявлять в чате
                            false // Скрытая ачивка
                    )
                    .addCriterion("ethereum_get",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.ETHEREUM.get())
                    )
                    .save(consumer, String.valueOf(new ResourceLocation(HDU.MODID, "seeker_of_magic")));


            Advancement WHITE_PRINCE = Advancement.Builder.advancement()
                    .parent(SEEKER_OF_MAGIC)
                    .display(
                            BlockRegistry.ETHEREUM_BLOCK.get(),
                            Component.translatable("advancement.hdu.white_prince.title"),
                            Component.translatable("advancement.hdu.white_prince.description"),
                            null,
                            FrameType.GOAL,
                            true,
                            true,
                            true
                    )
                    .addCriterion("get_ethereum_block",
                            InventoryChangeTrigger.TriggerInstance.hasItems(BlockRegistry.ETHEREUM_BLOCK.get())
                    )
                    .save(consumer, String.valueOf(new ResourceLocation(HDU.MODID, "white_prince")));
        }
    }
}