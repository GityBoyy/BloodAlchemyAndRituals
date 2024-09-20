package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.BloodRitualAltarBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.PedestalBlockEntity;
import org.chubby.github.bloodalchemyandrituals.common.blocks.entity.RitualAltarBlockEntity;

import java.util.function.Supplier;

public class BARBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MODID);

    public static final Supplier<BlockEntityType<BloodRitualAltarBlockEntity>> BLOOD_RITUAL_ALTAR_BE =
            BLOCK_ENTITIES.register("blood_ritual_altar_be",
                    ()-> BlockEntityType.Builder.of(BloodRitualAltarBlockEntity::new,BARBlocks.BLOOD_RITUAL_ALTAR.get()).build(null));

    public static final Supplier<BlockEntityType<RitualAltarBlockEntity>> RITUAL_ALTAR_BE =
            BLOCK_ENTITIES.register("ritual_altar_be",
                    ()-> BlockEntityType.Builder.of(RitualAltarBlockEntity::new,BARBlocks.RITUAL_ALTAR.get()).build(null));

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL_BE =
            BLOCK_ENTITIES.register("pedestal_be",
                    ()-> BlockEntityType.Builder.of(PedestalBlockEntity::new,BARBlocks.PEDESTAL_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus)
    {
        BLOCK_ENTITIES.register(eventBus);
    }
}
