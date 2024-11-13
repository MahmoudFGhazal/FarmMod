package com.fatec.farmmod.worldgen.dimension;

import com.fatec.farmmod.FarmMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModDimension {
    public static final ResourceKey<LevelStem> FARMMODDIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(FarmMod.MOD_ID, "farmmod"));
    public static final ResourceKey<Level> FARMMODDIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(FarmMod.MOD_ID, "farmmod"));
    public static final ResourceKey<DimensionType> FARMMOD_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(FarmMod.MOD_ID, "farmmod"));

    private static final Integer initialSize = 20;
    private  static Integer platformSize = initialSize;
    private static boolean confirmWaiting = false;

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        ServerLevel farmmodWorld = server.getLevel(FARMMODDIM_LEVEL_KEY);

        if (farmmodWorld == null) {
            BlockPos center = new BlockPos(0, 64, 0);
            createPlatform(farmmodWorld, center);
        }
    }

    private static void createPlatform(ServerLevel world, BlockPos center) {
        int initialX = center.getX() - (platformSize / 2);
        int initialZ = center.getZ() - (platformSize / 2);

        for (int x = initialX; x <= initialX + platformSize; x++) {
            for (int z = initialZ; z <= initialZ + platformSize; z++) {
                world.setBlock(new BlockPos(x, center.getY(), z), Blocks.DIRT.defaultBlockState(), 3);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player.level().dimension() == FARMMODDIM_LEVEL_KEY && !confirmWaiting && emeraldCount(player) >= 5) {
            player.sendSystemMessage(Component.literal("Você tem 5 esmeraldas. Deseja expandir a plataforma? Use /expandir para confirmar."));
            confirmWaiting = true;
        }
    }

    // Conta o número de esmeraldas no inventário do jogador
    private static int emeraldCount(Player player) {
        int count = 0;
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).getItem() == Items.EMERALD) {
                count += inventory.getItem(i).getCount();
            }
        }
        return count;
    }

    // Expande a plataforma se o jogador tiver esmeraldas suficientes
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player.level().dimension() == FARMMODDIM_LEVEL_KEY && confirmWaiting && emeraldCount(player) >= 5) {
            platformSize += 10;
            createPlatform((ServerLevel) player.level(), player.blockPosition());
            player.getInventory().clearOrCountMatchingItems(itemStack -> itemStack.is(Items.EMERALD), 5, player.inventoryMenu.getCraftSlots());
            player.sendSystemMessage(Component.literal("A plataforma foi expandida!"));
            confirmWaiting = false;
        }
    }
}
