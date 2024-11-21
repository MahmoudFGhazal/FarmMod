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

// A classe 'Mover' representa um item que permite o jogador pegar e colocar blocos.
// O item armazena o bloco que o jogador clica e o coloca em outra posição.
public class Mover extends Item {

    // Define o limite máximo de pilha do item como 1.
    public Mover(Properties pProperties){
        super(pProperties.stacksTo(1));
    }

    // Constante para o nome da tag que armazenará o bloco carregado no item.
    private static final String BLOCK_TAG = "StoredBlock";

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext){
        // Verifica se o código não está sendo executado no lado cliente (servidor).
        if(!pContext.getLevel().isClientSide()) {
            BlockPos position = pContext.getClickedPos();  // Posição do bloco clicado.
            Player player = pContext.getPlayer();  // Jogador que usou o item.
            BlockState blockState = pContext.getLevel().getBlockState(position);  // Estado do bloco.
            Block block = blockState.getBlock();  // Bloco clicado.
            String blockName = block.getName().getString();  // Nome do bloco.

            ItemStack stack = pContext.getItemInHand();  // O item em mãos.

            // Verifica se o bloco pode ser armazenado no item e se o item ainda não tem um bloco armazenado.
            if(ItemUtils.checkList(blockName) && !hasBlock(stack)) {
                if (player != null)
                    player.displayClientMessage(Component.literal("Você clicou no " + block.getName().getString()), true);
                // Armazena o bloco no item e remove o bloco do mundo.
                saveBlockToStack(stack, block);
                pContext.getLevel().setBlock(position, Blocks.AIR.defaultBlockState(), 2);
            } else if(hasBlock(stack)) {
                // Se o item já tiver um bloco armazenado, tenta colocá-lo na posição ao lado.
                position = position.relative(pContext.getClickedFace());  // Nova posição para colocar o bloco.
                System.out.println(position.getX() + " e " + position.getY() + " e " + position.getZ());
                Block pointClicked = pContext.getLevel().getBlockState(position).getBlock();  // Bloco na nova posição.

                // Verifica se o bloco pode ser colocado na nova posição (verificação de altura).
                if(ItemUtils.checkHeight(position, pointClicked)) {
                    BlockState newState = getBlockFromStack(stack).defaultBlockState();  // Recupera o estado do bloco carregado.
                    System.out.println(newState);
                    pContext.getLevel().setBlock(position, newState, 3);  // Coloca o bloco na nova posição.
                    clearBlockFromStack(stack);  // Limpa o bloco armazenado no item.
                } else {
                    if(player != null)
                        player.displayClientMessage(Component.literal("Não é possivel colocar bloco nesse lugar"), true);
                }
            } else {
                if(player != null)
                    player.displayClientMessage(Component.literal("Não é possivel pegar esse bloco"), true);
            }
        }
        return InteractionResult.SUCCESS;  // A ação foi bem-sucedida.
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> tooltip, net.minecraft.world.item.@NotNull TooltipFlag flag) {
        // Adiciona texto de dica quando o item é sobrevoado.
        if (hasBlock(stack)) {
            tooltip.add(Component.literal("Bloco carregado: " + getBlockFromStack(stack).getName().getString()));
        } else {
            tooltip.add(Component.literal("Nenhum bloco carregado"));
        }
    }

    // Verifica se o item tem um bloco armazenado.
    private boolean hasBlock(ItemStack stack) {
        CompoundTag tag = stack.getTag();  // Recupera a tag NBT do item.
        return tag != null && tag.contains(BLOCK_TAG);  // Verifica se a tag contém o bloco armazenado.
    }

    // Armazena o bloco no item.
    private void saveBlockToStack(ItemStack stack, Block block) {
        CompoundTag tag = stack.getOrCreateTag();  // Cria ou recupera a tag NBT do item.
        tag.putString(BLOCK_TAG, block.toString());  // Armazena a string representando o bloco na tag.
    }

    // Limpa o bloco armazenado no item.
    private void clearBlockFromStack(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            tag.remove(BLOCK_TAG);  // Remove a tag do bloco armazenado.
        }
    }

    // Recupera o bloco armazenado no item.
    private Block getBlockFromStack(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(BLOCK_TAG)) {
            String blockId = tag.getString(BLOCK_TAG);  // Recupera o ID do bloco armazenado.

            // Corrige o formato do ID do bloco, se necessário.
            if (blockId.startsWith("Block{") && blockId.endsWith("}")) {
                blockId = blockId.substring(6, blockId.length() - 1);  // Remove "Block{" e "}"
            }

            try {
                ResourceLocation blockKey = new ResourceLocation(blockId);  // Cria um ResourceLocation a partir do ID.
                return BuiltInRegistries.BLOCK.get(blockKey);  // Recupera o bloco registrado.
            } catch (ResourceLocationException e) {
                // Log de erro caso o ID do bloco seja inválido.
                System.err.println("Erro ao criar ResourceLocation: " + e.getMessage());
            }
        }
        return Blocks.AIR;  // Se não encontrar o bloco, retorna o bloco AIR (nulo).
    }
}
