package com.github.pastalapate.spawner_utilities.data_generators;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import com.github.pastalapate.spawner_utilities.init.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModTagsProvider {
    public static class ModItemTagsProvider extends ItemTagsProvider {

        public ModItemTagsProvider(DataGenerator p_i232552_1_, BlockTagsProvider p_i232552_2_, ExistingFileHelper fileHelper) {
            super(p_i232552_1_, p_i232552_2_, SpawnerUtilities.MOD_ID, fileHelper);
        }

        @Override
        protected void addTags() {
            copy(ModTags.Blocks.ORES_COOPER, ModTags.Items.ORES_COOPER);
            copy(Tags.Blocks.ORES, Tags.Items.ORES);

            tag(ModTags.Items.INGOTS_COPPER).add(ModItems.COPPER_INGOT.get());
        }
    }
}
