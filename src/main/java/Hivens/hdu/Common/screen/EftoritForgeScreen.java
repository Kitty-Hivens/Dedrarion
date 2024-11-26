package Hivens.hdu.Common.screen;

import Hivens.hdu.HDU;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EftoritForgeScreen extends AbstractContainerScreen<EftoritForgeMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HDU.MODID, "textures/gui/eftorit_forge_gui.png");


    public EftoritForgeScreen(EftoritForgeMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }



    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }
}
