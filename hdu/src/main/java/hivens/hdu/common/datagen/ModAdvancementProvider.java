package hivens.hdu.common.datagen;

import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.registry.ModItems;
import hivens.hdu.HDU;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;

public class ModAdvancementProvider extends ForgeAdvancementProvider {
    public ModAdvancementProvider(PackOutput output,
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

            Advancement DEDRARION_UNDERGROUND_MAIN = Advancement.Builder.advancement()
                    .display(
                            ModItems.EFTORIT.get(),
                            Component.translatable("advancement.hdu.dedrarion_underground.title"),
                            Component.translatable("advancement.hdu.dedrarion_underground.description"),
                            ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "textures/block/ethereum_block.png"),
                            FrameType.TASK,
                            false, // Показывать всплывающее уведомление
                            false, // Объявлять в чате
                            false // Скрытая ачивка
                    )
                    .addCriterion("cobblestone_get",
                            InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE)
                    )
                    .save(consumer, String.valueOf(ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "dedrarion_underground_main")));

            Advancement SEEKER_OF_MAGIC = Advancement.Builder.advancement()
                    .parent(DEDRARION_UNDERGROUND_MAIN)
                    .display(
                            ModItems.ETHEREUM.get(),
                            Component.translatable("advancement.hdu.seeker_of_magic.title"),
                            Component.translatable("advancement.hdu.seeker_of_magic.description"),
                            ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "textures/block/ethereum_block.png"),
                            FrameType.TASK,
                            true, // Показывать всплывающее уведомление
                            true, // Объявлять в чате
                            false // Скрытое достижение
                    )
                    .addCriterion("ethereum_get",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ETHEREUM.get())
                    )
                    .save(consumer, String.valueOf(ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "seeker_of_magic")));


            Advancement WHITE_PRINCE = Advancement.Builder.advancement()
                    .parent(SEEKER_OF_MAGIC)
                    .display(
                            ModBlocks.ETHEREUM_BLOCK.get(),
                            Component.translatable("advancement.hdu.white_prince.title"),
                            Component.translatable("advancement.hdu.white_prince.description"),
                            null,
                            FrameType.GOAL,
                            true,
                            true,
                            true
                    )
                    .addCriterion("get_ethereum_block",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.ETHEREUM_BLOCK.get())
                    )
                    .save(consumer, String.valueOf(ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "white_prince")));

            Advancement COLLECTOR_OF_JEWELRY = Advancement.Builder.advancement()
                    .parent(SEEKER_OF_MAGIC)
                    .display(
                            ModItems.RUBY.get(),
                            Component.translatable("advancement.hdu.collector_of_jewelry.title"),
                            Component.translatable("advancement.hdu.collector_of_jewelry.description"),
                            null,
                            FrameType.GOAL,
                            true,
                            false,
                            false
                    )
                    .addCriterion("get_all_the_jewelry_of_the_dedrarion_underground",
                            InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ETHEREUM.get(),
                                                                            ModItems.RUBY.get(),
                                                                            ModItems.EFTORIT.get())
                    )
                    .save(consumer, String.valueOf(ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "collector_of_jewelry")));
        }
    }
}