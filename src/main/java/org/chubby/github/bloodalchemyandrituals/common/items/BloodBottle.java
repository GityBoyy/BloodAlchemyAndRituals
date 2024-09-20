package org.chubby.github.bloodalchemyandrituals.common.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.NotNull;

public class BloodBottle extends Item {

    private final Item filledBloodBottle;

    public BloodBottle(Properties properties, Item filledBloodBottle) {
        super(properties);
        this.filledBloodBottle = filledBloodBottle;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide && player != null) {

            if (context.getLevel().getEntities(null, player.getBoundingBox().inflate(1.0D)).stream()
                    .anyMatch(entity -> entity instanceof LivingEntity)) {

                consumeBlood(player, context.getHand());

                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            LivingEntity targetEntity = getNearbyLivingEntity(player, level);
            if (targetEntity != null) {
                consumeBlood(player, hand);

                return InteractionResultHolder.success(itemStack);
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }

    private void consumeBlood(Player player, InteractionHand hand) {
        ItemStack emptyBottle = player.getItemInHand(hand);
        if (!player.isCreative()) {
            emptyBottle.shrink(1);

            ItemStack filled = new ItemStack(filledBloodBottle);
            if (!player.getInventory().add(filled)) {
                player.drop(filled, false);
            }
        }

        player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private LivingEntity getNearbyLivingEntity(Player player, Level level) {
        return level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(2.0D)).stream()
                .filter(entity -> entity != player)
                .findFirst()
                .orElse(null);
    }
}
