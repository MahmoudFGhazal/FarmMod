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

import java.util.Map;
import java.util.Objects;

//Mostrar qual item está carregando
//Verificar se tem algum bloco acima
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
            String blockName = block.getName().getString();

            String evolutionCategory = FarmMod.evolutions.entrySet().stream()
                    .filter(entry -> entry.getValue().contains(blockName))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(null);


            if(evolutionCategory != null && !hasBlock) {
                if (player != null)
                    player.displayClientMessage(Component.literal("Você clicou no " + block.getName().getString()), true);
                atualblock = block;
                pContext.getLevel().setBlock(position, Blocks.AIR.defaultBlockState(), 2);
                hasBlock = true;
            }else if(hasBlock) {
                BlockPos positionAbove = position.above();
                BlockState newState = atualblock.defaultBlockState();
                pContext.getLevel().setBlock(positionAbove, newState, 3);
                hasBlock = false;

            }

        }
        return InteractionResult.SUCCESS;
    }

}