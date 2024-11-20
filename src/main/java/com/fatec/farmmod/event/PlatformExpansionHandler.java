package com.fatec.farmmod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlatformExpansionHandler {

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            // Verifica se o jogador está segurando uma esmeralda
            if (event.getEntity().getMainHandItem().getItem() == Items.EMERALD) {
                PlatformExpander.expandPlatform(event.getEntity(), serverLevel);
                event.setCanceled(true);
            }
        }
    }
}