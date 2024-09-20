package org.chubby.github.bloodalchemyandrituals.common.blocks.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkullBlock extends Block implements Equipable {

    public static Map<LivingEntity, Integer> entityTimeMap = new HashMap<>();

    public SkullBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        AABB effectRadius = new AABB(pos).inflate(5);
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, effectRadius);

        updateNearbyEntities(level, pos, nearbyEntities);
    }

    private void updateNearbyEntities(Level level, BlockPos pos, List<LivingEntity> nearbyEntities) {
        entityTimeMap.entrySet().removeIf(entry -> !nearbyEntities.contains(entry.getKey()));

        for (LivingEntity entity : nearbyEntities) {
            entityTimeMap.put(entity, entityTimeMap.getOrDefault(entity, 0) + 1);

            int timeNearSkull = entityTimeMap.get(entity);

            applyEffectBasedOnTime(entity, timeNearSkull);
        }
    }

    private void applyEffectBasedOnTime(LivingEntity entity, int timeNearSkull) {
        int effectLevel = timeNearSkull / 100;

        effectLevel = Math.min(effectLevel, 4);

        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, timeNearSkull, effectLevel, false, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Constants.translatable("skull.info_press_shift").copy().withStyle(ChatFormatting.YELLOW));
        if(tooltipFlag.hasShiftDown())
        {
            tooltipComponents.add(Constants.translatable("skull.info").copy().withStyle(ChatFormatting.RED));
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);

        if(level.isClientSide) return;

        AABB effectRadius = new AABB(pos).inflate(5);
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, effectRadius);
        updateNearbyEntities(level, pos, nearbyEntities);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
