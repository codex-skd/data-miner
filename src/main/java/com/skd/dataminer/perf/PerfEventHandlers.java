package com.skd.dataminer.perf;

import com.skd.dataminer.DataMiner;
import com.skd.dataminer.latency.LatencyTracer;
import com.skd.dataminer.modimpact.ModAnalyzer;
import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = DataMiner.MODID)
public class PerfEventHandlers {

    private static long serverTickStart;

    @SubscribeEvent
    public static void onServerTickPre(ServerTickEvent.Pre event) {
        serverTickStart = System.nanoTime();
    }

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post event) {
        double mspt = (System.nanoTime() - serverTickStart) / 1_000_000.0;

        if (PerformanceMonitor.isRunning()) {
            PerformanceMonitor.recordTick(mspt);
        }

        if (LatencyTracer.isRunning() && mspt > 50) {
            MinecraftServer server = event.getServer();
            int entityCount = 0;
            int chunkCount = 0;
            for (var level : server.getAllLevels()) {
                for (var e : level.getAllEntities()) entityCount++;
                chunkCount += level.getChunkSource().getLoadedChunksCount();
            }
            JsonObject modCtx = ModAnalyzer.snapshotWorldContext(server);
            LatencyTracer.recordSlowTick(mspt, entityCount, chunkCount, modCtx);
        }
    }
}
