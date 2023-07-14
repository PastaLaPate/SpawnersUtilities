package com.github.pastalapate.spawner_utilities;

import com.github.pastalapate.spawner_utilities.gui.FESpawnerGUI;
import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModContainerType;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.networking.ModMessages;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MOD_ID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "spawners_utilities";
    public Main() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModTileEntities.TILE_ENTITY_TYPES.register(bus);
        ModContainerType.CONTAINER_TYPES.register(bus);
        ModMessages.register();
    }


    private void setup(FMLCommonSetupEvent event)
    {

    }

    private void clientSetup(FMLClientSetupEvent e) {
        ScreenManager.register(ModContainerType.FESpawnerGUI.get(), FESpawnerGUI.FESpawnerScreen::new);
    }
}
