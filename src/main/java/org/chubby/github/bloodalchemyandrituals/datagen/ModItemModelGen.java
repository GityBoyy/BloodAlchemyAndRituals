package org.chubby.github.bloodalchemyandrituals.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.blocks.custom.SkullBlock;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlocks;
import org.chubby.github.bloodalchemyandrituals.common.init.BARItems;

public class ModItemModelGen extends ItemModelProvider {

    public ModItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        basicItem(BARItems.BLOOD_INFUSED_INGOT.asItem());
        for(DeferredHolder<Item, ? extends Item> item : BARItems.ITEMS.getEntries())
        {
            if (!(item.get() instanceof TieredItem)) {
                if (!(item.get() instanceof BlockItem)) {

                    basicItem(item.get());
                }
            } else if (item.get() instanceof TieredItem) {
                // Handle TieredItem
                handheldItem((DeferredItem<Item>) item);
            }

        }
    }

    private ItemModelBuilder handheldItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Constants.MODID,"item/" + item.getId().getPath()));
    }
}
