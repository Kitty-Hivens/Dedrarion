package hivens.dedrarion.common.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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

        }
    }
}