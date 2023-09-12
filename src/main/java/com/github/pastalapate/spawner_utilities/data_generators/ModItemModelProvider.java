package com.github.pastalapate.spawner_utilities.data_generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
    ModelFile itemHandheld = getExistingFile(mcLoc("item/handheld"));

    public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("fe_spawner_tier1", modLoc("block/fe_spawner"));
        withExistingParent("fe_spawner_tier2", modLoc("block/fe_spawner_tier2"));
        withExistingParent("spawner_base", modLoc("block/spawner_base"));
        create("soul_capturer", itemHandheld);
        create("range_upgrade", itemGenerated);
        create("copper_ingot", itemGenerated);
        create("basic_upgrade", itemGenerated);
        create("entity_upgrade", itemGenerated);
        create("speed_upgrade", itemGenerated);
        create("soul_container", itemGenerated);
        create("spawner_info", itemHandheld);
    }

    private void create(String s, ModelFile m) {
        getBuilder(s).parent(m).texture("layer0", "item/" + s);
    }
}
