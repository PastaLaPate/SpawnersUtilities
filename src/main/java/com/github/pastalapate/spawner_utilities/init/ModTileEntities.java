package com.github.pastalapate.spawner_utilities.init;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.blocks.FESpawner;
import com.github.pastalapate.spawner_utilities.tiles_entities.InfinityEnergyBlockTileEntity;
import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SpawnerUtilities.MOD_ID);
    public static final RegistryObject<TileEntityType<FESpawnerTE>> FE_SPAWNER = TILE_ENTITY_TYPES.register("fe_spawner_tier1",
            () -> TileEntityType.Builder.of(() -> new FESpawnerTE(FESpawner.Builder.tier1), ModBlocks.FE_SPAWNER.get()).build(null));
    public static final RegistryObject<TileEntityType<FESpawnerTE>> FE_SPAWNER_TIER2 = TILE_ENTITY_TYPES.register("fe_spawner_tier2",
            () -> TileEntityType.Builder.of(() -> new FESpawnerTE(FESpawner.Builder.tier2), ModBlocks.FE_SPAWNER_TIER2.get()).build(null));
    public static final RegistryObject<TileEntityType<InfinityEnergyBlockTileEntity>> INFINITY_ENERGY = TILE_ENTITY_TYPES.register("infinity_energy_block",
            () -> TileEntityType.Builder.of(InfinityEnergyBlockTileEntity::new, ModBlocks.INFINITY_ENERGY_BLOCK.get()).build(null));
}
