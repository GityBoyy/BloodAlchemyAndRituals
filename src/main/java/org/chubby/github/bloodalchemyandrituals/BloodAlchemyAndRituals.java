package org.chubby.github.bloodalchemyandrituals;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.chubby.github.bloodalchemyandrituals.client.renderer.blockEntities.PedestalRenderer;
import org.chubby.github.bloodalchemyandrituals.client.renderer.blockEntities.RitualAltarBERenderer;
import org.chubby.github.bloodalchemyandrituals.client.renderer.blockEntities.RitualAltarRenderer;
import org.chubby.github.bloodalchemyandrituals.common.init.BARBlockEntities;
import org.chubby.github.bloodalchemyandrituals.util.ModInitializers;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Constants.MODID)
public class BloodAlchemyAndRituals {

    public BloodAlchemyAndRituals(IEventBus modEventBus, ModContainer modContainer) {

        ModInitializers.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(BARBlockEntities.PEDESTAL_BE.get(), PedestalRenderer::new);
            event.registerBlockEntityRenderer(BARBlockEntities.RITUAL_ALTAR_BE.get(), RitualAltarBERenderer::new);
            event.registerBlockEntityRenderer(BARBlockEntities.BLOOD_RITUAL_ALTAR_BE.get(), RitualAltarRenderer::new);
        }
    }
}
