package com.fatec.farmmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ItemUtils {

    // Instancia um gerador de números aleatórios
    public static Random rand = new Random();

    // Mapa que associa tipos de blocos (como "Iron", "Gold") com suas respectivas evoluções
    public static final Map<String, List<String>> evolutions = Map.of(
            "Iron", List.of("Iron Ore", "Block of Raw Iron", "Block of Iron"),
            "Gold", List.of("Gold Ore", "Block of Raw Gold", "Block of Gold"),
            "Copper", List.of("Copper Ore", "Block of Raw Copper", "Block of Copper"),
            "Stone", List.of("Cobblestone", "Stone", "Smooth Stone"),
            "Wood", List.of("Cherry Wood", "Stripped Cherry Log", "Cherry Planks")
    );

    /**
     * Retorna uma lista de evoluções de um bloco específico.
     * @param blockName Nome do bloco.
     * @return Lista de blocos que são evoluções do bloco fornecido.
     */
    public static List<String> getEvolutionsList(String blockName){
        return evolutions.values().stream()
                .filter(strings -> strings.contains(blockName))  // Filtra as listas de evolução que contém o nome do bloco
                .findFirst()
                .orElse(Collections.emptyList());  // Retorna uma lista vazia caso o bloco não tenha evolução
    }

    /**
     * Retorna um bloco pelo nome.
     * @param blockName Nome do bloco.
     * @return O bloco correspondente ao nome, ou null se não encontrar.
     */
    public static Block getBlockByName(String blockName){
        return ForgeRegistries.BLOCKS.getValues()
                .stream()
                .filter(block -> block.getName().getString().equals(blockName))  // Filtra pelo nome do bloco
                .findFirst()
                .orElse(null);  // Retorna null se o bloco não for encontrado
    }

    /**
     * Retorna um item pelo nome.
     * @param itemName Nome do item.
     * @return O item correspondente ao nome, ou null se não encontrar.
     */
    public static Item getItemByName(String itemName) {
        for (Item item : ForgeRegistries.ITEMS) {
            String translatedName = Component.translatable(item.getDescriptionId()).getString();  // Obtém o nome traduzido do item
            if (translatedName.equals(itemName)) {  // Compara o nome traduzido com o nome fornecido
                Optional<ResourceLocation> opitem = Optional.ofNullable(ForgeRegistries.ITEMS.getKey(item));
                if(opitem.isPresent()){
                    return ForgeRegistries.ITEMS.getValue(opitem.get());  // Retorna o item se encontrado
                }
            }
        }
        return null;  // Retorna null se o item não for encontrado
    }

    /**
     * Retorna o próximo estágio de evolução de um bloco.
     * @param blockName Nome do bloco.
     * @return O próximo estágio de evolução do bloco ou uma mensagem indicando que não há mais evolução.
     */
    public static String getNextEvolutions(String blockName){
        List<String> evolutionList = getEvolutionsList(blockName);  // Obtém a lista de evolução do bloco

        if(!evolutionList.isEmpty()){
            int currentIndex = evolutionList.indexOf(blockName);  // Encontra o índice do bloco atual na lista
            if(currentIndex < evolutionList.size() - 1){
                return evolutionList.get(currentIndex + 1);  // Retorna o próximo estágio de evolução
            }else{
                return "Items: " + evolutionList.get(currentIndex);  // Retorna o bloco final se não houver próximo estágio
            }
        }

        return null;  // Retorna null se o bloco não tiver evolução
    }

    /**
     * Retorna um bloco aleatório de uma das listas de evolução.
     * @param index Índice da evolução na lista.
     * @return Um bloco aleatório baseado no índice fornecido.
     */
    public static Block getRandomBlock(int index){
        Object[] keys = evolutions.keySet().toArray();  // Obtém as chaves (tipos de blocos) do mapa de evoluções
        String randomKey = (String) keys[random(keys.length)];  // Escolhe uma chave aleatória

        List<String> randomList = evolutions.get(randomKey);  // Obtém a lista de evolução correspondente à chave
        return getBlockByName(randomList.get(index));  // Retorna o bloco no índice especificado da lista de evolução
    }

    /**
     * Verifica se o nome do bloco existe em alguma lista de evolução.
     * @param blockName Nome do bloco.
     * @return Verdadeiro se o bloco estiver presente em alguma lista de evolução, falso caso contrário.
     */
    public static boolean checkList(String blockName){
        return evolutions.values().stream()
                .anyMatch(list -> list.contains(blockName));  // Verifica se o nome do bloco está presente em alguma lista
    }

    /**
     * Retorna um número aleatório entre 0 e o valor fornecido.
     * @param number Valor máximo para o número aleatório.
     * @return Número aleatório.
     */
    public static int random(int number){
        return rand.nextInt(number);  // Gera um número aleatório entre 0 e o valor fornecido
    }

    /**
     * Verifica se uma posição é válida para colocar um bloco.
     * @param position Posição do bloco.
     * @param aboveBlock Bloco acima da posição.
     * @return Verdadeiro se a altura for -62 e o bloco acima for ar, falso caso contrário.
     */
    public static boolean checkHeight(BlockPos position, Block aboveBlock){
        return position.getY() == -62 && aboveBlock == Blocks.AIR;  // Verifica a altura e se o bloco acima é ar
    }
}
