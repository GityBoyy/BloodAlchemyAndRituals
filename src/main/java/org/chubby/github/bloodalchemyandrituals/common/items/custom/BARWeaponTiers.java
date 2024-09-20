package org.chubby.github.bloodalchemyandrituals.common.items.custom;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.chubby.github.bloodalchemyandrituals.common.init.BARItems;

public class BARWeaponTiers
{
    public static final Tier BLOOD_INFUSED = new SimpleTier(
            Tags.Blocks.STONES,
            1200,
            2.5F,
            ((float) Math.sin(280.000D)),
            26,
            ()-> Ingredient.of(BARItems.BLOOD_INFUSED_INGOT)
    );
}
