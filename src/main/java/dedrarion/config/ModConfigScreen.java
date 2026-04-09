package dedrarion.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Cloth Config screen factory for the in-game config GUI.
 * <p>
 * Only loaded when Cloth Config is present — the caller guards against
 * {@link ClassNotFoundException} so this class is never touched otherwise.
 */
public final class ModConfigScreen {

    private ModConfigScreen() {}

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.dedrarion.title"));

        ConfigEntryBuilder eb = builder.entryBuilder();

        // =================================================================
        //  Magic Detector
        // =================================================================
        ConfigCategory detector = builder.getOrCreateCategory(
                Component.translatable("config.dedrarion.category.magic_detector"));

        detector.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.magic_detector.scan_radius"),
                        ModConfigValues.magicDetectorScanRadius.get())
                .setDefaultValue(16).setMin(4).setMax(64)
                .setTooltip(Component.translatable("config.dedrarion.magic_detector.scan_radius.tooltip"))
                .setSaveConsumer(v -> ModConfigValues.magicDetectorScanRadius.set(v))
                .build());

        detector.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.magic_detector.highlight_duration"),
                        ModConfigValues.magicDetectorHighlightDurationMs.get())
                .setDefaultValue(10_000).setMin(1_000).setMax(60_000)
                .setTooltip(Component.translatable("config.dedrarion.magic_detector.highlight_duration.tooltip"))
                .setSaveConsumer(v -> ModConfigValues.magicDetectorHighlightDurationMs.set(v))
                .build());

        // =================================================================
        //  Eftorit Forge
        // =================================================================
        ConfigCategory forge = builder.getOrCreateCategory(
                Component.translatable("config.dedrarion.category.eftorit_forge"));

        forge.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.eftorit_forge.craft_time"),
                        ModConfigValues.eftoritForgeCraftTime.get())
                .setDefaultValue(100).setMin(20).setMax(600)
                .setTooltip(Component.translatable("config.dedrarion.eftorit_forge.craft_time.tooltip"))
                .setSaveConsumer(v -> ModConfigValues.eftoritForgeCraftTime.set(v))
                .build());

        // =================================================================
        //  Tetralin
        // =================================================================
        ConfigCategory tetralin = builder.getOrCreateCategory(
                Component.translatable("config.dedrarion.category.tetralin"));

        tetralin.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.tetralin.surge_cooldown"),
                        ModConfigValues.tetralinSurgeCooldown.get())
                .setDefaultValue(120).setMin(20).setMax(600)
                .setTooltip(Component.translatable("config.dedrarion.tetralin.surge_cooldown.tooltip"))
                .setSaveConsumer(v -> ModConfigValues.tetralinSurgeCooldown.set(v))
                .build());

        // =================================================================
        //  Mnemosyne & Aleta
        // =================================================================
        ConfigCategory mnemosyne = builder.getOrCreateCategory(
                Component.translatable("config.dedrarion.category.mnemosyne_aleta"));

        mnemosyne.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.mnemosyne.max_essence"),
                        ModConfigValues.mnemosyneMaxEssence.get())
                .setDefaultValue(10).setMin(1).setMax(50)
                .setSaveConsumer(v -> ModConfigValues.mnemosyneMaxEssence.set(v))
                .build());

        mnemosyne.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.mnemosyne.aleta_cooldown"),
                        ModConfigValues.mnemosyneAletaCooldown.get())
                .setDefaultValue(300).setMin(40).setMax(1200)
                .setSaveConsumer(v -> ModConfigValues.mnemosyneAletaCooldown.set(v))
                .build());

        mnemosyne.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.mnemosyne.essence_decay_rate"),
                        ModConfigValues.mnemosyneEssenceDecayRate.get())
                .setDefaultValue(200).setMin(20).setMax(1000)
                .setSaveConsumer(v -> ModConfigValues.mnemosyneEssenceDecayRate.set(v))
                .build());

        // =================================================================
        //  Visuals (CLIENT)
        // =================================================================
        ConfigCategory visuals = builder.getOrCreateCategory(
                Component.translatable("config.dedrarion.category.visuals"));

        visuals.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.firefly.scan_interval"),
                        ModConfigValues.fireflyScanInterval.get())
                .setDefaultValue(40).setMin(10).setMax(200)
                .setSaveConsumer(v -> ModConfigValues.fireflyScanInterval.set(v))
                .build());

        visuals.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.firefly.scan_radius"),
                        ModConfigValues.fireflyScanRadius.get())
                .setDefaultValue(10).setMin(4).setMax(32)
                .setSaveConsumer(v -> ModConfigValues.fireflyScanRadius.set(v))
                .build());

        visuals.addEntry(eb.startIntField(
                        Component.translatable("config.dedrarion.firefly.spawn_interval"),
                        ModConfigValues.fireflySpawnInterval.get())
                .setDefaultValue(4).setMin(1).setMax(20)
                .setSaveConsumer(v -> ModConfigValues.fireflySpawnInterval.set(v))
                .build());

        visuals.addEntry(eb.startDoubleField(
                        Component.translatable("config.dedrarion.ore_marker.max_render_dist"),
                        ModConfigValues.oreMarkerMaxRenderDist.get())
                .setDefaultValue(48.0).setMin(16.0).setMax(128.0)
                .setSaveConsumer(v -> ModConfigValues.oreMarkerMaxRenderDist.set(v))
                .build());

        visuals.addEntry(eb.startDoubleField(
                        Component.translatable("config.dedrarion.ore_marker.base_size"),
                        ModConfigValues.oreMarkerBaseSize.get())
                .setDefaultValue(8.0).setMin(2.0).setMax(24.0)
                .setSaveConsumer(v -> ModConfigValues.oreMarkerBaseSize.set(v))
                .build());

        // =================================================================
        //  Debug
        // =================================================================
        ConfigCategory debug = builder.getOrCreateCategory(
                Component.translatable("config.dedrarion.category.debug"));

        debug.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.dedrarion.debug.fireflies"),
                        ModConfigValues.debugFireflies.get())
                .setDefaultValue(false)
                .setSaveConsumer(v -> ModConfigValues.debugFireflies.set(v))
                .build());

        debug.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.dedrarion.debug.ore_highlights"),
                        ModConfigValues.debugOreHighlights.get())
                .setDefaultValue(false)
                .setSaveConsumer(v -> ModConfigValues.debugOreHighlights.set(v))
                .build());

        debug.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.dedrarion.debug.forge_recipes"),
                        ModConfigValues.debugForgeRecipes.get())
                .setDefaultValue(false)
                .setSaveConsumer(v -> ModConfigValues.debugForgeRecipes.set(v))
                .build());

        return builder.build();
    }
}
