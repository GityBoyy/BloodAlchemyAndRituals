package org.chubby.github.bloodalchemyandrituals.common.blocks.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.RitualAltarBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.util.VoxelShapeHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RitualAltarBlock extends AbstractContainerBlock
{
    public RitualAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected Map<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        VoxelShape shape1 = Block.box(5.5, 1, 5.5,10.5, 10, 10.5);
        VoxelShape shape2 = Block.box(1.5, 9, 1.5,14.5, 11, 14.5);
        VoxelShape shape3 = Shapes.box(3, 0, 3,13, 2, 13);
        VoxelShape shape4 = Shapes.box(3, 12, 5,4, 17, 6);
        VoxelShape chairShape = VoxelShapeHelper.combine(List.of(shape1, shape2, shape3,shape4));
        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for(BlockState state : states)
        {
            Direction direction = state.getValue(FACING);
            VoxelShape rotatedChairShape = VoxelShapeHelper.rotateHorizontally(chairShape, direction);
            builder.put(state, rotatedChairShape);
        }
        return builder.build();
    }

    @Override
    protected Function<Properties, AbstractContainerBlock> createInstance() {
        return RitualAltarBlock::new;
    }

    @Override
    protected BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RitualAltarBlockEntity(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BARBlockEntities.RITUAL_ALTAR_BE.get(), RitualAltarBlockEntity::tick);
    }
}
