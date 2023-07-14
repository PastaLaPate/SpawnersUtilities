package com.github.pastalapate.spawner_utilities.networking.packets;

import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class DamageSyncC2SPacket implements IPacket {

    int damage;
    ItemStack item;

    public DamageSyncC2SPacket(int damage, ItemStack item) {
        this.damage = damage;
        this.item = item;
    }

    public DamageSyncC2SPacket(PacketBuffer packetBuffer) {
        this.damage = packetBuffer.readInt();
        this.item = packetBuffer.readItem();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(damage);
        buf.writeItem(item);
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            item.setDamageValue(item.getDamageValue() + damage);
        });
        return true;
    }
}
