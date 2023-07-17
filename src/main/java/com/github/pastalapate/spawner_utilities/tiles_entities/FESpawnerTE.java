package com.github.pastalapate.spawner_utilities.tiles_entities;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.energy.ModEnergyStorage;
import com.github.pastalapate.spawner_utilities.gui.FESpawnerGUI;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import com.github.pastalapate.spawner_utilities.init.ModTileEntities;
import com.github.pastalapate.spawner_utilities.networking.ModMessages;
import com.github.pastalapate.spawner_utilities.networking.packets.EnergySyncS2CPacket;
import com.github.pastalapate.spawner_utilities.networking.packets.ItemStackSyncS2CPacket;
import io.netty.buffer.Unpooled;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class FESpawnerTE extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    public ModEnergyStorage energyStorage;

    public final ItemStackHandler itemHandler;

    int timer = 0;
    boolean active = true;
    EntityType<?> entityType = null;
    LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    int spawnRange = 5;
    int entityLimit = 5;
    public int instance_id;
    public int entityCount;

    public FESpawnerTE() {
        super(ModTileEntities.FE_SPAWNER.get());
        this.energyStorage = new ModEnergyStorage(100_000, 300) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
            }
        };
        this.itemHandler = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                assert level != null;
                if (!level.isClientSide()) {
                    ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
                }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    return stack.getDescriptionId().equals(ModItems.SOUL_CONTAINER.get().getDescriptionId());
                } else {
                    return super.isItemValid(slot, stack);
                }
            }
        };
        FESpawnerTEManager.registerInstance(this);
    }

    public void setEnergyLevel(int energy) {
        this.energyStorage.setEnergy(energy);
    }

    @Override
    public void tick() {
        assert this.level != null;
        ItemStack item = itemHandler.getStackInSlot(0);
        boolean empty = item.isEmpty();
        if (empty || !item.getDescriptionId().equals(ModItems.SOUL_CONTAINER.get().getDescriptionId()) || item.getTag() == null) {
            return;
        } else {
            item.getTag().getString("entity");
        }
        entityType = EntityType.byString(item.getTag().getString("entity")).orElse(null);
        active = energyStorage.getEnergyStored() >= 100 && entityType != null && entityCount < entityLimit;
        if (!this.level.isClientSide() && active) {
            this.timer++;
            energyStorage.extractEnergy(100, false);
            if (timer > 40 && entityCount < entityLimit) {
                timer = 0;
                BlockPos pos = this.worldPosition;
                Random r = new Random();
                double x = pos.getX() + r.nextInt(spawnRange * 2) - spawnRange;
                double y = pos.getY() + 1;
                double z = pos.getZ() + r.nextInt(spawnRange * 2) - spawnRange;
                Entity entity = entityType.create(this.level);
                assert entity != null;
                entity.moveTo(x, y, z);
                if (this.level.noCollision(entityType.getAABB(x, y, z))) {
                    this.level.addFreshEntity(entity);
                    entity.getPersistentData().putInt("spawner_id", instance_id);
                    entityCount += 1;
                }
            }
        }
    }

    public void setHandler(ItemStackHandler itemStackHandler) {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            itemHandler.setStackInSlot(i, itemStackHandler.getStackInSlot(i));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
        lazyItemHandler.invalidate();
    }

    @Override
    @MethodsReturnNonnullByDefault
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("fe_spawner.energy", energyStorage.getEnergyStored());
        nbt.putString("fe_spawner.entity_type", entityType != null ? entityType.toString() : "");
        nbt.putInt("fe_spawner.entityCount", entityCount);
        nbt.putInt("fe_spawner.instance_id", instance_id); // Save the instance ID
        return super.save(nbt);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void load(BlockState blockState, CompoundNBT nbt) {
        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.energyStorage.setEnergy(nbt.getInt("fe_spawner.energy"));
        this.entityType = EntityType.byString(nbt.getString("fe_spawner.entity_type")).orElse(null);
        this.entityCount = nbt.getInt("fe_spawner.entityCount");
        this.instance_id = nbt.getInt("fe_spawner.instance_id"); // Load the instance ID
        super.load(blockState, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    public void drops() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            assert this.level != null;
            this.level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), itemHandler.getStackInSlot(i)));
        }
    }

    @Override
    @MethodsReturnNonnullByDefault
    public ITextComponent getDisplayName() {
        return new StringTextComponent("FE Spawner");
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer extraData = new PacketBuffer(Unpooled.buffer());
        extraData.writeBlockPos(this.getBlockPos()); // Write the BlockPos data to the buffer

        return new FESpawnerGUI(p_createMenu_1_, p_createMenu_2_, this, extraData);
    }

    public static class Listener {
        @SubscribeEvent
        public void onLivingEntityDies(LivingDeathEvent event) {
            if (event.getEntity().getPersistentData().getInt("spawner_id") == 0){return;}
            SpawnerUtilities.LOGGER.info(event.getEntity().getPersistentData().toString());
            for (FESpawnerTE instance : FESpawnerTEManager.getAllInstances()) {
                if (instance.instance_id == event.getEntity().getPersistentData().getInt("spawner_id")) {
                    instance.entityCount -= 1;
                }
            }
        }
    }
}
