package com.github.pastalapate.spawner_utilities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class SpawnerBase extends Block {
    public SpawnerBase() {
        super(Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops());
    }
}
