package com.codedex.soulmod;

import com.codedex.soulmod.block.ModBlocks;
import com.codedex.soulmod.blockentity.ModBlockEntities;
import com.codedex.soulmod.client.MournerModel;
import com.codedex.soulmod.client.MournerRenderer;
import com.codedex.soulmod.entity.ModEntities;
import com.codedex.soulmod.item.ModItems;
import com.codedex.soulmod.menu.ModMenuTypes;
import com.codedex.soulmod.screen.SoulCompressorScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SoulMod.MOD_ID)
public class SoulMod {
    public static final String MOD_ID = "soulmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SoulMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 1. Enregistrement des objets (Ordre important)
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        // 2. Enregistrement des événements de chargement
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::recordAttributes); //record des charastéristiques du mourner

        // 3. Enregistrement sur le bus Forge global
        MinecraftForge.EVENT_BUS.register(this);

        // 4. Configuration (Optionnel)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Logique commune serveur/client
    }

    // Ajoute tes items dans les onglets du mode Créatif
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.SOUL_DUST);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.SOUL_STAFF);
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.SOUL_COMPRESSOR);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.MOURNER_SPAWN_EGG);
        }
    }

    // CONFIGURATION CLIENT (Dit à Minecraft d'ouvrir la Screen quand on appelle le Menu)
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.SOUL_COMPRESSOR_MENU.get(), SoulCompressorScreen::new);
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Logique au démarrage du serveur
    }

    // Gestion automatique des événements côté Client uniquement
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // (possible réglages client a venir)
        }

        // generation du mourner dans le serv
        // generation du mourner lui meme ( cube )
        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(MournerModel.LAYER_LOCATION, MournerModel::createBodyLayer);
        }

        // apply du skin du mourner et de l'apparance de la charge au projectile
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.MOURNER.get(), MournerRenderer::new);
            event.registerEntityRenderer(ModEntities.MOURNER_FIREBALL.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        }
    }

    // liaison entre le mourner et ses attributs phys.
    // comme ca l'event en haut peu utiliser ces infos pour les records en haut
    private void recordAttributes(net.minecraftforge.event.entity.EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOURNER.get(), com.codedex.soulmod.entity.MournerEntity.createAttributes().build());
    }
}