package org.chubby.github.bloodalchemyandrituals.common.blocks.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.BloodRitualAltarBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.util.VoxelShapeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BloodRitualAltar extends BaseEntityBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<BloodRitualAltar> CODEC = simpleCodec(BloodRitualAltar::new);

    protected final Map<BlockState, VoxelShape> shapes;

    public BloodRitualAltar(Properties properties) {
        super(properties);
        this.shapes = generateShapes(this.getStateDefinition().getPossibleStates());
    }

    public Map<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
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
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapes.get(state);
    }

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



    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new BloodRitualAltarBlockEntity(blockPos,blockState);
    }



    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        if(level.isClientSide) return null;

        return createTickerHelper(type, BARBlockEntities.BLOOD_RITUAL_ALTAR_BE.get(), BloodRitualAltarBlockEntity::tick) ;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                                       Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof BloodRitualAltarBlockEntity bloodRitualAltarBlockEntity) {
            if(bloodRitualAltarBlockEntity.isEmpty() && !pStack.isEmpty()) {
                bloodRitualAltarBlockEntity.setItem(0, pStack);
                pStack.shrink(1);
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if(pStack.isEmpty()) {
                ItemStack stackOnPedestal = bloodRitualAltarBlockEntity.getItem(0);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                bloodRitualAltarBlockEntity.clearContent();
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}
