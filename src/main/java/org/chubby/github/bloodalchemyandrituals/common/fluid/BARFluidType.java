package org.chubby.github.bloodalchemyandrituals.common.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class BARFluidType
{
    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.withDefaultNamespace("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.withDefaultNamespace("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL =ResourceLocation.withDefaultNamespace("block/water_overlay");

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Constants.MODID);

    public static final Supplier<FluidType> BLOOD_FLUID_TYPE = registerFluidType("blood_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xFF000,
                    new Vector3f(224f / 255f, 56f / 255f, 208f / 255f),
                    FluidType.Properties.create()));

    private static Supplier<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
