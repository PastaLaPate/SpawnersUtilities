package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class SoulContainer extends Item {
    public SoulContainer() {
        super(new Properties().stacksTo(1).tab(ModGroup.instance).setNoRepair());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);

        CompoundNBT nbt = stack.getTag();
        if (nbt != null && nbt.contains("entity") && nbt.contains("entity_count")) {
            int neededEntities = 8;
            if (nbt.contains("needed_entity")) {
                neededEntities = nbt.getInt("needed_entity");
            }
            String capturedEntity = nbt.getString("entity");
            tooltip.add(new StringTextComponent("Captured Entity: " + capturedEntity + " (" + nbt.getInt("entity_count") + "/" + neededEntities +")"));
        }
    }
}
