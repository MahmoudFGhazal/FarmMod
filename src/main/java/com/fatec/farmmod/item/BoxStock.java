package com.fatec.farmmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BoxStock extends Item {

    // Constantes para definir o número máximo de stacks e o intervalo de atualização
    private static final int MAX_STACK = 100; // Quantidade máxima de stacks
    private static final int INTERVAL_STACK = 500; // Intervalo entre atualizações de stack (em ticks)
    private int tickCount = 0; // Contador de ticks
    private static final String COUNT_TAG = "box_stock_count"; // Tag para armazenar o número de stacks no item

    public BoxStock(@NotNull Properties properties) {
        super(properties.stacksTo(1)); // Define o item para permitir apenas 1 item por slot de inventário
    }

    // Obtém o valor de "count" (quantidade de stacks) do ItemStack
    private int getCount(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag(); // Obtém ou cria a tag NBT para o ItemStack
        return tag.getInt(COUNT_TAG); // Retorna o valor de "count" armazenado na tag
    }

    // Define o valor de "count" (quantidade de stacks) do ItemStack
    private void setCount(ItemStack stack, int count) {
        CompoundTag tag = stack.getOrCreateTag(); // Obtém ou cria a tag NBT para o ItemStack
        tag.putInt(COUNT_TAG, count); // Atualiza o valor de "count" na tag
    }

    /**
     * Atualiza o item a cada tick do mundo.
     * Se o item não estiver no lado do cliente e o jogador for um ServerPlayer, o item pode aumentar sua quantidade
     * se não atingir o limite máximo.
     */
    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isSelected) {
        if (!world.isClientSide && entity instanceof ServerPlayer) { // Certifica-se de que é um servidor e um jogador
            tickCount++; // Incrementa o contador de ticks
            if (tickCount >= INTERVAL_STACK) { // Verifica se atingiu o intervalo de atualização
                tickCount = 0; // Reseta o contador de ticks
                int currentCount = getCount(stack); // Obtém a quantidade atual de stacks
                if (currentCount < MAX_STACK) { // Verifica se a quantidade de stacks é menor que o máximo
                    setCount(stack, currentCount + 1); // Aumenta a quantidade de stacks em 1
                }
            }
        }
    }

    /**
     * Quando o item é usado em um bloco, ele tenta colocar um bloco aleatório se houver stacks disponíveis.
     */
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        Level world = pContext.getLevel(); // Obtém o mundo onde a ação ocorre
        BlockPos position = pContext.getClickedPos().relative(pContext.getClickedFace()); // Posição do bloco em que o item foi usado
        ItemStack stack = pContext.getItemInHand(); // O ItemStack usado pelo jogador
        Player player = pContext.getPlayer(); // O jogador que usou o item

        if (!world.isClientSide) { // Verifica se não está no lado cliente
            int currentCount = getCount(stack); // Obtém o valor atual de stacks do item
            if (currentCount > 0) { // Verifica se há stacks disponíveis para usar
                Block aboveBlock = pContext.getLevel().getBlockState(position).getBlock(); // Bloco acima da posição clicada
                if (ItemUtils.checkHeight(position, aboveBlock)) { // Verifica se a altura é válida para colocar um bloco
                    Block block = ItemUtils.getRandomBlock(0); // Obtém um bloco aleatório para colocar
                    world.setBlock(position, block.defaultBlockState(), 3); // Coloca o bloco aleatório no mundo
                    setCount(stack, currentCount - 1); // Reduz o número de stacks do item
                    return InteractionResult.SUCCESS; // Ação bem-sucedida
                } else {
                    if (player != null) {
                        player.displayClientMessage(Component.literal("Não é possivel colocar bloco nesse lugar"), true); // Mensagem de erro para o jogador
                    }
                }
            } else {
                if (player != null) {
                    player.displayClientMessage(Component.literal("Sem usos disponivel"), true); // Mensagem de erro se não houver stacks
                }
                return InteractionResult.FAIL; // Falha na interação
            }
        }
        return InteractionResult.PASS; // Retorna "pass" se a interação não foi bem-sucedida
    }

    /**
     * Adiciona o texto informativo sobre o número de stacks disponíveis ao item no inventário.
     */
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, List<Component> tooltip, net.minecraft.world.item.@NotNull TooltipFlag flag) {
        int count = getCount(stack); // Obtém o valor atual de "count" (quantidade de stacks) do ItemStack
        tooltip.add(Component.translatable("item.farmmod.boxstock.stacks", count)); // Adiciona o texto que exibe o número de stacks
    }
}
