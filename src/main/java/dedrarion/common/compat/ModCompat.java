package dedrarion.common.compat;

import dedrarion.common.compat.jei.JERCompat;
import net.minecraftforge.fml.ModList;

public class ModCompat {
    public static void initCommon() {
        if(ModList.get().isLoaded("data/jeresources")){
            JERCompat.init();
        }
    }
}
