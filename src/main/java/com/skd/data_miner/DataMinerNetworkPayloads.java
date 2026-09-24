package com.skd.data_miner;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public final class DataMinerNetworkPayloads {
    private DataMinerNetworkPayloads() {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(DataMiner.MODID, path);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCustomPayloadPacket(payload));
        }
    }

    public record ClientReloadPayload(long timestamp, String[] stackTrace)
        implements CustomPacketPayload {

        public static final Type<ClientReloadPayload> TYPE = new Type<>(id("client_reload"));

        public static final StreamCodec<FriendlyByteBuf, ClientReloadPayload> STREAM_CODEC =
            CustomPacketPayload.codec(ClientReloadPayload::write, ClientReloadPayload::new);

        public ClientReloadPayload(FriendlyByteBuf buffer) {
            this(buffer.readLong(), buffer.readUtf().split("\n"));
        }

        private void write(FriendlyByteBuf buffer) {
            buffer.writeLong(this.timestamp);
            buffer.writeUtf(String.join("\n", this.stackTrace));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}