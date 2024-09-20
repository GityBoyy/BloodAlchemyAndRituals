package org.chubby.github.bloodalchemyandrituals.common.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.common.init.BARFluids;
import org.chubby.github.bloodalchemyandrituals.common.init.BARRecipes;
import org.chubby.github.bloodalchemyandrituals.common.recipe.BloodRitualAltarRecipe;
import org.chubby.github.bloodalchemyandrituals.common.recipe.input.BloodRitualAltarInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BloodRitualAltarBlockEntity extends BlockEntity implements Container {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private float rotation;
    private int progress = 0;
    private int maxProgress = 40;
    private ContainerData data;
    public BloodRitualAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BARBlockEntities.BLOOD_RITUAL_ALTAR_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i){
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i){
                  case 0-> progress = i1;
                  case 1-> maxProgress = i1;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    private FluidStack neededStack = FluidStack.EMPTY;

    private final FluidTank FLUID_TANK = createFluidTank();

    private FluidTank createFluidTank() {
        return new FluidTank(6400) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return true;
            }
        };
    }

    public FluidTank getTank() {
        return FLUID_TANK;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return inventory.get(i);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(inventory, index, count);
        setChanged();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        setChanged();
        return ContainerHelper.takeItem(inventory, index);
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        inventory.set(index, stack.copyWithCount(1));
        setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }


    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        assert level != null;
        level.invalidateCapabilities(this.getBlockPos());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putInt("ritual_altar.progress", progress);
        tag.putInt("ritual_altar.maxProgress", maxProgress);

        // Save the fluid tank state
        CompoundTag fluidTag = new CompoundTag();
        this.FLUID_TANK.writeToNBT(registries,fluidTag);
        tag.put("FluidTank", fluidTag);

        if (!neededStack.isEmpty()) {
            CompoundTag neededFluidTag = new CompoundTag();
            neededStack.save(registries,neededFluidTag);
            tag.put("ritual_altar.needed_fluid", neededFluidTag);
        }

        // Save inventory
        ContainerHelper.saveAllItems(tag, inventory, registries);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        // Load the progress and maxProgress values
        progress = tag.getInt("ritual_altar.progress");
        maxProgress = tag.getInt("ritual_altar.maxProgress");

        // Load the fluid tank state
        if (tag.contains("FluidTank")) {
            CompoundTag fluidTag = tag.getCompound("FluidTank");
            this.FLUID_TANK.readFromNBT(registries,fluidTag);
        }

        // Load the needed fluid stack if it exists
        if (tag.contains("ritual_altar.needed_fluid")) {
            CompoundTag neededFluidTag = tag.getCompound("ritual_altar.needed_fluid");
            neededStack = FluidStack.parseOptional(registries,neededFluidTag);
        }

        // Load inventory
        ContainerHelper.loadAllItems(tag, inventory, registries);
    }




    public static void tick(Level level, BlockPos pos, BlockState state, BloodRitualAltarBlockEntity entity) {
        if (!level.isClientSide) {
            if (entity.hasRecipe()) {
                entity.increaseCraftingProcess();
                setChanged(level, pos, state);

                if (entity.hasProgressFinished()) {
                    entity.build();
                    entity.resetProgress();
                    entity.extractFluid();
                    LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                    if(lightningBolt!=null)
                    {
                        lightningBolt.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        level.addFreshEntity(lightningBolt);
                    }


                }
            } else {
                entity.resetProgress();
            }
        }
    }


    public void build() {
        if (this.level == null || isEmpty()) return;

        BloodRitualAltarInput input = new BloodRitualAltarInput(getItem(0));
        RecipeManager recipeManager = this.level.getRecipeManager();
        Optional<RecipeHolder<BloodRitualAltarRecipe>> recipeHolder = recipeManager.getRecipeFor(
                // The recipe type.
                BARRecipes.BLOOD_RITUAL_ALTAR_RECIPE.get(),
                input,
                this.level
        );

            ItemStack result = recipeHolder
                    .map(RecipeHolder::value)
                    .map(e -> e.assemble(input, level.registryAccess()))
                    .orElse(ItemStack.EMPTY);
            if (!result.isEmpty()) {
                if(!this.level.isClientSide) {
                    removeItemNoUpdate(0);
                    setItem(0, result);

                    setChanged();

                }
            }

    }

    private void extractFluid() {
        this.FLUID_TANK.drain(1000, IFluidHandler.FluidAction.EXECUTE);
    }

    public void fillTankWithFluid(FluidStack stack, ItemStack container) {
        this.FLUID_TANK.fill(new FluidStack(stack.getFluid(), stack.getAmount()), IFluidHandler.FluidAction.EXECUTE);

    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<BloodRitualAltarRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        BloodRitualAltarRecipe ritualRecipe = recipe.get().value();

        // Check fluid requirement
        if (!hasEnoughFluidToCraft()) {
            return false;
        }

        // Set max crafting progress
        this.maxProgress = ritualRecipe.craftTime();

        return true;
    }


    private boolean hasEnoughFluidToCraft() {
        System.out.println(this.FLUID_TANK.getFluid());
        return !this.FLUID_TANK.getFluid().isEmpty() &&
                this.FLUID_TANK.getFluid().getFluid() == BARFluids.BLOOD_SOURCE.get() &&
                this.FLUID_TANK.getFluidAmount() >= 1000;
    }


    private Optional<RecipeHolder<BloodRitualAltarRecipe>> getCurrentRecipe() {
        BloodRitualAltarInput input = new BloodRitualAltarInput(this.getItem(0));;

        if(level.isClientSide) {
            return Optional.empty();
        }
        return this.level.getRecipeManager().getRecipeFor(BARRecipes.BLOOD_RITUAL_ALTAR_RECIPE.get(), input, level);
    }


    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProcess() {
        this.progress++;
    }




    public float getRenderingRotation() {
        rotation += 0.75f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
