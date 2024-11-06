package com.fatec.farmmod.event;

import com.fatec.farmmod.FarmMod;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

//bah guri da catequesi

@Mod.EventBusSubscriber(modid = FarmMod.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){
        //iron trader
        if(event.getType() == VillagerProfession.ARMORER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear();

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.IRON_BLOCK, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.IRON_ORE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.RAW_IRON_BLOCK, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        //copper trader
        if(event.getType() == VillagerProfession.TOOLSMITH){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear();

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.COPPER_BLOCK, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.COPPER_ORE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.RAW_COPPER_BLOCK, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        //gold trader
        if(event.getType() == VillagerProfession.WEAPONSMITH){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear();

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.GOLD_BLOCK, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.GOLD_ORE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.RAW_GOLD_BLOCK, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        //stone trader
        if(event.getType() == VillagerProfession.MASON){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear();

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.SMOOTH_STONE, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.COBBLESTONE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.STONE, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        //wood trader
        if(event.getType() == VillagerProfession.FISHERMAN){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear();

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.CHERRY_PLANKS, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.CHERRY_LOG, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.STRIPPED_CHERRY_LOG, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

    }

}
