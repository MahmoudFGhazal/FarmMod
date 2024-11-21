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

    // Quantidade mínima de esmeraldas necessárias para expandir a plataforma
    public static int EMERALDS_REQUIRED = 10;

    // Tamanho da expansão em cada direção (raio de expansão)
    public static final int EXPANSION_SIZE = 2;

    // Altura fixa da plataforma no mundo
    private static final int PLATFORM_Y = -63;

    /**
     * Expande a plataforma para o jogador especificado, se ele tiver recursos suficientes.
     * @param player O jogador que solicita a expansão.
     * @param world O mundo do servidor onde ocorre a expansão.
     */
    public static void expandPlatform(Player player, ServerLevel world) {
        // Define o centro da plataforma com base na posição do jogador (x e z do jogador, altura fixa)
        BlockPos platformCenter = new BlockPos(player.getBlockX(), PLATFORM_Y, player.getBlockZ());

        // Conta a quantidade de esmeraldas no inventário do jogador
        int emeraldCount = countEmeralds(player);

        // Verifica se o jogador tem esmeraldas suficientes
        if (emeraldCount >= EMERALDS_REQUIRED) {
            // Remove a quantidade necessária de esmeraldas do inventário do jogador
            removeEmeralds(player);

            // Expande a plataforma em um quadrado ao redor do centro
            for (int x = -EXPANSION_SIZE; x <= EXPANSION_SIZE; x++) {
                for (int z = -EXPANSION_SIZE; z <= EXPANSION_SIZE; z++) {
                    // Calcula a posição de cada bloco a ser colocado
                    BlockPos pos = platformCenter.offset(x, 0, z);

                    // Coloca blocos apenas onde não há nada ou onde o bloco existente não é "Dirt"
                    if (world.getBlockState(pos).isAir() || !world.getBlockState(pos).is(Blocks.DIRT)) {
                        world.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                    }
                }
            }

            // Envia uma mensagem de sucesso ao jogador
            player.sendSystemMessage(Component.literal("A plataforma foi expandida!").withStyle(ChatFormatting.GREEN));
        } else {
            // Informa ao jogador que ele não possui esmeraldas suficientes
            player.sendSystemMessage(Component.literal("Você precisa de pelo menos " + EMERALDS_REQUIRED + " esmeraldas para expandir a plataforma.").withStyle(ChatFormatting.RED));
        }
    }

    /**
     * Conta a quantidade total de esmeraldas no inventário do jogador.
     * @param player O jogador cujo inventário será verificado.
     * @return O número total de esmeraldas encontradas.
     */
    private static int countEmeralds(Player player) {
        int count = 0;
        // Itera sobre os slots do inventário do jogador
        for (ItemStack stack : player.getInventory().items) {
            // Verifica se o item no slot é uma esmeralda
            if (stack.getItem() == Items.EMERALD) {
                count += stack.getCount(); // Soma a quantidade de esmeraldas
            }
        }
        return count;
    }

    /**
     * Remove a quantidade necessária de esmeraldas do inventário do jogador.
     * @param player O jogador cujo inventário será alterado.
     */
    private static void removeEmeralds(Player player) {
        int amount = EMERALDS_REQUIRED; // Quantidade a ser removida
        // Itera sobre os slots do inventário do jogador
        for (ItemStack stack : player.getInventory().items) {
            // Verifica se o item no slot é uma esmeralda
            if (stack.getItem() == Items.EMERALD) {
                // Calcula a quantidade a ser removida deste slot
                int removed = Math.min(stack.getCount(), amount);
                stack.shrink(removed); // Reduz a quantidade no slot
                amount -= removed; // Atualiza a quantidade restante a ser removida
                if (amount <= 0) {
                    break; // Sai do loop quando todas as esmeraldas necessárias forem removidas
                }
            }
        }
    }
}
