package org.chubby.github.bloodalchemyandrituals;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants
{
    public static final String MODID = "bloodalchemyandrituals";

    public static final Logger LOGGER = LogManager.getLogger("BloodAlchemyAndRituals");

    public static ResourceLocation customLocation(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID,path);
    }



    public static Component translatable(String name)
    {
        return Component.translatable(MODID+"."+name);
    }
}
