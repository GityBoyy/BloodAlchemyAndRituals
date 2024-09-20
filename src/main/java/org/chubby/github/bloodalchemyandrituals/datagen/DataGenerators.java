package org.chubby.github.bloodalchemyandrituals.datagen;

import io.netty.util.Constant;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.chubby.github.bloodalchemyandrituals.Constants;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        generator.addProvider(event.includeServer(),new RecipeGen(output,provider));
        generator.addProvider(event.includeServer(),new ModItemModelGen(output,existingFileHelper));
    }
}
