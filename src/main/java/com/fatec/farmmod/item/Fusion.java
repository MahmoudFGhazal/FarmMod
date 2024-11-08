package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

//Arrumar Merge
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

                visitedPositions.add(position);
                checkNeighbors(block, position, pContext.getLevel());

                if(matchingBlocks.size() >= 2) {
                    for (BlockPos posToDelete : matchingBlocks) {
                        pContext.getLevel().setBlock(posToDelete, Blocks.AIR.defaultBlockState(), 2);
                        pContext.getLevel().setBlock(posToDelete.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                    }

                    String nextBlockName = evolutionList.get(currentIndex + 1);
                    Block nextBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nextBlockName.toLowerCase().replace(" ", "_")));

                    if (nextBlock != null) {
                        if ((matchingBlocks.size()+1) % 5 == 0) {
                            int merge = (matchingBlocks.size()+1) / 5;

                            pContext.getLevel().setBlock(position, nextBlock.defaultBlockState(), 2);
                            pContext.getLevel().setBlock(position.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);

                            for(int i = 0; i < merge; i++){
                                BlockPos extraBlockPos = matchingBlocks.get(i);

                                pContext.getLevel().setBlock(extraBlockPos, nextBlock.defaultBlockState(), 2);
                                pContext.getLevel().setBlock(extraBlockPos.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                            }
                        } else if (currentIndex >= 0 && currentIndex < evolutionList.size() - 1) {
                            pContext.getLevel().setBlock(position, nextBlock.defaultBlockState(), 2);
                            pContext.getLevel().setBlock(position.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);

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
