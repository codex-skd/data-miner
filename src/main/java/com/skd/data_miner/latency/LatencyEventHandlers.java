package com.skd.data_miner.latency;

import com.skd.data_miner.DataMinerConfig;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LatencyEventHandlers {

	private static final Map<String, EatEntry> eatStartTimes = new ConcurrentHashMap<>();
	private static final Map<String, BreakEntry> breakEntries = new ConcurrentHashMap<>();

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(LatencyEventHandlers::onServerTick);

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			onLeftClickBlock(player, pos);
			return InteractionResult.PASS;
		});

		ItemEvents.USE.register((world, player, hand) -> {
			onEatStart(player, hand);
			return InteractionResult.PASS;
		});
	}

	private static void onServerTick(MinecraftServer server) {
		if (DataMinerConfig.LATENCY_ALWAYS_ON) LatencyTracer.ensureRunning();

		if (!LatencyTracer.isRunning()) return;

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			String uuid = player.getUUID().toString();

			BreakEntry entry = breakEntries.get(uuid);
			if (entry != null) {
				BlockState state = player.level().getBlockState(entry.pos);
				if (state.isAir()) {
					breakEntries.remove(uuid);
					double ms = (System.nanoTime() - entry.startNano) / 1_000_000.0;
					LatencyTracer.recordBlockBreak(entry.blockName, ms);
				}
			}

			EatEntry eat = eatStartTimes.get(uuid);
			if (eat != null && !player.isUsingItem()) {
				eatStartTimes.remove(uuid);
				double ms = (System.nanoTime() - eat.startNano) / 1_000_000.0;
				LatencyTracer.recordEat(eat.itemName, ms);
			}
		}
	}

	private static void onLeftClickBlock(Player player, BlockPos pos) {
		if (!LatencyTracer.isRunning()) return;

		BreakEntry current = breakEntries.get(player.getUUID().toString());
		if (current != null && current.pos.equals(pos)) return;

		BlockState state = player.level().getBlockState(pos);
		if (state.isAir()) return;

		String blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
		breakEntries.put(player.getUUID().toString(),
				new BreakEntry(pos, blockName, System.nanoTime()));
	}

	private static void onEatStart(Player player, InteractionHand hand) {
		if (DataMinerConfig.LATENCY_ALWAYS_ON) LatencyTracer.ensureRunning();
		if (!LatencyTracer.isRunning()) return;

		String uuid = player.getUUID().toString();
		if (eatStartTimes.containsKey(uuid)) return;

		ItemStack stack = player.getItemInHand(hand);
		if (stack.isEmpty()) return;

		String itemName = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
		eatStartTimes.put(uuid, new EatEntry(itemName, System.nanoTime()));
	}

	private static class EatEntry {
		final String itemName;
		final long startNano;

		EatEntry(String itemName, long startNano) {
			this.itemName = itemName;
			this.startNano = startNano;
		}
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
