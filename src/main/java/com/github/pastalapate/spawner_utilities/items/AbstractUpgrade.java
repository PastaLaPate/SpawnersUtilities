package com.github.pastalapate.spawner_utilities.items;

import net.minecraft.item.Item;

public abstract class AbstractUpgrade extends Item implements IUpgrade {
    private int supRange;
    private int supEntity;
    private double supTime;

    public AbstractUpgrade(Properties properties, int sR, int sE, double sT) {
        super(properties);
        this.supRange = sR;
        this.supEntity = sE;
        this.supTime = sT;
    }

    @Override
    public int getSupRange() {
        return this.supRange;
    }

    @Override
    public int getSupEntity() {
        return this.supEntity;
    }

    @Override
    public double getSupTime() {
        return this.supTime;
    }
}
