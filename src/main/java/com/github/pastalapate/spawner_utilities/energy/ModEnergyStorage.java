package com.github.pastalapate.spawner_utilities.energy;

import net.minecraftforge.energy.EnergyStorage;

public abstract class ModEnergyStorage extends EnergyStorage {

    public ModEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receiveEnergy != 0) {
            onEnergyChanged();
        }
        return receiveEnergy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractEnergy != 0) {
            onEnergyChanged();
        }
        return extractEnergy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public abstract void onEnergyChanged();
}