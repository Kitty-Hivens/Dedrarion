package dedrarion.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * All configurable values for Dedrarion Adventures.
 * <p>
 * Sections mirror the mod's feature groups. Values are accessed statically
 * (e.g. {@code ModConfigValues.magicDetectorScanRadius.get()}) and are
 * automatically synced from the .toml file on disk.
 */
public final class ModConfigValues {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec COMMON_SPEC;

    // =====================================================================
    //  COMMON (synced server → client, affects gameplay)
    // =====================================================================

    // --- Magic Detector ---
    public static final ForgeConfigSpec.IntValue magicDetectorScanRadius;
    public static final ForgeConfigSpec.IntValue magicDetectorHighlightDurationMs;

    // --- Eftorit Forge ---
    public static final ForgeConfigSpec.IntValue eftoritForgeCraftTime;

    // --- Tetralin ---
    public static final ForgeConfigSpec.IntValue tetralinSurgeCooldown;

    // --- Mnemosyne & Aleta ---
    public static final ForgeConfigSpec.IntValue mnemosyneMaxEssence;
    public static final ForgeConfigSpec.IntValue mnemosyneAletaCooldown;
    public static final ForgeConfigSpec.IntValue mnemosyneEssenceDecayRate;

    // =====================================================================
    //  CLIENT (local only, visuals & debug)
    // =====================================================================

    // --- Firefly System ---
    public static final ForgeConfigSpec.IntValue fireflyScanInterval;
    public static final ForgeConfigSpec.IntValue fireflyScanRadius;
    public static final ForgeConfigSpec.IntValue fireflySpawnInterval;

    // --- Ore Marker Overlay ---
    public static final ForgeConfigSpec.DoubleValue oreMarkerMaxRenderDist;
    public static final ForgeConfigSpec.DoubleValue oreMarkerBaseSize;

    // --- Debug ---
    public static final ForgeConfigSpec.BooleanValue debugFireflies;
    public static final ForgeConfigSpec.BooleanValue debugOreHighlights;
    public static final ForgeConfigSpec.BooleanValue debugForgeRecipes;

    // =====================================================================

    static {
        // --- COMMON ---
        var common = new ForgeConfigSpec.Builder();

        common.comment("Magic Detector settings").push("magic_detector");
        magicDetectorScanRadius = common
                .comment("Scan radius in blocks when RMB is pressed")
                .defineInRange("scan_radius", 16, 4, 64);
        magicDetectorHighlightDurationMs = common
                .comment("How long detected ores stay highlighted (milliseconds)")
                .defineInRange("highlight_duration_ms", 10_000, 1_000, 60_000);
        common.pop();

        common.comment("Eftorit Forge settings").push("eftorit_forge");
        eftoritForgeCraftTime = common
                .comment("Craft duration in ticks (20 ticks = 1 second)")
                .defineInRange("craft_time", 100, 20, 600);
        common.pop();

        common.comment("Tetralin settings").push("tetralin");
        tetralinSurgeCooldown = common
                .comment("Surge ability cooldown in ticks")
                .defineInRange("surge_cooldown", 120, 20, 600);
        common.pop();

        common.comment("Mnemosyne & Aleta settings").push("mnemosyne_aleta");
        mnemosyneMaxEssence = common
                .comment("Maximum soul essence stacks")
                .defineInRange("max_essence", 10, 1, 50);
        mnemosyneAletaCooldown = common
                .comment("Wave of Insight cooldown in ticks")
                .defineInRange("aleta_cooldown", 300, 40, 1200);
        mnemosyneEssenceDecayRate = common
                .comment("Ticks between essence decay steps at night")
                .defineInRange("essence_decay_rate", 200, 20, 1000);
        common.pop();

        COMMON_SPEC = common.build();

        // --- CLIENT ---
        var client = new ForgeConfigSpec.Builder();

        client.comment("Firefly particle settings (Magic Detector passive effect)").push("fireflies");
        fireflyScanInterval = client
                .comment("Ticks between nearby ore scans for firefly behavior")
                .defineInRange("scan_interval", 40, 10, 200);
        fireflyScanRadius = client
                .comment("Scan radius for firefly ore detection")
                .defineInRange("scan_radius", 10, 4, 32);
        fireflySpawnInterval = client
                .comment("Ticks between firefly particle spawns")
                .defineInRange("spawn_interval", 4, 1, 20);
        client.pop();

        client.comment("HUD ore marker overlay settings").push("ore_markers");
        oreMarkerMaxRenderDist = client
                .comment("Maximum distance (blocks) at which HUD markers are visible")
                .defineInRange("max_render_dist", 48.0, 16.0, 128.0);
        oreMarkerBaseSize = client
                .comment("Base size of the marker glow (pixels)")
                .defineInRange("base_size", 8.0, 2.0, 24.0);
        client.pop();

        client.comment("Debug toggles — enable extra logging and visual helpers").push("debug");
        debugFireflies = client
                .comment("Log firefly spawn events and ore scan counts")
                .define("fireflies", false);
        debugOreHighlights = client
                .comment("Always show ore highlights even without detector")
                .define("ore_highlights", false);
        debugForgeRecipes = client
                .comment("Log recipe matching attempts in the Eftorit Forge")
                .define("forge_recipes", false);
        client.pop();

        CLIENT_SPEC = client.build();
    }

    private ModConfigValues() {}
}
