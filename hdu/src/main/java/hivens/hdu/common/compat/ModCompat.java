package hivens.hdu.common.compat;

import hivens.hdu.common.compat.jei.JERCompat;
import net.minecraftforge.fml.ModList;

public class ModCompat {
    public static void initCommon() {
        if(ModList.get().isLoaded("jeresources")){
            JERCompat.init();
        }
    }
}
