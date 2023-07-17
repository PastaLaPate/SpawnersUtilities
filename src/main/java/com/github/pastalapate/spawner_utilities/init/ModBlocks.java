package com.github.pastalapate.spawner_utilities.init;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.blocks.InfinityEnergyBlock;
import com.github.pastalapate.spawner_utilities.blocks.FESpawner;
import com.github.pastalapate.spawner_utilities.blocks.SpawnerBase;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpawnerUtilities.MOD_ID);

    public static final RegistryObject<Block> FE_SPAWNER = createBlock("fe_spawner", FESpawner::new, ModGroup.instance);

    public static final RegistryObject<Block> INFINITY_ENERGY_BLOCK = createBlock("infinity_energy_block", InfinityEnergyBlock::new, ModGroup.instance);
    public static final RegistryObject<Block> SPAWNER_BASE = createBlock("spawner_base", SpawnerBase::new, ModGroup.instance);
    public static final List<RegistryObject<Block>> TRANSPARENT_BLOCKS = new ArrayList<>();

    static {
        TRANSPARENT_BLOCKS.add(SPAWNER_BASE);
        TRANSPARENT_BLOCKS.add(FE_SPAWNER);
    }

    public static RegistryObject<Block> createBlock(String name, Supplier<? extends Block> supplier, ItemGroup group) {
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(group)));
        return block;
    }
}
