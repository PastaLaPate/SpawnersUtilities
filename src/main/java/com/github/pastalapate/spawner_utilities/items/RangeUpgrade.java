package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;

public class RangeUpgrade extends AbstractUpgrade {

    public RangeUpgrade() {
        super(new Properties().tab(ModGroup.instance).setNoRepair().stacksTo(64), -1, 0, 0);
    }
}
