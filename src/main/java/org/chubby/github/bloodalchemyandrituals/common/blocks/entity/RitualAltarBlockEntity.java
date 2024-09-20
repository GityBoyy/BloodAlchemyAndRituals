package org.chubby.github.bloodalchemyandrituals.common.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.common.rituals.RitualAltarRituals;

import java.util.List;
import java.util.Map;

public class RitualAltarBlockEntity extends AbstractContainerBlockEntity<RitualAltarBlockEntity> {

    public final RitualPedestalPositions pedPositions;
    private final RitualAltarRituals rituals = new RitualAltarRituals();
    public boolean isMatched;

    public RitualAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BARBlockEntities.RITUAL_ALTAR_BE.get(), pos, blockState);
        BlockPos altarPos = this.getBlockPos();
        this.pedPositions = new RitualPedestalPositions(
                altarPos.offset(2, 0, 0),
                altarPos.offset(-2, 0, 0),
                altarPos.offset(0, 0, 2),
                altarPos.offset(0, 0, -2)
        );
        this.addRitualsToConst();
    }

    @Override
    protected int containerSize() {
        return 1;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RitualAltarBlockEntity be) {
        if (!level.isClientSide) {
            be.isMatched = be.checkItemsOnPedestal(level);
        }
    }

    public boolean checkItemsOnPedestal(Level level) {
        NonNullList<ItemStack> placedItems = NonNullList.withSize(4, ItemStack.EMPTY);
        int index = 0;

        for (BlockPos pos : pedPositions.getPositions()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof PedestalBlockEntity be) {
                ItemStack stack = be.getItem(0);
                if (!stack.isEmpty()) {
                    if (index < placedItems.size()) {
                        placedItems.set(index, stack);
                        index++;
                    } else {
                        Constants.LOGGER.warn("More items found on pedestal than can be stored: " + stack);
                    }
                }
            }
        }
        Constants.LOGGER.info("Placed Items: {}", placedItems);
        Constants.LOGGER.info("Ritual Input Items: {}", rituals.getInputItems());
        System.out.println(rituals.matchItems(placedItems));
        return rituals.matchItems(placedItems);
    }

    public boolean getIsMatched() {
        return this.isMatched;
    }

    public RitualPedestalPositions getPedPositions() {
        return pedPositions;
    }

    public record RitualPedestalPositions(BlockPos pos1, BlockPos pos2, BlockPos pos3, BlockPos pos4) {
        public BlockPos[] getPositions() {
            return new BlockPos[]{pos1, pos2, pos3, pos4};
        }
    }

    public RitualAltarRituals getRituals() {
        return rituals;
    }

    private void addRitualsToConst() {
        Constants.LOGGER.info("Added rituals!");
        this.rituals.addRituals(
                NonNullList.of(
                        new ItemStack(Items.COBBLESTONE), new ItemStack(Items.COBBLESTONE), new ItemStack(Items.COBBLESTONE), new ItemStack(Items.PAPER)),
                new ItemStack(Items.BOOK)
        );
        Constants.LOGGER.info("Rituals added: " + rituals.getInputItems() + " -> " + rituals.getResult());

    }

    public void resetAltar() {
        this.isMatched = false;
    }
}
