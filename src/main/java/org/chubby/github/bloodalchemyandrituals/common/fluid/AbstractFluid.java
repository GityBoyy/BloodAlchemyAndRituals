package org.chubby.github.bloodalchemyandrituals.common.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.chubby.github.bloodalchemyandrituals.common.fluid.impl.IFluid;


import java.util.function.Supplier;

/**
 * AbstractFluid is a custom fluid class that extends ForgeFlowingFluid and implements IFluid interface.
 * This class includes various properties and behaviors for the fluid such as temperature, color, viscosity,
 * density, freezing capability, poisonous nature, and more.
 */
public class AbstractFluid extends BaseFlowingFluid implements IFluid {
    private final int temperature;
    private final int color;
    private final int viscosity;
    private final int density;
    private final int evaporationRate;
    private final int lightLevel;
    private final boolean canFreeze;
    private final boolean isPoisonous;
    private final boolean isBuoyant;
    private final boolean canExtinguishFire;
    private final boolean holy;
    private final Holder<MobEffect> effect;
    private final int effectDuration;
    private final Supplier<? extends FluidType> fluidType;
    private final Supplier<? extends Fluid> flowing;
    private final Supplier<? extends Fluid> still;

    /**
     * Constructor for AbstractFluid.
     *
     * @param properties Properties for the fluid.
     * @param holy Whether the fluid is holy.
     */
    protected AbstractFluid(ABSProperties properties, boolean holy) {
        super(properties);
        this.temperature = properties.temperature;
        this.color = properties.color;
        this.viscosity = properties.viscosity;
        this.density = properties.density;
        this.evaporationRate = properties.evaporationRate;
        this.lightLevel = properties.lightLevel;
        this.canFreeze = properties.canFreeze;
        this.isPoisonous = properties.isPoisonous;
        this.isBuoyant = properties.isBuoyant;
        this.canExtinguishFire = properties.canExtinguishFire;
        this.fluidType = properties.fluidType;
        this.flowing = properties.flowing;
        this.still = properties.still;
        this.holy = holy;
        this.effect = properties.effect;
        this.effectDuration = properties.effectDuration;
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getDensity() {
        return density;
    }

    @Override
    public Holder<MobEffect> getEffect() {
        return null;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public boolean applyEffect(Holder<MobEffect> effect, LivingEntity player,int duration) {
        if(player.level().isClientSide) return false;

        return player.addEffect(new MobEffectInstance(effect,duration));
    }

    @Override
    public int getEvaporationRate() {
        return evaporationRate;
    }

    @Override
    public int getViscosity() {
        return viscosity;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public boolean canFreeze() {
        return canFreeze;
    }

    @Override
    public ParticleOptions getDripParticle() {
        return null;
    }

    @Override
    public int getSpreadDelay(Level level, BlockPos pos, FluidState fluidState, FluidState neighborFluidState) {
        return this.getTickDelay(level);
    }

    @Override
    public boolean canExtinguishFire() {
        return canExtinguishFire;
    }

    @Override
    public boolean isBuoyant() {
        return isBuoyant;
    }

    @Override
    public Fluid getFlowing() {
        return super.getFlowing();
    }

    @Override
    public Fluid getSource() {
        return super.getSource();
    }

    @Override
    public boolean isSource(FluidState pState) {
        return false;
    }

    @Override
    public int getAmount(FluidState pState) {
        return 8;
    }

    /**
     * Ticks the fluid, handling freezing, poisoning, and holy effects.
     *
     * @param pLevel The level.
     * @param pPos The position.
     * @param pState The fluid state.
     */
    @Override
    public void tick(Level pLevel, BlockPos pPos, FluidState pState) {
        if (!pLevel.isClientSide()) {
            if (this.canFreeze() && this.getTemperature() <= 23) {
                if (pLevel.isEmptyBlock(pPos.above())) {
                    pLevel.setBlockAndUpdate(pPos, Blocks.ICE.defaultBlockState());
                }
            }
            for (LivingEntity entity : pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pPos))) {
                applyEffect(getEffect(),entity,getDuration());
            }

        }
    }

    /**
     * Flowing fluid class.
     */
    public static class Flowing extends BaseFlowingFluid {
        public Flowing(Properties properties) {
            super(properties);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }


    }

    /**
     * Source fluid class.
     */
    public static class Source extends BaseFlowingFluid {
        public Source(Properties properties) {
            super(properties);
        }



        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
    }

    /**
     * Custom properties for AbstractFluid.
     */
    public static class ABSProperties extends BaseFlowingFluid.Properties {
        private int temperature = 300;
        private int color = 0xFFFFFF;
        private int viscosity = 1000;
        private int density = 1000;
        private int evaporationRate = 5;
        private int lightLevel = 0;
        private boolean canFreeze = false;
        private boolean isPoisonous = false;
        private boolean isBuoyant = false;
        private boolean canExtinguishFire = false;
        public boolean holy;
        public Holder<MobEffect> effect;
        public int effectDuration;
        private Supplier<? extends FluidType> fluidType;
        private Supplier<? extends Fluid> still;
        private Supplier<? extends Fluid> flowing;

        /**
         * Constructor for ABSProperties.
         *
         * @param fluidType The fluid type.
         * @param still The still fluid supplier.
         * @param flowing The flowing fluid supplier.
         */
        public ABSProperties(Supplier<? extends FluidType> fluidType, Supplier<? extends Fluid> still, Supplier<? extends Fluid> flowing) {
            super(fluidType, still, flowing);
            this.fluidType = fluidType;
            this.still = still;
            this.flowing = flowing;
        }

        public ABSProperties temperature(int temperature) {
            this.temperature = temperature;
            return this;
        }

        public ABSProperties color(int color) {
            this.color = color;
            return this;
        }

        public ABSProperties viscosity(int viscosity) {
            this.viscosity = viscosity;
            return this;
        }

        public ABSProperties density(int density) {
            this.density = density;
            return this;
        }

        public ABSProperties evaporationRate(int evaporationRate) {
            this.evaporationRate = evaporationRate;
            return this;
        }

        public ABSProperties lightLevel(int lightLevel) {
            this.lightLevel = lightLevel;
            return this;
        }

        public ABSProperties addEffect(Holder<MobEffect> effect)
        {
            this.effect = effect;
            return this;
        }

        public ABSProperties setDurationOfEffect(int duration)
        {
            this.effectDuration = duration;
            return this;
        }

        public ABSProperties canFreeze(boolean canFreeze) {
            this.canFreeze = canFreeze;
            return this;
        }

        public ABSProperties isPoisonous(boolean isPoisonous) {
            this.isPoisonous = isPoisonous;
            return this;
        }

        public ABSProperties isBuoyant(boolean isBuoyant) {
            this.isBuoyant = isBuoyant;
            return this;
        }

        public ABSProperties canExtinguishFire(boolean canExtinguishFire) {
            this.canExtinguishFire = canExtinguishFire;
            return this;
        }
        public ABSProperties isHoly(boolean isHoly){
            this.holy = isHoly;
            return this;
        }
    }
}
