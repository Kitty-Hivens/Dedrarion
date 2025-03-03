package Hivens.hdu;

import java.util.function.BooleanSupplier;

public class Config {
    private static final BooleanSupplier DEV_MODE = () -> false; // В релизе false

    public static boolean isDevMode() {
        return DEV_MODE.getAsBoolean();
    }
}
