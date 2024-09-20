package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BARTabs
{

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MODID);

    public static final List<Supplier<? extends Block>> BLOCKS = new ArrayList<>();
    public static final List<Supplier<? extends Item>> ITEMS = new ArrayList<>();

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> BLOCK_TAB = TABS.register("blocks",()->CreativeModeTab.builder()
            .title(Component.translatable("itemGroup."+Constants.MODID+"blocks"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(()-> ItemStack.EMPTY)
            .displayItems((itemDisplayParameters, output) -> {
                BLOCKS.forEach(block-> output.accept(block.get()));
            })
            .build()
    );

    public static final DeferredHolder<CreativeModeTab,CreativeModeTab> ITEM_TAB = TABS.register("items",()->CreativeModeTab.builder()
            .title(Component.translatable("itemGroup."+ Constants.MODID+"items"))
            .withTabsBefore(BLOCK_TAB.getKey())
            .icon(()->ItemStack.EMPTY)
            .displayItems((itemDisplayParameters, output) -> {
                ITEMS.forEach(item-> output.accept(item.get()));
            })
            .build()
    );

    public static void register(IEventBus eventBus)
    {
        TABS.register(eventBus);
    }
}