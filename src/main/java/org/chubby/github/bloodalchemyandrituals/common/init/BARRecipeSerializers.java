package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.recipe.BloodRitualAltarRecipe;
import org.chubby.github.bloodalchemyandrituals.common.recipe.serializer.BloodRitualAltarRecipeSerializer;

import java.util.function.Supplier;

public class BARRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MODID);

    public static final Supplier<RecipeSerializer<BloodRitualAltarRecipe>> BLOOD_RITUAL_ALTAR_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("blood_ritual_altar_recipe_serializer", BloodRitualAltarRecipeSerializer::new);

    public static void register(IEventBus eventBus)
    {
        System.out.println("REGISTERED RECIPE SERIALIZERS");
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
