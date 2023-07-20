package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;

public class SpeedUpgrade extends AbstractUpgrade {
    public SpeedUpgrade() {
        // speed - (speed * boost)
        super(0, 0, 0.25);
    }
}
