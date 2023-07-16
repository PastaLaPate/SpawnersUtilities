package com.github.pastalapate.spawner_utilities;

import com.github.pastalapate.spawner_utilities.gui.FESpawnerGUI;
import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModContainerType;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.networking.ModMessages;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MOD_ID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "spawners_utilities";
    public Main() {
        // Register the setup method for modloading
        LOGGER.info("Thank you using this mod :)");
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
        RenderTypeLookup.setRenderLayer(ModBlocks.FE_SPAWNER.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.SPAWNER_BASE.get(), RenderType.translucent());
        ScreenManager.register(ModContainerType.FESpawnerGUI.get(), FESpawnerGUI.FESpawnerScreen::new);
    }
}
