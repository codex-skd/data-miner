package com.skd.data_miner.latency;

import com.skd.data_miner.DataMiner;
import com.skd.data_miner.DataMinerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = DataMiner.MODID)
public class LatencyEventHandlers {

    private static final Map<String, Long> eatStartTimes = new ConcurrentHashMap<>();
    private static final Map<String, BreakEntry> breakEntries = new ConcurrentHashMap<>();
    private static boolean alwaysOnChecked = false;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!alwaysOnChecked) {
            alwaysOnChecked = true;
            if (DataMinerConfig.LATENCY_ALWAYS_ON.get()) LatencyTracer.ensureRunning();
        }
        if (DataMinerConfig.LATENCY_ALWAYS_ON.get()) LatencyTracer.ensureRunning();

        if (!LatencyTracer.isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        String uuid = player.getUUID().toString();
        BreakEntry entry = breakEntries.get(uuid);
        if (entry == null) return;

        BlockState state = player.level().getBlockState(entry.pos);
        if (state.isAir()) {
            breakEntries.remove(uuid);
            double ms = (System.nanoTime() - entry.startNano) / 1_000_000.0;
            LatencyTracer.recordBlockBreak(entry.blockName, ms);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!LatencyTracer.isRunning()) return;
        Player player = event.getEntity();

        BreakEntry current = breakEntries.get(player.getUUID().toString());
        if (current != null && current.pos.equals(event.getPos())) return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.isAir()) return;

        String blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        breakEntries.put(player.getUUID().toString(),
                new BreakEntry(event.getPos(), blockName, System.nanoTime()));
    }

    @SubscribeEvent
    public static void onEatStart(LivingEntityUseItemEvent.Start event) {
        if (DataMinerConfig.LATENCY_ALWAYS_ON.get()) LatencyTracer.ensureRunning();
        if (!LatencyTracer.isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        eatStartTimes.put(player.getUUID().toString(), System.nanoTime());
    }

    @SubscribeEvent
    public static void onEatStop(LivingEntityUseItemEvent.Stop event) {
        if (DataMinerConfig.LATENCY_ALWAYS_ON.get()) LatencyTracer.ensureRunning();
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

    private static class BreakEntry {
        final BlockPos pos;
        final String blockName;
        final long startNano;

        BreakEntry(BlockPos pos, String blockName, long startNano) {
            this.pos = pos;
            this.blockName = blockName;
            this.startNano = startNano;
        }
    }
}
