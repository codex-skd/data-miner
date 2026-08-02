package com.skd.data_miner.perf;

import com.google.gson.JsonObject;

import com.skd.data_miner.latency.LatencyTracer;
import com.skd.data_miner.modimpact.ModAnalyzer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class PerfEventHandlers {

	private static long serverTickStart;

	public static void register() {
		ServerTickEvents.START_SERVER_TICK.register(server -> serverTickStart = System.nanoTime());

		ServerTickEvents.END_SERVER_TICK.register(PerfEventHandlers::onServerTickEnd);
	}

	private static void onServerTickEnd(MinecraftServer server) {
		double mspt = (System.nanoTime() - serverTickStart) / 1_000_000.0;

		if (PerformanceMonitor.isRunning()) {
			PerformanceMonitor.recordTick(mspt);
		}

		if (LatencyTracer.isRunning() && mspt > 50) {
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
