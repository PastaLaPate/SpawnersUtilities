package com.github.pastalapate.spawner_utilities.networking.packets;

import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ItemStackSyncS2CPacket implements IPacket {
    private final ItemStackHandler itemStackHandler;
    private final BlockPos pos;

    public ItemStackSyncS2CPacket(ItemStackHandler itemStackHandler, BlockPos pos) {
        this.itemStackHandler = itemStackHandler;
        this.pos = pos;
    }

    public ItemStackSyncS2CPacket(PacketBuffer buf) {
        int size = buf.readVarInt();
        itemStackHandler = new ItemStackHandler(size);
        for (int i = 0; i < size; i++) {
            itemStackHandler.insertItem(i, buf.readItem(), false);
        }

        this.pos = buf.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        Collection<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            list.add(itemStackHandler.getStackInSlot(i));
        }

        buf.writeVarInt(list.size());
        for (ItemStack stack : list) {
            buf.writeItem(stack);
        }

        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            assert Minecraft.getInstance().level != null;
            if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof FESpawnerTE) {
                FESpawnerTE blockEntity = (FESpawnerTE) Minecraft.getInstance().level.getBlockEntity(pos);
                assert blockEntity != null;
                blockEntity.setHandler(this.itemStackHandler);
            }
        });
        return true;
    }
}