package org.chubby.github.bloodalchemyandrituals.common.init;

import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.items.BloodBottle;
import org.chubby.github.bloodalchemyandrituals.common.items.custom.BARArmorMaterials;
import org.chubby.github.bloodalchemyandrituals.common.items.custom.BARWeaponTiers;

import java.util.function.Supplier;

public class BARItems
{
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MODID);

    public static final DeferredItem<Item> FILLED_BLOOD_BOTTLE = registerItem("filled_blood_bottle",
            ()-> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BLOOD_BOTTLE = registerItem("blood_bottle",
            ()-> new BloodBottle(new Item.Properties(),FILLED_BLOOD_BOTTLE.asItem()),false);

    public static final DeferredItem<Item> BLOOD_INFUSED_INGOT = registerItem("blood_infused_ingot",
            ()-> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));

    //ArmorItems
    public static final DeferredItem<Item> BLOOD_INFUSED_HELMET = registerItem("blood_infused_helmet",
            ()-> new ArmorItem(BARArmorMaterials.BLOOD_INFUSED, ArmorItem.Type.HELMET,new Item.Properties()));
    public static final DeferredItem<Item> BLOOD_INFUSED_CHESTPLATE = registerItem("blood_infused_chestplate",
            ()-> new ArmorItem(BARArmorMaterials.BLOOD_INFUSED, ArmorItem.Type.HELMET,new Item.Properties()));
    public static final DeferredItem<Item> BLOOD_INFUSED_LEGGINGS = registerItem("blood_infused_leggings",
            ()-> new ArmorItem(BARArmorMaterials.BLOOD_INFUSED, ArmorItem.Type.HELMET,new Item.Properties()));
    public static final DeferredItem<Item> BLOOD_INFUSED_BOOTS = registerItem("blood_infused_boots",
            ()-> new ArmorItem(BARArmorMaterials.BLOOD_INFUSED, ArmorItem.Type.HELMET,new Item.Properties()));

    //Weapons
    public static final DeferredItem<Item> BLOOD_INFUSED_SWORD = registerItem("blood_infused_sword",
            ()-> new SwordItem(BARWeaponTiers.BLOOD_INFUSED,new Item.Properties().fireResistant().setNoRepair()));
    public static final DeferredItem<Item> BLOOD_INFUSED_PICKAXE = registerItem("blood_infused_pickaxe",
            ()-> new PickaxeItem(BARWeaponTiers.BLOOD_INFUSED,new Item.Properties().fireResistant().setNoRepair()));
    public static final DeferredItem<Item> BLOOD_INFUSED_AXE = registerItem("blood_infused_axe",
            ()-> new AxeItem(BARWeaponTiers.BLOOD_INFUSED,new Item.Properties().fireResistant().setNoRepair()));
    public static final DeferredItem<Item> BLOOD_INFUSED_SHOVEL = registerItem("blood_infused_shovel",
            ()-> new ShovelItem(BARWeaponTiers.BLOOD_INFUSED,new Item.Properties().fireResistant().setNoRepair()));
    public static final DeferredItem<Item> BLOOD_INFUSED_HOE = registerItem("blood_infused_hoe",
            ()-> new HoeItem(BARWeaponTiers.BLOOD_INFUSED,new Item.Properties().fireResistant().setNoRepair()));


    public static<T extends Item> DeferredItem<T> registerItem(String name,Supplier<T> sup)
    {
        return registerItem(name,sup,true);
    }

    public static<T extends Item> DeferredItem<T> registerItem(String name, Supplier<T> sup,boolean putInTab)
    {
        DeferredItem<T> regObj = ITEMS.register(name,sup);
        if(putInTab)
        {
            BARTabs.ITEMS.add(regObj);
        }
        return regObj;
    }

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
