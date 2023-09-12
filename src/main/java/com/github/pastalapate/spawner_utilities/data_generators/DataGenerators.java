package com.github.pastalapate.spawner_utilities.data_generators;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = SpawnerUtilities.MOD_ID,  bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModLootTableProvider(generator));
        generator.addProvider(new ModBlockModelProvider(generator, SpawnerUtilities.MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(new ModItemModelProvider(generator, SpawnerUtilities.MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(new ModLanguageProvider(generator, SpawnerUtilities.MOD_ID));
        generator.addProvider(new ModBlockStateProvider(generator, event.getExistingFileHelper()));
    }
}
