package org.chubby.github.bloodalchemyandrituals.common.items.custom;


import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.Constants;
import org.chubby.github.bloodalchemyandrituals.common.init.BARItems;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BARArmorMaterials
{
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Constants.MODID);

    public static final Holder<ArmorMaterial> BLOOD_INFUSED =
            ARMOR_MATERIALS.register("blood_infused",
                    ()-> new ArmorMaterial(
                            Util.make(new EnumMap<>(ArmorItem.Type.class),map ->{
                                map.put(ArmorItem.Type.HELMET,3);
                                map.put(ArmorItem.Type.CHESTPLATE,7);
                                map.put(ArmorItem.Type.LEGGINGS,5);
                                map.put(ArmorItem.Type.BOOTS,4);
                            }),
                            26,
                            SoundEvents.ARMOR_EQUIP_NETHERITE,
                            ()->Ingredient.of(BARItems.BLOOD_INFUSED_INGOT),
                            List.of(
                                    new ArmorMaterial.Layer(
                                            Constants.customLocation("blood_infused")
                                    )
                            ),
                            1,
                            1
                    ));

    public static void register(IEventBus eventBus)
    {
        ARMOR_MATERIALS.register(eventBus);
    }
}
