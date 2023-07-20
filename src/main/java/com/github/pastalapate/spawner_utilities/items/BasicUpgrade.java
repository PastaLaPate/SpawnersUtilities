package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;

public class BasicUpgrade extends AbstractUpgrade{

    public BasicUpgrade() {
        super(new Properties().stacksTo(64).tab(ModGroup.instance), 0, 0, 0);
    }
}
