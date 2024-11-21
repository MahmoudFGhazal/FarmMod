package com.fatec.farmmod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PlatformExpansionHandler {

    // Armazena o estado do botão e tempo de cooldown para cada jogador
    private static final HashMap<UUID, Integer> playerCooldown = new HashMap<>();

    private static final int COOLDOWN_TICKS = 10; // Define um intervalo de 10 ticks (~0.5 segundos)

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel) {
            UUID playerId = event.getEntity().getUUID();

            // Obtém o tempo de cooldown restante ou 0
            int cooldown = playerCooldown.getOrDefault(playerId, 0);

            // Verifica se o cooldown já terminou
            if (cooldown <= 0) {
                if (event.getEntity().getMainHandItem().getItem() == Items.EMERALD) {
                    PlatformExpander.expandPlatform(event.getEntity(), serverLevel);

                    // Reinicia o cooldown
                    playerCooldown.put(playerId, COOLDOWN_TICKS);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        UUID playerId = event.player.getUUID();

        // Reduz o cooldown do jogador a cada tick, se necessário
        int cooldown = playerCooldown.getOrDefault(playerId, 0);
        if (cooldown > 0) {
            playerCooldown.put(playerId, cooldown - 1);
        }
    }
}
