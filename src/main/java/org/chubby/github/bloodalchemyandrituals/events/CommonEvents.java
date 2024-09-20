package org.chubby.github.bloodalchemyandrituals.events;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.BloodRitualAltarBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.PedestalBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.RitualAltarBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlocks;
import org.chubby.github.bloodalchemyandrituals.common.init.BARFluids;
import org.chubby.github.bloodalchemyandrituals.common.init.BARItems;

import java.util.Collection;
import java.util.Optional;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class CommonEvents {

    @SubscribeEvent
    private static void onItemUse(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        Player player = event.getEntity();

        if (!stack.isEmpty()) {
            if (stack.getItem() == BARItems.FILLED_BLOOD_BOTTLE.asItem() && player.isShiftKeyDown()) {
                fillAltarWithBlood(stack, level, pos, player);
            }
            else if(player.isShiftKeyDown() && stack.is(Items.STICK) && level.getBlockState(pos).getBlock() == BARBlocks.RITUAL_ALTAR.get())
            {
                craftItemInAltar(stack,level,pos,player);
            }
        }
    }

    @SubscribeEvent
    private static void onPlayerKillsLivingEntity(LivingDeathEvent event)
    {
        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();
        Level level = victim.level();
        double vx = victim.getX();
        double vy = victim.getY();
        double vz = victim.getZ();
        if (!level.isClientSide && source.getEntity() instanceof Player player)
        {
            Collection<ItemEntity> drops = victim.captureDrops();
            if(drops!=null&&!drops.isEmpty())
            {
                ItemEntity bloodBottleDrop = new ItemEntity(level, vx, vy, vz, new ItemStack(BARItems.BLOOD_BOTTLE.get(), 1));
                level.addFreshEntity(bloodBottleDrop);
            }
        }
    }


    private static void fillAltarWithBlood(ItemStack stack, Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BloodRitualAltarBlockEntity altar) {
            FluidStack blood = new FluidStack(BARFluids.BLOOD_SOURCE.get(), 100);
            int fluidBefore = altar.getTank().getFluidAmount();
            altar.fillTankWithFluid(blood,stack);
            int fluidAfter = altar.getTank().getFluidAmount();

            if (fluidAfter > fluidBefore) {
                stack.shrink(1);
            } else {
                player.displayClientMessage(Component.translatable("bloodalchemyandrituals.altar_full_message").withStyle(ChatFormatting.DARK_RED), true);
            }
        }
    }

    private static void craftItemInAltar(ItemStack stack, Level level, BlockPos pos,Player player) {
       BlockEntity blockEntity = level.getBlockEntity(pos);

        if(blockEntity instanceof RitualAltarBlockEntity ritualAltar)
        {
            if (ritualAltar.checkItemsOnPedestal(level) && ritualAltar.getIsMatched()) {
                ItemStack resultStack = ritualAltar.getRituals().getResult();
                ritualAltar.setItem(0, resultStack.copyWithCount(1));
                ritualAltar.setChanged();
                //removePedestalItems(level, ritualAltar);
            } else {
                if (player != null) {
                    player.displayClientMessage(Component.translatable("bloodalchemyandrituals.altar_not_matched_message").withStyle(ChatFormatting.DARK_RED), true);
                }
            }
        }
    }


    private static void removePedestalItems(Level level, RitualAltarBlockEntity ritualAltar) {
        for (BlockPos pedestalPos : ritualAltar.getPedPositions().getPositions()) {
            BlockEntity pedestalEntity = level.getBlockEntity(pedestalPos);
            if (pedestalEntity instanceof PedestalBlockEntity pedestalBE) {
                pedestalBE.removeItemNoUpdate(0);
            }
        }
    }
}
