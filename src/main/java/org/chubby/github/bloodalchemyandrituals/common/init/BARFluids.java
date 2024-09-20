package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.fluid.AbstractFluid;
import org.chubby.github.bloodalchemyandrituals.common.fluid.BARFluidType;

import java.util.function.Supplier;

public class BARFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Constants.MODID);

    public static final Supplier<FlowingFluid> BLOOD_FLOWING =
            FLUIDS.register("blood_flowing",()-> new AbstractFluid.Flowing(BARFluids.BLOOD_PROPERTIES));
    public static final Supplier<Fluid> BLOOD_SOURCE =
            FLUIDS.register("blood_source",()-> new AbstractFluid.Source(BARFluids.BLOOD_PROPERTIES));

    public static AbstractFluid.ABSProperties BLOOD_PROPERTIES = (AbstractFluid.ABSProperties) new AbstractFluid.ABSProperties(
            BARFluidType.BLOOD_FLUID_TYPE,
            BLOOD_FLOWING,
            BLOOD_SOURCE
    )
            .density(1200)
            .viscosity(30)
            .color(0xFF0000)
            .canFreeze(false)
            .isBuoyant(true)
            .evaporationRate(12)
            .lightLevel(1)
            .canExtinguishFire(false)
            .temperature(30)
            .setDurationOfEffect(120)
            .addEffect(MobEffects.CONFUSION)
            .slopeFindDistance(2).levelDecreasePerBlock(1)
            .block(BARBlocks.BLOOD_BLOCK);

    public static void register(IEventBus eventBus)
    {
        FLUIDS.register(eventBus);
    }
}
