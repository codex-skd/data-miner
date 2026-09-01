package com.skd.data_miner;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class DataMinerClientNetworking {
    private static final Logger LOGGER = LogUtils.getLogger();

    private DataMinerClientNetworking() {}

    public static void registerReloadListener() {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.getResourceManager() instanceof ReloadableResourceManager rrm)) {
            return;
        }

        rrm.registerReloadListener(new PreparableReloadListener() {
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager,
                    ProfilerFiller prepProfiler, ProfilerFiller reloadProfiler,
                    Executor bgExec, Executor gameExec) {
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
