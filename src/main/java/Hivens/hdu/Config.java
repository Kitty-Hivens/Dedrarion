package Hivens.hdu;

import java.util.function.BooleanSupplier;

public class Config {
    private static final BooleanSupplier DEV_MODE = () -> true; // В релизе false

    public static boolean isDevMode() {
        return DEV_MODE.getAsBoolean();
    }
}
