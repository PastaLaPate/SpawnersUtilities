package com.github.pastalapate.spawner_utilities.networking.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface IPacket {
    void toBytes(PacketBuffer buf);
    boolean handle(Supplier<NetworkEvent.Context> supplier);
}
