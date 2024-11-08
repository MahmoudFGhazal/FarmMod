package com.fatec.farmmod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.fatec.farmmod.FarmMod.evolutions;

//Ter chance de merge no normal
//Analisar Bloco acima e abaixo para não substituir
public class Fusion extends Item {
    List<BlockPos> matchingBlocks = new ArrayList<>();
    Set<BlockPos> visitedPositions = new HashSet<>();

    public Fusion(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos position = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            BlockState blockState = pContext.getLevel().getBlockState(position);
            Block block = blockState.getBlock();
            String blockName = block.getName().getString();

            List<String> evolutionList = evolutions.values().stream()
                    .filter(strings -> strings.contains(blockName))
                    .findFirst()
                    .orElse(null);

            if (evolutionList != null && player != null) {
                int currentIndex = evolutionList.indexOf(blockName);

                matchingBlocks.add(position);
                visitedPositions.add(position);
                checkNeighbors(block, position, pContext.getLevel());

                if(matchingBlocks.size() >= 3 && currentIndex >= 0 && currentIndex < 2) {
                    for (BlockPos posToDelete : matchingBlocks) {
                        pContext.getLevel().setBlock(posToDelete, Blocks.AIR.defaultBlockState(), 2);
                        pContext.getLevel().setBlock(posToDelete.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                    }

                    String nextBlockName = evolutionList.get(currentIndex + 1);
                    Block nextBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nextBlockName.toLowerCase().replace(" ", "_")));
                    int quantBlocks = matchingBlocks.size();

                    if (nextBlock != null) {
                        if (quantBlocks % 5 == 0) {
                            for(int i = 0; i <= Math.floorDiv(quantBlocks, 5); i+=2){
                                pContext.getLevel().setBlock(matchingBlocks.get(i), nextBlock.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(matchingBlocks.get(i).below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(matchingBlocks.get(i+1), nextBlock.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(matchingBlocks.get(i+1).below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                            }
                        }else {
                            int i;
                            for(i = 0; i < Math.floorDiv(quantBlocks, 3); i++){
                                pContext.getLevel().setBlock(matchingBlocks.get(i), nextBlock.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(matchingBlocks.get(i).below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                            }

                            for(int p = i; p < quantBlocks % 3 + i; p++){
                                pContext.getLevel().setBlock(matchingBlocks.get(p), block.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(matchingBlocks.get(p).below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
            matchingBlocks.clear();
            visitedPositions.clear();
        }

        return InteractionResult.SUCCESS;
    }

    private void checkNeighbors(Block startblock, BlockPos position, Level level) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (y == x) continue;

                BlockPos currentPos = position.offset(x, 0, y);

                if (visitedPositions.contains(currentPos)) continue;

                BlockState currentState = level.getBlockState(currentPos);
                Block currentBlock = currentState.getBlock();
                visitedPositions.add(currentPos);

                if (currentBlock == startblock) {
                    matchingBlocks.add(currentPos);
                    checkNeighbors(startblock, currentPos, level);
                }
            }
        }
    }
}
