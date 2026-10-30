package com.skd.data_miner;

import com.mojang.logging.LogUtils;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DataMiner.MODID, value = Dist.CLIENT)
public final class DataMinerClientNetworking {
    private static final Logger LOGGER = LogUtils.getLogger();

    private DataMinerClientNetworking() {}

    // Minecraft.getInstance() is still null during mod construction, so the listener
    // must be registered through the mod-bus event instead of the resource manager.
    @SubscribeEvent
    public static void onAddClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(DataMinerNetworkPayloads.id("client_reload_detector"), new PreparableReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparableReloadListener.SharedState state,
                    java.util.concurrent.Executor bgExec,
                    PreparableReloadListener.PreparationBarrier barrier,
                    java.util.concurrent.Executor gameExec) {
                long ts = System.currentTimeMillis();
                StackTraceElement[] rawStack = Thread.currentThread().getStackTrace();
                String[] stack = Arrays.stream(rawStack).map(Object::toString).toArray(String[]::new);
                LOGGER.warn("[DataMiner] Client resource reload detected at {} (thread={})", ts, Thread.currentThread().getName());
                for (int i = 0; i < Math.min(stack.length, 15); i++) {
                    LOGGER.warn("  at {}", stack[i]);
                }
                DataMinerNetworkPayloads.sendToServer(new DataMinerNetworkPayloads.ClientReloadPayload(ts, stack));
                return barrier.wait(net.minecraft.util.Unit.INSTANCE).thenRun(() -> {});
            }
        });
    }
}