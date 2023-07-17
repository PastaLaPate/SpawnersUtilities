package com.github.pastalapate.spawner_utilities.networking;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.networking.packets.DamageSyncC2SPacket;
import com.github.pastalapate.spawner_utilities.networking.packets.EnergySyncS2CPacket;
import com.github.pastalapate.spawner_utilities.networking.packets.ItemStackSyncS2CPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SpawnerUtilities.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(EnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncS2CPacket::new)
                .encoder(EnergySyncS2CPacket::toBytes)
                .consumer(EnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemStackSyncS2CPacket::new)
                .encoder(ItemStackSyncS2CPacket::toBytes)
                .consumer(ItemStackSyncS2CPacket::handle)
                .add();

        net.messageBuilder(DamageSyncC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DamageSyncC2SPacket::new)
                .encoder(DamageSyncC2SPacket::toBytes)
                .consumer(DamageSyncC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}