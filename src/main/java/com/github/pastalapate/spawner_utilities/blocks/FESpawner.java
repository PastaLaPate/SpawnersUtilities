package com.github.pastalapate.spawner_utilities.blocks;

import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
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
import javax.annotation.ParametersAreNonnullByDefault;

public class FESpawner extends Block  {

    private final Builder builder;

    private FESpawner(Builder builder) {
        super(AbstractBlock.Properties.of(Material.GLASS).noOcclusion().strength(3f, 15f).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops().isViewBlocking((a,b,c) -> false));
        this.builder = builder;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onRemove(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        TileEntity entity = p_196243_2_.getBlockEntity(p_196243_3_);
        if (entity instanceof FESpawnerTE) {
            FESpawnerTE tileEntity = (FESpawnerTE) entity;
            tileEntity.drops();
        }
        super.onRemove(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
    }

    @Override
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        if (!world.isClientSide) {
            TileEntity tileentity = world.getBlockEntity(blockPos);
            if (tileentity instanceof FESpawnerTE) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (FESpawnerTE) tileentity, blockPos);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FESpawnerTE(builder);
    }

    @Override
    @MethodsReturnNonnullByDefault
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    public static class Builder {

        public static final Builder tier1 = new Builder().setRange(4).setMaxEntities(5).setMaxUpgrade(0).setSpawnTime(40);

        public int range = 4;
        public int maxEntities = 5;
        public int upgradeLimit = 0;
        public int spawnTime = 40;

        public Builder() {

        }

        public Builder(final int range, final int maxEntities, final int upgradeLimit) {
            this.range = range;
            this.maxEntities = maxEntities;
            this.upgradeLimit = upgradeLimit;
        }

        public Builder setRange(int range) {
            this.range = range;
            return this;
        }

        public Builder setMaxEntities(int maxEntities) {
            this.maxEntities = maxEntities;
            return this;
        }

        public Builder setMaxUpgrade(int mU) {
            this.upgradeLimit = mU;
            return this;
        }

        public Builder setSpawnTime(int ST) {
            this.spawnTime = ST;
            return this;
        }

        public FESpawner build() {
            return new FESpawner(this);
        }
    }
}
