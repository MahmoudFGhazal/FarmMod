package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
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
    private static final int INTERVAL_STACK = 500;
    private int tickCount = 0;
    private static final String COUNT_TAG = "box_stock_count";

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
//obtem o valor de count do item stack
    private int getCount (ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt(COUNT_TAG);
    }

    private void setCount(ItemStack stack, int count){
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(COUNT_TAG, count);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean isSelected) {
        if (!world.isClientSide && entity instanceof ServerPlayer) {
            tickCount++;
            if(tickCount >= INTERVAL_STACK){
                tickCount = 0;
                int currentCount = getCount(stack);
                if(currentCount < MAX_STACK){
                    setCount(stack, currentCount +1); //aumenta em 1 o stack
                    System.out.println("Stack count: " + (currentCount + 1));
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
            int currentCount = getCount(stack);
            if (currentCount > 0) { //verifica se o item tem stacks para usar
                Random random = new Random();
                Block block = BLOCKS[random.nextInt(BLOCKS.length)];
                world.setBlock(pos, block.defaultBlockState(), 3);
                setCount(stack, currentCount - 1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag){
        int count = getCount(stack);
        tooltip.add(Component.translatable("item.farmmod.box_stock.stacks", count));
    }
}