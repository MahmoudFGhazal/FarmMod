package com.fatec.farmmod;

import com.fatec.farmmod.item.ModCreativeModeTabs;
import com.fatec.farmmod.item.ModItems;
import com.fatec.farmmod.block.ModBlock;
import com.fatec.farmmod.event.ModEvents;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab; // Importação necessária
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(FarmMod.MOD_ID)
public class FarmMod {
    public static final String MOD_ID = "farmmod";

    private static final Logger LOGGER = LogUtils.getLogger();
    public static Map<String, List<String>> evolutions = new HashMap<>();

    public FarmMod(FMLJavaModLoadingContext context) {
        String[] irons = {"Iron Ore", "Block of Raw Iron", "Block of Iron"};
        evolutions.put("Iron", Arrays.asList(irons));
        String[] golds = {"Gold Ore", "Block of Raw Gold", "Block of Gold"};
        evolutions.put("Gold", Arrays.asList(golds));
        String[] coppers = {"Copper Ore", "Block of Raw Copper", "Block of Copper"};
        evolutions.put("Copper", Arrays.asList(coppers));
        String[] stones = {"Cobblestone", "Stone", "Smooth Stone"};
        evolutions.put("Stone", Arrays.asList(stones));
        String[] woods = {"Cherry Log", "Stripped Cherry Log", "Cherry Planks"};
        evolutions.put("Wood", Arrays.asList(woods));

        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModeTabs.register(modEventBus); // Registro das abas criativas
        ModItems.register(modEventBus); // Registro dos itens
        ModBlock.register(modEventBus); // Registro dos blocos

        modEventBus.addListener(this::commonSetup); // Configuração comum
        MinecraftForge.EVENT_BUS.register(this); // Registro de eventos

        MinecraftForge.EVENT_BUS.register(ModEvents.class);

        modEventBus.addListener(this::addCreative); // Adiciona itens às abas criativas
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Configurações comuns que não dependem do cliente
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Correção aqui
        ResourceKey<CreativeModeTab> farmTabKey = ModCreativeModeTabs.FARM_TAB.getKey();
        if (event.getTabKey().equals(farmTabKey)) {
            event.accept(ModItems.FUSION); // Adiciona o item Fusion à aba de ingredientes
            event.accept(ModItems.BOXSTOCK);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Lógica quando o servidor está iniciando
    }

    // Classe interna para eventos do cliente
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Configurações específicas do cliente
        }
    }
}
