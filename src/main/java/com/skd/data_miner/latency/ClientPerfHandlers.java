package com.skd.data_miner.latency;

import com.skd.data_miner.DataMiner;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import com.skd.data_miner.perf.PerformanceMonitor;

@EventBusSubscriber(modid = DataMiner.MODID, value = Dist.CLIENT)
public class ClientPerfHandlers {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        if (!PerformanceMonitor.isRunning()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc != null) {
            PerformanceMonitor.recordFrame(mc.getFps());
        }
    }
}
