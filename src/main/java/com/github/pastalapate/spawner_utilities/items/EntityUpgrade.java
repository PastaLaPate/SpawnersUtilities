package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;

public class EntityUpgrade extends AbstractUpgrade {
    public EntityUpgrade() {
        // maxEntity = maxEntity + entityBoost
        super(new Properties().tab(ModGroup.instance).setNoRepair().stacksTo(64), 0, 5, 0);
    }
}
