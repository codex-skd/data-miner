package com.skd.data_miner.latency;

import com.skd.data_miner.perf.PerformanceMonitor;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class ClientPerfHandlers {

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!PerformanceMonitor.isRunning()) return;
			Minecraft mc = Minecraft.getInstance();
			if (mc != null) {
				PerformanceMonitor.recordFrame(mc.getFps());
			}
		});
	}
}
