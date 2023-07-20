package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModGroup;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

public class SpawnerInfo extends Item {
    public SpawnerInfo() {
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
        } else if (blockState.getBlock().is(ModBlocks.FE_SPAWNER.get()) || blockState.getBlock().is(ModBlocks.FE_SPAWNER_TIER2.get())) {
            TileEntity tileEntity = world.getBlockEntity(lookPos);
            assert tileEntity != null;
            final CompoundNBT nbt = tileEntity.save(new CompoundNBT());
            final int energy = nbt.getInt("fe_spawner.energy");
            final String entityType = nbt.getString("fe_spawner.entity_type");
            final int instance_id = nbt.getInt("fe_spawner.instance_id");
            final int spawnRange = nbt.getInt("fe_spawner.spawnRange");
            final int entityLimit = nbt.getInt("fe_spawner.entityLimit");
            String[] messages = {
                    f("Energy %s", energy),
                    f("Entity Type %s", entityType),
                    f("Entity Limit %s", entityLimit),
                    f("Spawn Range %s", spawnRange),
                    f("Instance ID %s", instance_id),
            };
            for (String message : messages) {
                player.displayClientMessage(new StringTextComponent(message), false);
            }
        } else {
            player.displayClientMessage(new StringTextComponent("Not a spawner !"), false);
        }
        return super.use(world, player, hand);
    }

    private String f(String f, Object... args) {
        return String.format(f, args);
    }
}
