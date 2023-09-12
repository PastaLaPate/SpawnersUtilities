package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;
import net.minecraft.item.Item;

public class CopperIngot extends Item {
    public CopperIngot() {
        super(new Properties().tab(ModGroup.instance).stacksTo(64).setNoRepair());
    }
}
