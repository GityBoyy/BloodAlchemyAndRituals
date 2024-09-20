package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.blocks.custom.BloodRitualAltar;
import org.chubby.github.bloodalchemyandrituals.common.blocks.custom.PedestalBlock;
import org.chubby.github.bloodalchemyandrituals.common.blocks.custom.RitualAltarBlock;
import org.chubby.github.bloodalchemyandrituals.common.blocks.custom.SkullBlock;

import java.util.function.Supplier;

public class BARBlocks
{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MODID);

    public static final DeferredBlock<Block> SKULL_BLOCK = registerBlock("skull_block",
            ()-> new SkullBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<Block> BLOOD_RITUAL_ALTAR = registerBlock("blood_ritual_altar",
            ()-> new BloodRitualAltar(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> RITUAL_ALTAR = registerBlock("ritual_altar",
            ()-> new RitualAltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> PEDESTAL_BLOCK = registerBlock("pedestal_block",
            ()-> new PedestalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops()));

    public static final DeferredBlock<LiquidBlock> BLOOD_BLOCK = BLOCKS.register("blood_block",
            ()-> new LiquidBlock(BARFluids.BLOOD_FLOWING.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).requiresCorrectToolForDrops()));

    public static<T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T>sup)
    {
            return registerBlock(name,sup,true);
    }

    public static<T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T>sup,boolean putInTab)
    {
        DeferredBlock<T> regObj = BLOCKS.register(name,sup);
        BARItems.registerItem(name,()-> new BlockItem(regObj.get(),new Item.Properties()),false);
        if(putInTab)
        {
            BARTabs.BLOCKS.add(regObj);
        }
        return regObj;
    }



    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
