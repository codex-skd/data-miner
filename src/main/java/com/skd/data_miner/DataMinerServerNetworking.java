package com.skd.data_miner;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class DataMinerServerNetworking {
    private static final Logger LOGGER = LogUtils.getLogger();

    private DataMinerServerNetworking() {}

    public static void handleClientReload(ServerPlayer player, DataMinerNetworkPayloads.ClientReloadPayload payload) {
        LOGGER.warn("[DataMiner] Client reload from {} (UUID={}) at {}",
            player.getName().getString(), player.getUUID(), payload.timestamp());
        for (String frame : payload.stackTrace()) {
            LOGGER.warn("  at {}", frame);
        }
    }
}