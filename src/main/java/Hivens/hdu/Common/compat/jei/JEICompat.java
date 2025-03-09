package Hivens.hdu.Common.compat.jei;

import Hivens.hdu.HDU;
import mezz.jei.api.*;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@JeiPlugin
public final class JEICompat
        implements IModPlugin
{

    public static final ResourceLocation UID = new ResourceLocation(HDU.MODID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        // Добавить станции после
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration){
        // Добавить рецепты после

        assert Minecraft.getInstance().level != null;
        @SuppressWarnings("resource")
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

    }


}