package org.chubby.github.bloodalchemyandrituals.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.init.BARItems;
import org.chubby.github.bloodalchemyandrituals.datagen.custom.BloodRitualAltarRecipeGen;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        new BloodRitualAltarRecipeGen(
                Ingredient.of(Items.APPLE),
                new ItemStack(Items.GOLDEN_APPLE),
                20
        )
                .unlockedBy("has_apple", has(Items.APPLE))
                .save(recipeOutput, Constants.customLocation(BuiltInRegistries.ITEM.getKey(Items.GOLDEN_APPLE).getPath()));

        addBloodRitualRecipe(Items.IRON_INGOT, BARItems.BLOOD_INFUSED_INGOT, recipeOutput);
        addBloodRitualRecipe(Items.IRON_HELMET, BARItems.BLOOD_INFUSED_HELMET, recipeOutput);
        addBloodRitualRecipe(Items.IRON_CHESTPLATE, BARItems.BLOOD_INFUSED_CHESTPLATE, recipeOutput);
        addBloodRitualRecipe(Items.IRON_LEGGINGS, BARItems.BLOOD_INFUSED_LEGGINGS, recipeOutput);
        addBloodRitualRecipe(Items.IRON_BOOTS, BARItems.BLOOD_INFUSED_BOOTS, recipeOutput);
        addBloodRitualRecipe(Items.IRON_SWORD, BARItems.BLOOD_INFUSED_SWORD, recipeOutput);
        addBloodRitualRecipe(Items.IRON_PICKAXE, BARItems.BLOOD_INFUSED_PICKAXE, recipeOutput);
        addBloodRitualRecipe(Items.IRON_AXE, BARItems.BLOOD_INFUSED_AXE, recipeOutput);
        addBloodRitualRecipe(Items.IRON_SHOVEL, BARItems.BLOOD_INFUSED_SHOVEL, recipeOutput);
        addBloodRitualRecipe(Items.IRON_HOE, BARItems.BLOOD_INFUSED_HOE, recipeOutput);
    }

    private static void addBloodRitualRecipe(Item input, Supplier<Item> outputItem, RecipeOutput recipeOutput) {
        new BloodRitualAltarRecipeGen(
                Ingredient.of(input),
                new ItemStack(outputItem.get()),
                20
        )
                .unlockedBy("has_" + input.toString(), has(input))
                .save(recipeOutput, Constants.customLocation(BuiltInRegistries.ITEM.getKey(outputItem.get()).getPath()));
    }
}
