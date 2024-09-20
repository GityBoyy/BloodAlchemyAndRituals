package org.chubby.github.bloodalchemyandrituals.common.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlocks;

public class PedestalBlockEntity extends AbstractContainerBlockEntity<PedestalBlockEntity>{

    public PedestalBlockEntity( BlockPos pos, BlockState blockState) {
        super(BARBlockEntities.PEDESTAL_BE.get(), pos, blockState);
    }


    @Override
    protected int containerSize() {
        return 1;
    }
}
