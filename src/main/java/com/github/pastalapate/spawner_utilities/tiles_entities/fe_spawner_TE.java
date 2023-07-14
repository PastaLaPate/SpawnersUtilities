package com.github.pastalapate.spawner_utilities.tiles_entities;

import com.github.pastalapate.spawner_utilities.Main;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class fe_spawner_TE extends TileEntity implements INamedContainerProvider ,ITickableTileEntity {

    public ModEnergyStorage energyStorage;

    public final ItemStackHandler itemHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
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

    int timer = 0;
    boolean active = true;
    EntityType<?> entityType = null;
    LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    int spawnRange = 5;

    public fe_spawner_TE() {
        super(ModTileEntities.FE_SPAWNER.get());
        this.energyStorage = new ModEnergyStorage(100_000, 300) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
            }


        };
    }

    public void setEnergyLevel(int energy) {
        this.energyStorage.setEnergy(energy);
    }

    @Override
    public void tick() {
        assert this.level != null;
        ItemStack item = itemHandler.getStackInSlot(0);
        boolean empty = item.isEmpty();

        if (item.isEmpty() || !item.getDescriptionId().equals(ModItems.SOUL_CONTAINER.get().getDescriptionId()) || item.getTag() == null || item.getTag().getString("entity") == null) {
            return;
        }
        entityType = EntityType.byString(item.getTag().getString("entity")).orElse(null);
        active = energyStorage.getEnergyStored() >= 100 && entityType != null;
        if (!this.level.isClientSide() && active) {
            this.timer++;
            energyStorage.extractEnergy(100, false);
            if (timer > 40) {
                timer = 0;
                BlockPos pos = this.worldPosition;
                Random r = new Random();
                double x = pos.getX() + r.nextInt(spawnRange*2)-spawnRange;
                double y = pos.getY() + 1;
                double z = pos.getZ() + r.nextInt(spawnRange*2)-spawnRange;
                Entity entity = entityType.create(this.level);
                assert entity != null;
                entity.moveTo(x, y, z);
                if (this.level.noCollision(entityType.getAABB(x, y, z))) {
                    this.level.addFreshEntity(entity);
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
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("fe_spawner.energy", energyStorage.getEnergyStored());
        nbt.putString("fe_spawner.entity_type", entityType != null ? entityType.toString() : "");
        return super.save(nbt);
    }

    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        this.itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.energyStorage.setEnergy(nbt.getInt("fe_spawner.energy"));
        this.entityType = EntityType.byString(nbt.getString("fe_spawner.entity_type")).orElse(null);
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

    @Override
    @MethodsReturnNonnullByDefault
    public ITextComponent getDisplayName() {
        return new StringTextComponent("FE Spawner");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer extraData = new PacketBuffer(Unpooled.buffer());
        extraData.writeBlockPos(this.getBlockPos()); // Write the BlockPos data to the buffer

        return new FESpawnerGUI(p_createMenu_1_, p_createMenu_2_, this, extraData);
    }

}
