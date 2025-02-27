package Hivens.hdu.Common.compat;

import Hivens.hdu.Common.compat.jei.JERCompat;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModCompat {public static void initCommon(FMLCommonSetupEvent e){
    if(ModList.get().isLoaded("jeresources")){
        JERCompat.init();
    }
}
}
