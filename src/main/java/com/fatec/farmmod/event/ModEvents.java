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

@Mod.EventBusSubscriber(modid = FarmMod.MOD_ID) // Inscreve este método como um evento para o mod especificado
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){

        // Adicionando novas trocas para a profissão de ARMORER (Ferreiro de armaduras)
        if(event.getType() == VillagerProfession.ARMORER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear(); // Limpa as trocas padrão

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // Trocas do nível 1: 1 bloco de ferro por 3 esmeraldas
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.IRON_BLOCK, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // Trocas do nível 2: 6 esmeraldas por 1 minério de ferro
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.IRON_ORE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // Trocas do nível 3: 12 esmeraldas por 1 bloco de ferro bruto
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.RAW_IRON_BLOCK, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        // Adicionando novas trocas para a profissão de TOOLSMITH (Ferreiro de ferramentas)
        if(event.getType() == VillagerProfession.TOOLSMITH){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear(); // Limpa as trocas padrão

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // Trocas do nível 1: 1 bloco de cobre por 3 esmeraldas
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.COPPER_BLOCK, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // Trocas do nível 2: 6 esmeraldas por 1 minério de cobre
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.COPPER_ORE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // Trocas do nível 3: 12 esmeraldas por 1 bloco de cobre bruto
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.RAW_COPPER_BLOCK, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        // Adicionando novas trocas para a profissão de WEAPONSMITH (Ferreiro de armas)
        if(event.getType() == VillagerProfession.WEAPONSMITH){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear(); // Limpa as trocas padrão

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // Trocas do nível 1: 1 bloco de ouro por 3 esmeraldas
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.GOLD_BLOCK, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // Trocas do nível 2: 6 esmeraldas por 1 minério de ouro
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.GOLD_ORE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // Trocas do nível 3: 12 esmeraldas por 1 bloco de ouro bruto
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.RAW_GOLD_BLOCK, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        // Adicionando novas trocas para a profissão de MASON (Pedreiro)
        if(event.getType() == VillagerProfession.MASON){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear(); // Limpa as trocas padrão

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // Trocas do nível 1: 1 pedra lisa por 3 esmeraldas
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.SMOOTH_STONE, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // Trocas do nível 2: 6 esmeraldas por 1 pedra
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.COBBLESTONE, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // Trocas do nível 3: 12 esmeraldas por 1 pedra
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.STONE, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }

        // Adicionando novas trocas para a profissão de FISHERMAN (Pescador)
        if(event.getType() == VillagerProfession.FISHERMAN){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.clear(); // Limpa as trocas padrão

            trades.put(1, new ArrayList<>());
            trades.put(2, new ArrayList<>());
            trades.put(3, new ArrayList<>());
            trades.put(4, new ArrayList<>());
            trades.put(5, new ArrayList<>());

            // Trocas do nível 1: 1 pranchas de cerejeira por 3 esmeraldas
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.CHERRY_PLANKS, 1),
                    new ItemStack(Items.EMERALD, 3),
                    Integer.MAX_VALUE, 12, 0.0f));

            // Trocas do nível 2: 6 esmeraldas por 1 tronco de cerejeira
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 6),
                    new ItemStack(Items.CHERRY_LOG, 1),
                    Integer.MAX_VALUE, 6, 0.0f));

            // Trocas do nível 3: 12 esmeraldas por 1 tronco de cerejeira descascado
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(Items.STRIPPED_CHERRY_LOG, 1),
                    Integer.MAX_VALUE, 4, 0.0f));
        }
    }
}
