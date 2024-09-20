package org.chubby.github.bloodalchemyandrituals.common.blocks.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.AbstractContainerBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.BloodRitualAltarBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public abstract class AbstractContainerBlock extends BaseEntityBlock {

    private final MapCodec<AbstractContainerBlock> CODEC = simpleCodec(createInstance());
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    //protected final Map<BlockState, VoxelShape> shapes;
    public AbstractContainerBlock(Properties properties) {
        super(properties);
        //this.shapes = generateShapes(this.getStateDefinition().getPossibleStates());
    }

    protected abstract Map<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states);



    public @NotNull BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }


    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected abstract Function<BlockBehaviour.Properties,AbstractContainerBlock> createInstance();

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return createBlockEntity(blockPos,blockState);
    }
    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                                       Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof AbstractContainerBlockEntity<?> containerBe) {
            if(containerBe.isEmpty() && !pStack.isEmpty()) {
                containerBe.setItem(0, pStack);
                pStack.shrink(1);
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if(pStack.isEmpty()) {
                ItemStack stackOnPedestal = containerBe.getItem(0);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                containerBe.clearContent();
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
    protected abstract BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState);

}
