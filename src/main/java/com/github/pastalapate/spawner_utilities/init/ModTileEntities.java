package com.github.pastalapate.spawner_utilities.init;

import com.github.pastalapate.spawner_utilities.Main;
import com.github.pastalapate.spawner_utilities.tiles_entities.InfinityEnergyBlockTileEntity;
import com.github.pastalapate.spawner_utilities.tiles_entities.fe_spawner_TE;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MOD_ID);
    public static final RegistryObject<TileEntityType<fe_spawner_TE>> FE_SPAWNER = TILE_ENTITY_TYPES.register("fe_spawner",
            () -> TileEntityType.Builder.of(fe_spawner_TE::new, ModBlocks.FE_SPAWNER.get()).build(null));
    public static final RegistryObject<TileEntityType<InfinityEnergyBlockTileEntity>> INFINITY_ENERGY = TILE_ENTITY_TYPES.register("infinity_energy_block",
            () -> TileEntityType.Builder.of(InfinityEnergyBlockTileEntity::new, ModBlocks.INFINITY_ENERGY_BLOCK.get()).build(null));
}
