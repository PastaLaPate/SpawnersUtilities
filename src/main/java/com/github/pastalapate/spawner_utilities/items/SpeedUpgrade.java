package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;

public class SpeedUpgrade extends AbstractUpgrade {
    public SpeedUpgrade() {
        // base energy - (base energy * boost)
        super(new Properties().tab(ModGroup.instance).setNoRepair().stacksTo(64), 0, 0, 0.25);
    }
}
