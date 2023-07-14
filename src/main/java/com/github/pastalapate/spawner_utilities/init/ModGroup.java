package com.github.pastalapate.spawner_utilities.init;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModGroup extends ItemGroup {

    public static final ModGroup instance = new ModGroup(ItemGroup.TABS.length, "Spawners Utilities");

    private ModGroup(int index, String label) {
        super(index, label);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ItemStack makeIcon() {
        return new ItemStack(ModItems.SPAWNER_INFO.get());
    }
}
