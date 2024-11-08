package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmMod.MOD_ID);

    // Registro do item Fusion
    public static final RegistryObject<Item> FUSION = ITEMS.register("fusion", () -> new Fusion(new Properties()));

    public static final RegistryObject<Item> MOVER = ITEMS.register("mover", () -> new Mover(new Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
