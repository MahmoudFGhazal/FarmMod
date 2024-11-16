package com.fatec.farmmod.item;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ItemUtils {

    public static Random rand = new Random();

    public static final Map<String, List<String>> evolutions = Map.of(
            "Iron", List.of("Iron Ore", "Block of Raw Iron", "Block of Iron"),
            "Gold", List.of("Gold Ore", "Block of Raw Gold", "Block of Gold"),
            "Copper", List.of("Copper Ore", "Block of Raw Copper", "Block of Copper"),
            "Stone", List.of("Cobblestone", "Stone", "Smooth Stone"),
            "Wood", List.of("Cherry Log", "Stripped Cherry Log", "Cherry Planks")
    );

    public static List<String> getEvolutionsList(String blockName){
        return evolutions.values().stream()
                .filter(strings -> strings.contains(blockName))
                .findFirst()
                .orElse(Collections.emptyList());
    }

    public static Block getBlockByName(String blockName){
        return ForgeRegistries.BLOCKS.getValues()
                .stream()
                .filter(block -> block.getName().getString().equals(blockName))
                .findFirst()
                .orElse(null);
    }

    public static Block getNextEvolutions(String blockName){
        List<String> evolutionList = getEvolutionsList(blockName);

        if(!evolutionList.isEmpty()){
            int currentIndex = evolutionList.indexOf(blockName);
            if(currentIndex >= 0 && currentIndex < evolutionList.size() - 1){
                return getBlockByName(evolutionList.get(currentIndex + 1));
            }
        }

        return null;
    }

    public static boolean checkList(String blockName){
        return evolutions.values().stream()
                .anyMatch(list -> list.contains(blockName));
    }

    public static boolean chance(int percent){
        return rand.nextInt(100) > percent;
    }

}
