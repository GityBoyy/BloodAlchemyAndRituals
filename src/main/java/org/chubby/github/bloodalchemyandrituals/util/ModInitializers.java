package org.chubby.github.bloodalchemyandrituals.util;

import net.neoforged.bus.api.IEventBus;
import org.chubby.github.bloodalchemyandrituals.common.fluid.BARFluidType;
import org.chubby.github.bloodalchemyandrituals.common.init.*;
import org.chubby.github.bloodalchemyandrituals.common.items.custom.BARArmorMaterials;

public class ModInitializers
{

    public static void register(IEventBus eventBus)
    {
        BARItems.register(eventBus);
        BARBlocks.register(eventBus);
        BARTabs.register(eventBus);
        BARFluids.register(eventBus);
        BARFluidType.register(eventBus);
        BARBlockEntities.register(eventBus);
        BARRecipes.register(eventBus);
        BARRecipeSerializers.register(eventBus);
        BARArmorMaterials.register(eventBus);
    }


}
