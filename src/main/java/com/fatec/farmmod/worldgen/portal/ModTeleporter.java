package com.fatec.farmmod.worldgen.portal;

import com.fatec.farmmod.worldgen.dimension.ModDimension;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ModTeleporter {

    public static void teleportToFarmModDimension(ServerPlayer player) {
        ServerLevel farmmodWorld = player.getServer().getLevel(ModDimension.FARMMODDIM_LEVEL_KEY);

        if (farmmodWorld != null) {
            BlockPos spawnPos = new BlockPos(0, 65, 0); // Define a posição de teletransporte

            // Teletransporta o jogador para a posição definida na dimensão `farmmod`
            player.teleportTo(farmmodWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYRot(), player.getXRot());
            player.sendSystemMessage(Component.literal("Bem-vindo à dimensão FarmMod!"));
        }
    }
}
