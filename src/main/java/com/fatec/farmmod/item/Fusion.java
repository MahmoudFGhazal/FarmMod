package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

            Block atualblock = null;

            if (player != null) {
                for (Block blockex : ForgeRegistries.BLOCKS.getValues()) {
                    if (FarmMod.MOD_ID.equals(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockex)).getNamespace())) {
                        if (block == blockex) {
                            player.displayClientMessage(Component.literal("Você clicou no " + block.getName().getString()), true);
                            atualblock = blockex;
                            break;
                        }
                    }
                }
            }

            if (atualblock != null) {
                visitedPositions.add(position);
                checkNeighbors(atualblock, position, pContext.getLevel());
                System.out.println(matchingBlocks.size());
                for (BlockPos posToDelete : matchingBlocks) {
                    pContext.getLevel().setBlock(posToDelete, Blocks.AIR.defaultBlockState(), 2);
                    pContext.getLevel().setBlock(posToDelete.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                }

                matchingBlocks.clear();
                visitedPositions.clear();
            }
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
