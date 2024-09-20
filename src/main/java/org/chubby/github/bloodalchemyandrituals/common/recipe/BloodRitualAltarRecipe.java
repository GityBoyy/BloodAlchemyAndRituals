package org.chubby.github.bloodalchemyandrituals.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.chubby.github.bloodalchemyandrituals.common.init.BARRecipes;
import org.chubby.github.bloodalchemyandrituals.common.recipe.input.BloodRitualAltarInput;
import org.chubby.github.bloodalchemyandrituals.common.recipe.serializer.BloodRitualAltarRecipeSerializer;
import org.jetbrains.annotations.NotNull;

public record BloodRitualAltarRecipe(Ingredient inputItem, ItemStack result, int craftTime) implements Recipe<BloodRitualAltarInput> {

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean matches(@NotNull BloodRitualAltarInput ritualAltarInput, @NotNull Level level) {
        if(level.isClientSide()) {
            return false;
        }
        return this.inputItem.test(ritualAltarInput.stack());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull BloodRitualAltarInput ritualAltarInput, HolderLookup.@NotNull Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return i * i1 >= 1;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return BloodRitualAltarRecipeSerializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return BARRecipes.BLOOD_RITUAL_ALTAR_RECIPE.get();
    }

}
