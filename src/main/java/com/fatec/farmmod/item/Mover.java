package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Mover extends Item {

    public Mover(Properties pProperties){
        super(pProperties);
    }

    private boolean hasBlock = false;
    Block atualblock = null;

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext){
        if(!pContext.getLevel().isClientSide()) {
            BlockPos position = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            BlockState blockState = pContext.getLevel().getBlockState(position);
            Block block = blockState.getBlock();

            if(!hasBlock) {
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

                pContext.getLevel().setBlock(position, Blocks.AIR.defaultBlockState(), 2);
                hasBlock = true;
            } else {
                if (block != Blocks.AIR) {
                    BlockPos positionAbove = position.above();
                    BlockState newState = atualblock.defaultBlockState();
                    pContext.getLevel().setBlock(positionAbove, newState, 3);

                    hasBlock = false;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

}
