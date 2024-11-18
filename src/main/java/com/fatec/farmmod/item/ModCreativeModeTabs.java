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
    public static final DeferredRegister<CreativeModeTab> MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FarmMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FARM_TAB = MODE_TABS.register("farmtab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.FUSION.get()))
                    .title(Component.translatable("creativetab.farmtab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.FUSION.get());
                        pOutput.accept(ModItems.MOVER.get());})
                    .build());

    public static void register(IEventBus eventBus) {
        MODE_TABS.register(eventBus);
    }
}
