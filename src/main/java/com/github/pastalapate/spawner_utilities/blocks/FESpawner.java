package com.github.pastalapate.spawner_utilities.blocks;

import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class FESpawner extends Block  {

    private final Builder builder;
    public static final List<RegistryObject<Block>> tiers = new ArrayList<>();

    static {
        tiers.add(ModBlocks.FE_SPAWNER);
        tiers.add(ModBlocks.FE_SPAWNER_TIER2);
    }

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

    public static boolean isB(Block block) {
        for (RegistryObject<Block> tier : tiers) {
            if (block.is(tier.get())) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {

        public static final Builder tier1 = new Builder("Tier 1").setEnergyCons(100).setRange(4).setMaxEntities(5).setMaxUpgrade(0).setSpawnTime(40).setTileEntity(ModTileEntities.FE_SPAWNER);
        public static final Builder tier2 = new Builder("Tier 2").setEnergyCons(200).setRange(3).setMaxEntities(10).setMaxUpgrade(2).setSpawnTime(20).setTileEntity(ModTileEntities.FE_SPAWNER_TIER2);

        public int range = 4;
        public int maxEntities = 5;
        public int upgradeLimit = 0;
        public int spawnTime = 40;
        public RegistryObject<TileEntityType<FESpawnerTE>> tileEntity;
        public int energyCons;
        public final String name;

        public Builder(String name) {
            this.name = name;
        }

        public Builder setEnergyCons(int energyCons) {
            this.energyCons = energyCons;
            return this;
        }

        public Builder setRange(int range) {
            this.range = range;
            return this;
        }

        public Builder setTileEntity(RegistryObject<TileEntityType<FESpawnerTE>>tileEntity) {
            this.tileEntity = tileEntity;
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
