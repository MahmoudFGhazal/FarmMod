package com.fatec.farmmod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlatformExpansionHandler {

    // Método que lida com o evento de clique do jogador com o botão direito em um bloco
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        // Verifica se o mundo é do tipo ServerLevel (mundo do servidor)
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            // Verifica se o jogador está segurando uma esmeralda na mão principal
            if (event.getEntity().getMainHandItem().getItem() == Items.EMERALD) {
                // Expande a plataforma usando o método 'expandPlatform' da classe PlatformExpander
                PlatformExpander.expandPlatform(event.getEntity(), serverLevel);
                // Cancela o evento para evitar que o comportamento padrão do clique ocorra
                event.setCanceled(true);
            }
        }
    }
}
