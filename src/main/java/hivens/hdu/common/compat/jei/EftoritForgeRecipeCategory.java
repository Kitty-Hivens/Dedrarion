package hivens.hdu.common.compat.jei;

import hivens.hdu.HDU;
import hivens.hdu.common.recipe.EftoritForgeRecipe;
import hivens.hdu.common.recipe.EftoritIngredient;
import hivens.hdu.common.registry.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder; // <-- Новый импорт
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class EftoritForgeRecipeCategory implements IRecipeCategory<EftoritForgeRecipe> {
    public static final RecipeType<EftoritForgeRecipe> TYPE =
            RecipeType.create(HDU.MOD_ID, "eftorit_forging", EftoritForgeRecipe.class);

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HDU.MOD_ID, "textures/gui/eftorit_forge_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    private static final int CENTER_X = 60;
    private static final int CENTER_Y = 42;
    private static final int RADIUS = 28;
    private static final float ROTATION_SPEED = 0.001f;

    public EftoritForgeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.EFTORIT_FORGE.get()));
    }

    @Override
    public int getWidth() { return 176; }
    @Override
    public int getHeight() { return 85; }
    @Override
    public @NotNull RecipeType<EftoritForgeRecipe> getRecipeType() { return TYPE; }
    @Override
    public @NotNull Component getTitle() { return Component.literal("Eftorit Forge"); }
    @Override
    public @NotNull IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull EftoritForgeRecipe recipe, @NotNull IFocusGroup focuses) {
        int invisibleSlotX = 0;
        for (EftoritIngredient ingredient : recipe.getEftoritIngredients()) {
            var slot = builder.addSlot(RecipeIngredientRole.INPUT, invisibleSlotX, getHeight() + 5)
                    .addIngredients(ingredient.ingredient());

            if (!ingredient.consume()) {
                slot.addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable("tooltip.hdu.jei.catalyst")));
            }
            if (ingredient.copyNbt()) {
                slot.addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable("tooltip.hdu.jei.nbt_source")));
            }
            invisibleSlotX += 18;
        }

        assert Minecraft.getInstance().level != null;
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 34)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(@NotNull EftoritForgeRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        List<Point> positions = calculateIngredientPositions(recipe);

        for (int i = 0; i < positions.size(); i++) {
            ItemStack stackToRender = getRenderedStack(recipe.getEftoritIngredients().get(i));
            if (stackToRender.isEmpty()) continue;

            Point pos = positions.get(i);
            guiGraphics.renderItem(stackToRender, pos.x, pos.y);
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull EftoritForgeRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Point> positions = calculateIngredientPositions(recipe);

        for (int i = 0; i < positions.size(); i++) {
            Point pos = positions.get(i);
            // Проверяем, находится ли курсор над анимированным предметом
            if (mouseX >= pos.x && mouseX < pos.x + 16 && mouseY >= pos.y && mouseY < pos.y + 16) {
                EftoritIngredient ingredient = recipe.getEftoritIngredients().get(i);
                ItemStack stack = getRenderedStack(ingredient);

                // 1. Добавляем стандартную подсказку предмета (имя, чары и т.д.)
                List<Component> vanillaTooltip = Screen.getTooltipFromItem(Minecraft.getInstance(), stack);
                for (Component line : vanillaTooltip) {
                    tooltip.add(line);
                }

                // 2. Добавляем наши кастомные строки
                if (!ingredient.consume()) {
                    tooltip.add(Component.translatable("tooltip.hdu.jei.catalyst"));
                }
                if (ingredient.copyNbt()) {
                    tooltip.add(Component.translatable("tooltip.hdu.jei.nbt_source"));
                }

                // Прекращаем проверку, так как мы нашли предмет под курсором
                return;
            }
        }
    }

    private List<Point> calculateIngredientPositions(EftoritForgeRecipe recipe) {
        List<Point> positions = new ArrayList<>();
        List<EftoritIngredient> ingredients = recipe.getEftoritIngredients();
        if (ingredients.isEmpty()) return positions;

        boolean isShiftPressed = Screen.hasShiftDown();
        float time = (isShiftPressed) ? 0 : (System.currentTimeMillis() % 360000) * ROTATION_SPEED;
        float angleStep = (float) (2 * Math.PI / ingredients.size());

        for (int i = 0; i < ingredients.size(); i++) {
            float angle = i * angleStep + time;
            int x = (int) (CENTER_X - 8 + Math.cos(angle) * RADIUS);
            int y = (int) (CENTER_Y - 8 + Math.sin(angle) * RADIUS);
            positions.add(new Point(x, y));
        }
        return positions;
    }

    private ItemStack getRenderedStack(EftoritIngredient eftoritIngredient) {
        ItemStack[] items = eftoritIngredient.ingredient().getItems();
        if (items.length == 0) return ItemStack.EMPTY;
        int itemIndex = (int) ((System.currentTimeMillis() / 1000) % items.length);
        return items[itemIndex];
    }
}