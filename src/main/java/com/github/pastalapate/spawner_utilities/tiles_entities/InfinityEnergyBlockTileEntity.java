package com.github.pastalapate.spawner_utilities.tiles_entities;

import com.github.pastalapate.spawner_utilities.energy.ModEnergyStorage;
import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class InfinityEnergyBlockTileEntity extends TileEntity implements ITickableTileEntity {

    ModEnergyStorage storage = new ModEnergyStorage(Integer.MAX_VALUE, Integer.MAX_VALUE){
        @Override
        public void onEnergyChanged() {

        }
    };
    LazyOptional<IEnergyStorage> energyHandler = LazyOptional.empty();

    public InfinityEnergyBlockTileEntity() {
        super(ModTileEntities.INFINITY_ENERGY.get());
    }

    @Override
    public void onLoad() {
        this.energyHandler = LazyOptional.of(() -> storage);
        super.onLoad();
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        energyHandler.invalidate();
    }

    @Override
    public void tick() {
        storage.setEnergy(Integer.MAX_VALUE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityEnergy.ENERGY) {
            energyHandler.cast();
        }
        return super.getCapability(cap);
    }
}
