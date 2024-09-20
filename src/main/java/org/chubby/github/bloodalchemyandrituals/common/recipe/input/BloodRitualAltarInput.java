package org.chubby.github.bloodalchemyandrituals.common.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record BloodRitualAltarInput(ItemStack stack) implements RecipeInput
{

    @Override
    public @NotNull ItemStack getItem(int slot) {
        if (slot != 0) throw new IllegalArgumentException("No item for index " + slot);
        return this.stack();
    }

    @Override
    public int size() {
        return 1;
    }
}
