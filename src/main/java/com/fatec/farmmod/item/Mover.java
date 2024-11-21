package com.fatec.farmmod.item;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//Mostrar qual item está carregando
public class Mover extends Item {

    public Mover(Properties pProperties){
        super(pProperties.stacksTo(1));
    }

    private static final String BLOCK_TAG = "StoredBlock";

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext){
        if(!pContext.getLevel().isClientSide()) {
            BlockPos position = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            BlockState blockState = pContext.getLevel().getBlockState(position);
            Block block = blockState.getBlock();
            String blockName = block.getName().getString();

            ItemStack stack = pContext.getItemInHand();

            if(ItemUtils.checkList(blockName) && !hasBlock(stack)) {
                if (player != null)
                    player.displayClientMessage(Component.literal("Você clicou no " + block.getName().getString()), true);
                saveBlockToStack(stack, block);
                pContext.getLevel().setBlock(position, Blocks.AIR.defaultBlockState(), 2);
            }else if(hasBlock(stack)) {
                position = position.relative(pContext.getClickedFace());
                System.out.println(position.getX() + " e " + position.getY() + " e " + position.getZ());
                Block pointClicked = pContext.getLevel().getBlockState(position).getBlock();
                if(ItemUtils.checkHeight(position, pointClicked)) {
                    BlockState newState = getBlockFromStack(stack).defaultBlockState();
                    System.out.println(newState);
                    pContext.getLevel().setBlock(position, newState, 3);
                    clearBlockFromStack(stack);
                }else{
                    if(player != null)
                        player.displayClientMessage(Component.literal("Não é possivel colocar bloco nesse lugar"), true);
                }
            }else{
                if(player != null)
                    player.displayClientMessage(Component.literal("Não é possivel pegar esse bloco"), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, net.minecraft.world.item.@NotNull TooltipFlag flag) {
        if (hasBlock(stack)) {
            tooltip.add(Component.literal("Bloco carregado: " + getBlockFromStack(stack).getName().getString()));
        } else {
            tooltip.add(Component.literal("Nenhum bloco carregado"));
        }
    }

    private boolean hasBlock(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(BLOCK_TAG);
    }

    private void saveBlockToStack(ItemStack stack, Block block) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(BLOCK_TAG, block.toString());
    }

    private void clearBlockFromStack(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove(BLOCK_TAG);
        }
    }

    private Block getBlockFromStack(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(BLOCK_TAG)) {
            String blockId = tag.getString(BLOCK_TAG);

            // Corrigir o formato do blockId, se necessário
            if (blockId.startsWith("Block{") && blockId.endsWith("}")) {
                blockId = blockId.substring(6, blockId.length() - 1); // Remove "Block{" e "}"
            }

            try {
                ResourceLocation blockKey = new ResourceLocation(blockId);
                return BuiltInRegistries.BLOCK.get(blockKey);
            } catch (ResourceLocationException e) {
                // Log de erro ou fallback para um bloco padrão
                System.err.println("Erro ao criar ResourceLocation: " + e.getMessage());
            }
        }
        return Blocks.AIR;
    }

}