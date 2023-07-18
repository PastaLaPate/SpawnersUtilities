package com.github.pastalapate.spawner_utilities;

import com.github.pastalapate.spawner_utilities.gui.FESpawnerGUI;
import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModContainerType;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.networking.ModMessages;
import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpawnerUtilities.MOD_ID)
public class SpawnerUtilities
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger("Spawner Utilities");
    public static final String MOD_ID = "spawners_utilities";
    public SpawnerUtilities() {
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

        MinecraftForge.EVENT_BUS.register(new FESpawnerTE.Listener());
    }


    private void setup(FMLCommonSetupEvent event)
    {

    }

    private void clientSetup(FMLClientSetupEvent e) {
        for (RegistryObject<Block> transparentBlock : ModBlocks.TRANSPARENT_BLOCKS) {
            RenderTypeLookup.setRenderLayer(transparentBlock.get(), RenderType.translucent());
        }
        ScreenManager.register(ModContainerType.FESpawnerGUI.get(), (FESpawnerGUI container, PlayerInventory inventory, ITextComponent text) -> new FESpawnerGUI.FESpawnerScreen(container, inventory));
    }
}
