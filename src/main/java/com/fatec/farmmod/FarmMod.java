package com.fatec.farmmod;

import com.fatec.farmmod.item.ModCreativeModeTabs;
import com.fatec.farmmod.item.ModItems;
import com.fatec.farmmod.event.ModEvents;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab; // Importação necessária
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(FarmMod.MOD_ID)
public class FarmMod {
    public static final String MOD_ID = "farmmod";

    private static final Logger LOGGER = LogUtils.getLogger();


    public FarmMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModeTabs.register(modEventBus); // Registro das abas criativas
        ModItems.register(modEventBus); // Registro dos itens

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


    //muda o jogador pro modo aventura independentemente do modo que escolher
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getInventory().contains(new ItemStack(ModItems.FUSION.get()))) {
            player.getInventory().add(new ItemStack(ModItems.FUSION.get()));
            player.getInventory().add(new ItemStack(ModItems.MOVER.get()));
            player.getInventory().add(new ItemStack(ModItems.BOXSTOCK.get()));
        }
    }

    /*
    //não deixa o jogador mudar de modo de jogo(fica no aventura)
    @SubscribeEvent
    public static void onPLayerChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event) {
        if(event.getNewGameMode() != GameType.ADVENTURE){
            event.setCanceled(true);
            event.getEntity().sendSystemMessage(Component.literal("você não pode mudar o modo de jogo!"));
        }
    }
    */
}
