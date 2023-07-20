package com.github.pastalapate.spawner_utilities.init;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpawnerUtilities.MOD_ID);

    public static final RegistryObject<Item> SPAWNER_INFO = ITEMS.register("spawner_info", SpawnerInfo::new);
    public static final RegistryObject<Item> SOUL_CAPTURER = ITEMS.register("soul_capturer", SoulCapturer::new);
    public static final RegistryObject<Item> SOUL_CONTAINER = ITEMS.register("soul_container", SoulContainer::new);

    public static final RegistryObject<Item> BASIC_UPGRADE = ITEMS.register("basic_upgrade", BasicUpgrade::new);
    public static final RegistryObject<Item> SPEED_UPGRADE = ITEMS.register("speed_upgrade", SpeedUpgrade::new);
    public static final RegistryObject<Item> ENTITY_UPGRADE = ITEMS.register("entity_upgrade", EntityUpgrade::new);
    public static final RegistryObject<Item> RANGE_UPGRADE = ITEMS.register("range_upgrade", RangeUpgrade::new);
    public static final RegistryObject<Item> REDSTONE_UPGRADE = ITEMS.register("redstone_upgrade", RedstoneUpgrade::new);
}
