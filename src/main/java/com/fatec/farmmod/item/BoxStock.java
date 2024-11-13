package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BoxStock extends Item {

    private static final int MAX_STACK = 100;
    private static final int INTERVAL_STACK = 6000;
    private int tickCount = 0;

    private static final Block[] BLOCKS = {
            Blocks.COPPER_ORE,
            Blocks.GOLD_ORE,
            Blocks.IRON_ORE,
            Blocks.COBBLESTONE,
            Blocks.CHERRY_WOOD
    };

    public BoxStock(@NotNull Properties properties) {
        super(properties.stacksTo(1)); //pelo oq eu entendi isso permite so 1 item desses no slot, evita bug
    }

    public void inventoryTick(ItemStack stack, @NotNull Level world, Entity entity, int slot, boolean isSelected) {
        if (!world.isClientSide && entity instanceof ServerPlayer) {
            tickCount++;
            stack.setCount(0);
            if(tickCount >= INTERVAL_STACK){
                tickCount = 0;
                if(stack.getCount() < MAX_STACK){
                    stack.grow(1); //aumenta em 1 o stack
                }
            }
        }
    }
    @Override
    public InteractionResult useOn(@NotNull UseOnContext context){
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        ItemStack stack = context.getItemInHand();

        if(!world.isClientSide) {
            if (stack.getCount() > 0) { //verifica se o item tem stacks para usar
                Random random = new Random();
                Block block = BLOCKS[random.nextInt(BLOCKS.length)];
                world.setBlock(pos, block.defaultBlockState(), 3);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag){
        tooltip.add(Component.translatable("item.farmmod.box_stock.stacks", stack.getCount()));
    }
}

