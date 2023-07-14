package com.github.pastalapate.spawner_utilities.init;

import com.github.pastalapate.spawner_utilities.Main;
import com.github.pastalapate.spawner_utilities.items.SoulCapturer;
import com.github.pastalapate.spawner_utilities.items.SoulContainer;
import com.github.pastalapate.spawner_utilities.items.Spawner_info;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<Item> SPAWNER_INFO = ITEMS.register("spawner_info", Spawner_info::new);
    public static final RegistryObject<Item> SOUL_CAPTURER = ITEMS.register("soul_capturer", SoulCapturer::new);
    public static final RegistryObject<Item> SOUL_CONTAINER = ITEMS.register("soul_container", SoulContainer::new);
}
