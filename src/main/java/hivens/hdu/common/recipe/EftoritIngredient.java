package hivens.hdu.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public record EftoritIngredient(Ingredient ingredient, boolean consume, boolean copyNbt) {

    // Вспомогательные конструкторы для удобства
    public EftoritIngredient(Ingredient ingredient, boolean consume) {
        this(ingredient, consume, false); // По умолчанию NBT не копируем
    }

    public static EftoritIngredient fromJson(JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json);
        boolean consume = !json.has("consume") || json.get("consume").getAsBoolean(); // По умолчанию true
        boolean copyNbt = json.has("copyNbt") && json.get("copyNbt").getAsBoolean();   // Новое поле
        return new EftoritIngredient(ingredient, consume, copyNbt);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        ingredient.toNetwork(buffer);
        buffer.writeBoolean(consume);
        buffer.writeBoolean(copyNbt); // Новое поле
    }

    public static EftoritIngredient fromNetwork(FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        boolean consume = buffer.readBoolean();
        boolean copyNbt = buffer.readBoolean(); // Новое поле
        return new EftoritIngredient(ingredient, consume, copyNbt);
    }
}