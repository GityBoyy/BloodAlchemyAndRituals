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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractContainerBlockEntity<T extends AbstractContainerBlockEntity<T>> extends BlockEntity implements Container
{
    private float rotation;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(containerSize(), ItemStack.EMPTY);
    public AbstractContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static BlockEntity create(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if(!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        setChanged();
        return inventory.get(pSlot);
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        setChanged();
        ItemStack stack = inventory.get(pSlot);
        stack.shrink(pAmount);
        return inventory.set(pSlot, stack);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        setChanged();
        return ContainerHelper.takeItem(inventory, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        setChanged();
        inventory.set(pSlot, pStack.copyWithCount(1));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ContainerHelper.saveAllItems(pTag, inventory, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        ContainerHelper.loadAllItems(pTag, inventory, pRegistries);
    }

    public float getRenderingRotation() {
        rotation += 0.75f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    protected abstract int containerSize();

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
