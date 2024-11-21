package com.fatec.farmmod.item;

import com.fatec.farmmod.FarmMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    // Registro deferido para os itens, para registrar no Minecraft usando o Forge
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FarmMod.MOD_ID);

    // Registro do item 'Fusion' com a classe 'Fusion' definida anteriormente
    public static final RegistryObject<Item> FUSION = ITEMS.register("fusion", () -> new Fusion(new Properties()));

    // Registro do item 'Mover' com a classe 'Mover' definida anteriormente
    public static final RegistryObject<Item> MOVER = ITEMS.register("mover", () -> new Mover(new Properties()));

    // Registro do item 'BoxStock' com a classe 'BoxStock' definida anteriormente
    public static final RegistryObject<Item> BOXSTOCK = ITEMS.register("boxstock", () -> new BoxStock(new Properties()));

    // Método de registro dos itens no evento de registro do Forge
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);  // Registra o DeferredRegister para os itens no evento
    }
}
