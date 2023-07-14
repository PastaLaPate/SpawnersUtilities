package com.github.pastalapate.spawner_utilities.blocks;

import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.tiles_entities.fe_spawner_TE;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class fe_spawner extends Block  {

    public fe_spawner() {
        super(AbstractBlock.Properties.of(Material.GLASS).strength(3f, 15f).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (!world.isClientSide) {
            TileEntity tileentity = world.getBlockEntity(blockPos);
            if (tileentity instanceof fe_spawner_TE) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (fe_spawner_TE) tileentity, blockPos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.FE_SPAWNER.get().create();
    }
}
