package com.github.pastalapate.spawner_utilities.blocks;

import com.github.pastalapate.spawner_utilities.tiles_entities.InfinityEnergyBlockTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class InfinityEnergyBlock extends Block {

    public InfinityEnergyBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).noDrops().instabreak());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfinityEnergyBlockTileEntity();
    }
}
