package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;
import net.minecraft.item.Item;

public abstract class AbstractUpgrade extends Item implements IUpgrade {
    private final int supRange;
    private final int supEntity;
    private final double supTime;

    public AbstractUpgrade(int sR, int sE, double sT) {
        super(new Properties().tab(ModGroup.instance).setNoRepair().stacksTo(64));
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
