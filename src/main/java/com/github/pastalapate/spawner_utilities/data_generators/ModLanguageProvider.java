package com.github.pastalapate.spawner_utilities.data_generators;

import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.RegistryObject;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.SPAWNER_INFO, "Spawner info");
        addItem(ModItems.SOUL_CAPTURER, "Soul capturer");
        addItem(ModItems.SOUL_CONTAINER, "Soul container");
        addItem(ModItems.SPEED_UPGRADE, "Speed Upgrade");
        addItem(ModItems.ENTITY_UPGRADE, "Entity Upgrade");
        addItem(ModItems.RANGE_UPGRADE, "Range Upgrade");
        addItem(ModItems.BASIC_UPGRADE, "Basic Upgrade");
        addItem(ModItems.COPPER_INGOT, "Copper Ingot");
        addBlock(ModBlocks.FE_SPAWNER, "FE Spawner");
        addBlock(ModBlocks.FE_SPAWNER_TIER2, "FE Spawner tier 2");
        addBlock(ModBlocks.SPAWNER_BASE, "Spawner Base");
        add("itemGroup.spawners_utilities", "Spawners Utilities");
    }

    private void addBlock(RegistryObject<Block> obj, String n) {
        add(obj.get(), n);
    }

    private void addItem(RegistryObject<Item> obj, String n) {
        add(obj.get(), n);
    }
}
