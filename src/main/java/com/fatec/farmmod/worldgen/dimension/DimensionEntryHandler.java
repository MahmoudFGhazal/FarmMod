package com.fatec.farmmod.worldgen.dimension;

import com.fatec.farmmod.worldgen.portal.ModTeleporter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DimensionEntryHandler {
    @SubscribeEvent
    public static void onRightClickPinkWool(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer serverPlayer) {
            // Verifica se o bloco clicado é de lã rosa
            if (event.getLevel().getBlockState(event.getPos()).getBlock() == Blocks.PINK_WOOL) {
                ModTeleporter.teleportToFarmModDimension(serverPlayer);
            }
        }
    }
}
