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

    public static Random rand = new Random();

    public static final Map<String, List<String>> evolutions = Map.of(
            "Iron", List.of("Iron Ore", "Block of Raw Iron", "Block of Iron"),
            "Gold", List.of("Gold Ore", "Block of Raw Gold", "Block of Gold"),
            "Copper", List.of("Copper Ore", "Block of Raw Copper", "Block of Copper"),
            "Stone", List.of("Cobblestone", "Stone", "Smooth Stone"),
            "Wood", List.of("Cherry Wood", "Stripped Cherry Log", "Cherry Planks")
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

    public static Item getItemByName(String itemName) {
        for (Item item : ForgeRegistries.ITEMS) {
            String translatedName = Component.translatable(item.getDescriptionId()).getString();
            if (translatedName.equals(itemName)) {
                Optional<ResourceLocation> opitem = Optional.ofNullable(ForgeRegistries.ITEMS.getKey(item));
               if(opitem.isPresent()){
                   return ForgeRegistries.ITEMS.getValue(opitem.get());
               }
            }
        }
        return null;
    }

    public static String getNextEvolutions(String blockName){
        List<String> evolutionList = getEvolutionsList(blockName);

        if(!evolutionList.isEmpty()){
            int currentIndex = evolutionList.indexOf(blockName);
            if(currentIndex < evolutionList.size() - 1){
                return evolutionList.get(currentIndex + 1);
            }else{
                return "Items: " + evolutionList.get(currentIndex);
            }
        }

        return null;
    }

    public static Block getRandomBlock(int index){
        Object[] keys = evolutions.keySet().toArray();
        String randomKey = (String) keys[random(keys.length)];

        List<String> randomList = evolutions.get(randomKey);
        return getBlockByName(randomList.get(index));
    }

    public static boolean checkList(String blockName){
        return evolutions.values().stream()
                .anyMatch(list -> list.contains(blockName));
    }

    public static int random(int number){
        return rand.nextInt(number);
    }

    public static boolean checkHeight(BlockPos position, Block aboveBlock){
        return position.getY() == -62 && aboveBlock == Blocks.AIR;
    }


}
