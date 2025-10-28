package hivens.hdu.common.compat.jei;

import hivens.hdu.HDU;
import hivens.hdu.common.recipe.EftoritForgeRecipe;
import hivens.hdu.common.registry.ModBlocks;
import hivens.hdu.common.registry.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {
    public static final Logger LOGGER = LogManager.getLogger();

    public JEICompat() {
        LOGGER.info(">>>>>>>>>> [HDU-JEI] Plugin constructor called! <<<<<<<<<<");
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        LOGGER.info(">>>>>>>>>> [HDU-JEI] Step 1: Registering Categories... <<<<<<<<<<");
        try {
            registration.addRecipeCategories(new EftoritForgeRecipeCategory(
                    registration.getJeiHelpers().getGuiHelper()));
            LOGGER.info(">>>>>>>>>> [HDU-JEI] Step 1 SUCCESS! <<<<<<<<<<");
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>> [HDU-JEI] Step 1 FAILED! Error while registering categories:", e);
        }
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        LOGGER.info(">>>>>>>>>> [HDU-JEI] Step 2: Registering Recipes... <<<<<<<<<<");
        try {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                RecipeManager recipeManager = minecraft.level.getRecipeManager();
                List<EftoritForgeRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipes.EFTORIT_FORGE_RECIPE_TYPE.get());
                registration.addRecipes(EftoritForgeRecipeCategory.TYPE, recipes);
                LOGGER.info(">>>>>>>>>> [HDU-JEI] Step 2 SUCCESS! Found {} recipes. <<<<<<<<<<", recipes.size());
            } else {
                LOGGER.warn(">>>>>>>>>> [HDU-JEI] Step 2 SKIPPED! Minecraft level was null. <<<<<<<<<<");
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>> [HDU-JEI] Step 2 FAILED! Error while registering recipes:", e);
        }
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        LOGGER.info(">>>>>>>>>> [HDU-JEI] Step 3: Registering Catalysts... <<<<<<<<<<");
        try {
            registration.addRecipeCatalyst(new ItemStack(ModBlocks.EFTORIT_FORGE.get()), EftoritForgeRecipeCategory.TYPE);
            LOGGER.info(">>>>>>>>>> [HDU-JEI] Step 3 SUCCESS! <<<<<<<<<<");
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>> [HDU-JEI] Step 3 FAILED! Error while registering catalyst:", e);
        }
    }
}