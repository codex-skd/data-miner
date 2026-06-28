package com.skd.dataminer.latency;

import com.skd.dataminer.DataMiner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = DataMiner.MODID)
public class LatencyEventHandlers {

    private static final Map<String, Long> eatStartTimes = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onEatStart(LivingEntityUseItemEvent.Start event) {
        if (!LatencyTracer.isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        eatStartTimes.put(player.getUUID().toString(), System.nanoTime());
    }

    @SubscribeEvent
    public static void onEatStop(LivingEntityUseItemEvent.Stop event) {
        if (!LatencyTracer.isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        String uuid = player.getUUID().toString();
        Long startNano = eatStartTimes.remove(uuid);
        if (startNano == null) return;

        double ms = (System.nanoTime() - startNano) / 1_000_000.0;
        ItemStack stack = event.getItem();
        String itemName = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        LatencyTracer.recordEat(itemName, ms);
    }
}
