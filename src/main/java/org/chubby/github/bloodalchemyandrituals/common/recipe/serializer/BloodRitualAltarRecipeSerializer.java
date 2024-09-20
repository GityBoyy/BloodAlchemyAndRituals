package org.chubby.github.bloodalchemyandrituals.common.recipe.serializer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.chubby.github.bloodalchemyandrituals.common.recipe.BloodRitualAltarRecipe;
import org.jetbrains.annotations.NotNull;

public class BloodRitualAltarRecipeSerializer implements RecipeSerializer<BloodRitualAltarRecipe> {

    public static BloodRitualAltarRecipeSerializer INSTANCE;


    public static final MapCodec<BloodRitualAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(BloodRitualAltarRecipe::inputItem),
            ItemStack.CODEC.fieldOf("result").forGetter(BloodRitualAltarRecipe::result),
            Codec.INT.fieldOf("craftTime").forGetter(BloodRitualAltarRecipe::craftTime)
    ).apply(inst, BloodRitualAltarRecipe::new));
    public final StreamCodec<RegistryFriendlyByteBuf, BloodRitualAltarRecipe> STREAM_CODEC;
    public BloodRitualAltarRecipeSerializer()
    {
        STREAM_CODEC = StreamCodec.of(this::toNetwork,this::fromNetwork);
        INSTANCE = this;
    }

    @Override
    public @NotNull MapCodec<BloodRitualAltarRecipe> codec() {
        return CODEC;
    }

    private BloodRitualAltarRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient inputItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
        int craftTime = buffer.readInt();

        return new BloodRitualAltarRecipe(inputItem,result,craftTime);
    }

    private void toNetwork(RegistryFriendlyByteBuf buffer, BloodRitualAltarRecipe recipe)
    {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.inputItem());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.result());
        buffer.writeInt(recipe.craftTime());

    }
    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, BloodRitualAltarRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
