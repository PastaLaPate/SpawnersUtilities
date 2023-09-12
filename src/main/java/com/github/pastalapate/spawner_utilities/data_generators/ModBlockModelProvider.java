package com.github.pastalapate.spawner_utilities.data_generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        create("fe_spawner");
        create("fe_spawner_tier2");
        create("spawner_base");
    }

    private void create(String block) {
        cubeAll(block, modLoc("block/" + block));
    }
}
