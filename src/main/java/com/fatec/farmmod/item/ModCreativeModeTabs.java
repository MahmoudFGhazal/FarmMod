package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {

    // Registra para criar e registrar as guias do modo criativo
    public static final DeferredRegister<CreativeModeTab> MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmMod.MOD_ID);

    // Registro o nome da guia e os itens que estão nela
    public static final RegistryObject<CreativeModeTab> FARM_TAB = MODE_TABS.register("farmtab",
            () -> CreativeModeTab.builder()  // Criando a guia usando o construtor do CreativeModeTab
                    .icon(() -> new ItemStack(ModItems.FUSION.get()))  // Definindo o ícone da guia como o item 'Fusion'
                    .title(Component.translatable("creativetab.farmtab"))  // Definindo o título da guia com tradução
                    .displayItems((pParameters, pOutput) -> {  // Definindo os itens a serem exibidos na guia
                        pOutput.accept(ModItems.FUSION.get());  // Adicionando o item 'Fusion'
                        pOutput.accept(ModItems.MOVER.get());  // Adicionando o item 'Mover'
                    })
                    .build());  // Finalizando a criação da guia

    // Registrar no jogo
    public static void register(IEventBus eventBus) {
        MODE_TABS.register(eventBus);  // Registrando o DeferredRegister para as CreativeModeTabs no evento
    }
}
