package org.chubby.github.bloodalchemyandrituals.common.rituals;

import com.ibm.icu.impl.CollectionSet;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class RitualAltarRituals
{
    private final NonNullList<ItemStack> inputItems = NonNullList.withSize(4,ItemStack.EMPTY);
    private ItemStack result = ItemStack.EMPTY;

    public NonNullList<ItemStack> getInputItems() {
        return inputItems;
    }

    public ItemStack getResult() {
        return result;
    }

    public boolean matchItems(NonNullList<ItemStack> placedItems) {
        if (placedItems.size() != inputItems.size()) {
            return false;
        }

        for (int i = 0; i < inputItems.size(); i++) {
            ItemStack requiredItem = inputItems.get(i);
            ItemStack placedItem = placedItems.get(i);

            if (!ItemStack.isSameItem(requiredItem,placedItem) || placedItem.isEmpty()) {
                return false;
            }
        }

        return true;
    }




    public void addRituals(NonNullList<ItemStack> stacks, ItemStack result) {
        for (int i = 0; i < inputItems.size(); i++) {
            inputItems.set(i, ItemStack.EMPTY);
        }

        int size = Math.min(stacks.size(), inputItems.size());
        for (int i = 0; i < size; i++) {
            if (!stacks.get(i).isEmpty()) {
                this.inputItems.set(i, stacks.get(i).copy());
            }
        }
        this.result = result.copy();
    }

}
