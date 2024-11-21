package com.fatec.farmmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Fusion extends Item {
    // Lista que mantém os blocos que correspondem ao critério de fusão
    List<BlockPos> matchingBlocks = new ArrayList<>();
    // Conjunto para evitar visitar os mesmos blocos durante a verificação de vizinhos
    Set<BlockPos> visitedPositions = new HashSet<>();

    public Fusion(Properties pProperties) {
        super(pProperties.stacksTo(1));  // Define que apenas 1 item pode ser empilhado
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {  // Verifica se a ação está ocorrendo no lado do servidor
            BlockPos position = pContext.getClickedPos();  // Posição onde o jogador clicou
            Player player = pContext.getPlayer();  // Jogador que realizou a ação
            BlockState blockState = pContext.getLevel().getBlockState(position);  // Estado do bloco clicado
            Block block = blockState.getBlock();  // Bloco clicado
            String blockName = block.getName().getString();  // Nome do bloco clicado

            // Verifica se o bloco clicado corresponde a um bloco da lista de fusão
            if(ItemUtils.checkList(blockName) && player != null) {
                matchingBlocks.add(position);  // Adiciona a posição à lista de blocos correspondentes
                visitedPositions.add(position);  // Marca a posição como visitada
                checkNeighbors(block, position, pContext.getLevel());  // Verifica vizinhos do bloco clicado

                String nextBlockName = ItemUtils.getNextEvolutions(blockName);  // Obtém o próximo bloco após a fusão

                // Verifica se há blocos suficientes e se existe um próximo bloco válido para a fusão
                if(matchingBlocks.size() >= 3 && nextBlockName != null) {
                    deleteBlocks(pContext);  // Exclui os blocos correspondentes

                    int quantBlocks = matchingBlocks.size();
                    if (nextBlockName.startsWith("Items: ")) {  // Se o próximo nome for de item
                        nextBlockName = nextBlockName.substring("Items: ".length());  // Remove prefixo "Items: "
                        Item dropItem = ItemUtils.getItemByName(nextBlockName);  // Obtém o item
                        Level world = pContext.getLevel();
                        if(dropItem != null) {
                            // Cria uma entidade de item no mundo
                            ItemStack emeraldDrop = new ItemStack(dropItem, matchingBlocks.size());
                            world.addFreshEntity(new ItemEntity(
                                    world,
                                    position.getX() + 0.5,
                                    position.getY() + 0.5,
                                    position.getZ() + 0.5,
                                    emeraldDrop
                            ));
                        }
                    } else {
                        // Caso o próximo nome seja de bloco, obtém o bloco correspondente
                        Block nextBlock = ItemUtils.getBlockByName(nextBlockName);
                        if (quantBlocks % 5 == 0) {
                            // Caso o número de blocos seja múltiplo de 5, faz a fusão "sortuda"
                            for (int i = 0; i <= Math.floorDiv(quantBlocks, 5); i++) {
                                pContext.getLevel().setBlock(matchingBlocks.get(i++), nextBlock.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(matchingBlocks.get(i), nextBlock.defaultBlockState(), 2);
                            }
                            player.displayClientMessage(Component.literal("Lucky Merge"), true);  // Mensagem de fusão sortuda
                        } else {
                            // Caso contrário, realiza a fusão normal, com chance aleatória de sucesso
                            int i;
                            for (i = 0; i < Math.floorDiv(quantBlocks, 3); i++) {
                                if (ItemUtils.random(100) > 5) {
                                    pContext.getLevel().setBlock(matchingBlocks.get(i), nextBlock.defaultBlockState(), 2);
                                } else {
                                    pContext.getLevel().setBlock(matchingBlocks.get(i++), nextBlock.defaultBlockState(), 2);
                                    pContext.getLevel().setBlock(matchingBlocks.get(i), nextBlock.defaultBlockState(), 2);
                                    player.displayClientMessage(Component.literal("Lucky Merge"), true);  // Mensagem de fusão sortuda
                                }
                            }

                            // Faz o preenchimento dos blocos restantes
                            for (int p = i; p < quantBlocks % 3 + i; p++) {
                                pContext.getLevel().setBlock(matchingBlocks.get(p), block.defaultBlockState(), 2);
                            }
                        }
                    }

                } else {
                    // Caso o número de blocos seja insuficiente para a fusão
                    player.displayClientMessage(Component.literal("Quantidade de Blocos Insuficientes"), true);
                }
            }

            // Limpa as listas de blocos correspondentes e posições visitadas após a ação
            matchingBlocks.clear();
            visitedPositions.clear();
        }

        return InteractionResult.SUCCESS;  // Retorna sucesso para a interação
    }

    // Metodo para verificar os vizinhos de um bloco e adicionar os correspondentes à lista
    private void checkNeighbors(Block startblock, BlockPos position, Level level) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (y == x) continue;  // Impede a verificação diagonal

                BlockPos currentPos = position.offset(x, 0, y);  // Calcula a posição do vizinho

                if (visitedPositions.contains(currentPos)) continue;  // Ignora se o vizinho já foi visitado

                BlockState currentState = level.getBlockState(currentPos);
                Block currentBlock = currentState.getBlock();
                visitedPositions.add(currentPos);  // Marca o vizinho como visitado

                if (currentBlock == startblock) {  // Se o bloco é igual ao bloco inicial
                    matchingBlocks.add(currentPos);  // Adiciona à lista de blocos correspondentes
                    checkNeighbors(startblock, currentPos, level);  // Verifica os vizinhos do vizinho
                }
            }
        }
    }

    // Metodo que deleta os blocos correspondentes
    private void deleteBlocks(UseOnContext pContext){
        for (BlockPos posToDelete : matchingBlocks) {
            pContext.getLevel().setBlock(posToDelete, Blocks.AIR.defaultBlockState(), 2);  // Deleta o bloco

            // Substitui o bloco abaixo por grama se for terra
            Block belowBlock = pContext.getLevel().getBlockState(posToDelete.below()).getBlock();
            if(belowBlock == Blocks.DIRT)
                pContext.getLevel().setBlock(posToDelete.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
        }
    }
}
