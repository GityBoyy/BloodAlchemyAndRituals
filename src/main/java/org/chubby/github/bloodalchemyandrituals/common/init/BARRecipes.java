package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.recipe.BloodRitualAltarRecipe;

import java.util.function.Supplier;

public class BARRecipes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MODID);

    public static final Supplier<RecipeType<BloodRitualAltarRecipe>> BLOOD_RITUAL_ALTAR_RECIPE =
            RECIPE_TYPES.register(
                    "blood_ritual_altar_recipe",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "blood_ritual_altar_recipe"))
            );
    public static void register(IEventBus eventBus)
    {
        System.out.println("REGISTERED RECIPES");
        RECIPE_TYPES.register(eventBus);
    }
}
