package org.chubby.github.bloodalchemyandrituals.datagen.custom;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.recipe.BloodRitualAltarRecipe;
import org.jetbrains.annotations.NotNull;

public class BloodRitualAltarRecipeGen extends SimpleRecipeBuilder {

    private final Ingredient inputItem;
    private int craftTime;
    public BloodRitualAltarRecipeGen(Ingredient inputItem, ItemStack resultItem, int craftTime) {
        super(resultItem);
        this.inputItem = inputItem;
        this.craftTime = craftTime;
    }

    @Override
    public void save(RecipeOutput output, @NotNull ResourceLocation id) {
        ResourceLocation advancementId =ResourceLocation.fromNamespaceAndPath(Constants.MODID, "recipes/" + id.getPath());
        System.out.println(advancementId);
        Advancement.Builder advancement$builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        if (this.criteria != null) {
            this.criteria.forEach(advancement$builder::addCriterion);
        }

        BloodRitualAltarRecipe result = new BloodRitualAltarRecipe(this.inputItem, this.result,this.craftTime);
        output.accept(id, result, advancement$builder.build(id.withPrefix("recipes/")));

    }
}
