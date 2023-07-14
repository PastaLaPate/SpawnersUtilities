package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModGroup;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

public class Spawner_info extends Item {
    public Spawner_info() {
        super(new Properties().tab(ModGroup.instance).stacksTo(1));
    }

    @Override @MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        BlockRayTraceResult ray = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.NONE);
        BlockPos lookPos = ray.getBlockPos();
        BlockState blockState = world.getBlockState(lookPos);
        if (blockState.getBlock().is(Blocks.SPAWNER)) {
            TileEntity tileEntity = world.getBlockEntity(lookPos);
            assert tileEntity != null;
            CompoundNBT nbt = tileEntity.save(new CompoundNBT());
            CompoundNBT SpawnData = (CompoundNBT) nbt.get("SpawnData");
            assert SpawnData != null;
            player.displayClientMessage(new StringTextComponent("NBT Data : " + SpawnData.getString("id")), false);
        } else {
            player.displayClientMessage(new StringTextComponent("Not a spawner !"), false);
        }
        return super.use(world, player, hand);
    }
}
