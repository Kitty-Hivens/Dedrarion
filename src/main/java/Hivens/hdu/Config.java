package Hivens.hdu;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec configSpec;
    public static final ForgeConfigSpec.BooleanValue devMode;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("general");
        devMode = builder
                .comment("Enable/Disable development mode. Set to true for detailed logging.")
                .define("devMode", false); // Значение по умолчанию
        builder.pop();

        configSpec = builder.build();
    }

    public static boolean isDevMode() {
        return devMode.get();
    }
}
