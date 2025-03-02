package Hivens.hdu.Common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;

public record EftoritIngredient(Ingredient ingredient, boolean consume) {
    
    public static EftoritIngredient fromJson(JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json);
        boolean consume = json.has("consume") && json.get("consume").getAsBoolean();
        return new EftoritIngredient(ingredient, consume);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        ingredient.toNetwork(buffer);
        buffer.writeBoolean(consume);
    }

    public static EftoritIngredient fromNetwork(FriendlyByteBuf buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        boolean consume = buffer.readBoolean();
        return new EftoritIngredient(ingredient, consume);
    }
}
