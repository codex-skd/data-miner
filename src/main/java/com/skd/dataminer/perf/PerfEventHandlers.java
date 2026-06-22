package com.skd.dataminer.perf;

import com.skd.dataminer.DataMiner;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = DataMiner.MODID)
public class PerfEventHandlers {

    private static long serverTickStart;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        if (!PerformanceMonitor.isRunning()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc != null) {
            PerformanceMonitor.recordFrame(mc.getFps());
        }
    }

    @SubscribeEvent
    public static void onServerTickPre(ServerTickEvent.Pre event) {
        serverTickStart = System.nanoTime();
    }

    @SubscribeEvent
    public static void onServerTickPost(ServerTickEvent.Post event) {
        if (!PerformanceMonitor.isRunning()) return;
        double mspt = (System.nanoTime() - serverTickStart) / 1_000_000.0;
        PerformanceMonitor.recordTick(mspt);
    }
}
