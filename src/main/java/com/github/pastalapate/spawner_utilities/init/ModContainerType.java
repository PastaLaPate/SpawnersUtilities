package com.github.pastalapate.spawner_utilities.init;

import com.github.pastalapate.spawner_utilities.Main;
import com.github.pastalapate.spawner_utilities.gui.FESpawnerGUI;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerType {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MOD_ID);

    public static final RegistryObject<ContainerType<FESpawnerGUI>> FESpawnerGUI = CONTAINER_TYPES.register("fe_spawner_gui", () -> IForgeContainerType.create(FESpawnerGUI::new));

}
