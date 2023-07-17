package com.github.pastalapate.spawner_utilities.tiles_entities;

import java.util.ArrayList;
import java.util.List;

public class FESpawnerTEManager {

    private static final List<FESpawnerTE> instances = new ArrayList<>();
    private static int nextInstanceID = 1;

    private FESpawnerTEManager() {
        // Private constructor to prevent instantiation
    }

    public static void registerInstance(FESpawnerTE instance) {
        instance.instance_id = nextInstanceID++;
        instances.add(instance);
    }

    public static void unregisterInstance(FESpawnerTE instance) {
        instances.remove(instance);
    }

    public static int getNextInstanceID() {
        return nextInstanceID;
    }

    public static List<FESpawnerTE> getAllInstances() {
        return instances;
    }
}
