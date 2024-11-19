package com.fatec.farmmod.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class PlatformExpander {

    public static final int EMERALDS_REQUIRED = 10; // Quantidade de esmeraldas para expandir
    public static final int EXPANSION_SIZE = 2; // Tamanho de expansão em cada direção

    // Coordenadas fixas da plataforma
    private static final int PLATFORM_Y = -63;

    public static void expandPlatform(Player player, ServerLevel world) {
        // Coordenadas fixas da plataforma (x baseado no jogador, y fixo, z baseado no jogador)
        BlockPos platformCenter = new BlockPos(player.getBlockX(), PLATFORM_Y, player.getBlockZ());

        // Verifica se o jogador possui esmeraldas suficientes
        int emeraldCount = countEmeralds(player);

        if (emeraldCount >= EMERALDS_REQUIRED) {
            // Remove esmeraldas do inventário
            removeEmeralds(player, EMERALDS_REQUIRED);

            // Expande a plataforma
            for (int x = -EXPANSION_SIZE; x <= EXPANSION_SIZE; x++) {
                for (int z = -EXPANSION_SIZE; z <= EXPANSION_SIZE; z++) {
                    BlockPos pos = platformCenter.offset(x, 0, z);

                    // Apenas coloca blocos onde não há nada ou onde o bloco não é parte da plataforma
                    if (world.getBlockState(pos).isAir() || !world.getBlockState(pos).is(Blocks.DIRT)) {
                        world.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                    }
                }
            }

            player.sendSystemMessage(Component.literal("A plataforma foi expandida!").withStyle(ChatFormatting.GREEN));
        } else {
            player.sendSystemMessage(Component.literal("Você precisa de pelo menos " + EMERALDS_REQUIRED + " esmeraldas para expandir a plataforma.").withStyle(ChatFormatting.RED));
        }
    }

    private static int countEmeralds(Player player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == Items.EMERALD) {
                count += stack.getCount();
            }
        }
        return count;
    }

    private static void removeEmeralds(Player player, int amount) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == Items.EMERALD) {
                int removed = Math.min(stack.getCount(), amount);
                stack.shrink(removed);
                amount -= removed;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }
}