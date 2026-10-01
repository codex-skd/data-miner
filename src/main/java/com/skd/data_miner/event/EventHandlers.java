package com.skd.data_miner.event;

import com.google.gson.JsonObject;

import com.skd.data_miner.DataMiner;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.time.Instant;

public class EventHandlers {

	private static long lastPlayerTickTime = 0;
	private static final long PLAYER_TICK_INTERVAL_MS = 1000;

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(EventHandlers::onServerTick);

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			onPlayerInteract(player, hitResult.getBlockPos());
			if (player.getItemInHand(hand).getItem() instanceof BlockItem blockItem) {
				onBlockPlace(player, blockItem.getBlock(), hitResult.getBlockPos());
			}
			return net.minecraft.world.InteractionResult.PASS;
		});

		ServerLivingEntityEvents.AFTER_DAMAGE.register(EventHandlers::onEntityDamage);

		ServerLivingEntityEvents.AFTER_DEATH.register(EventHandlers::onEntityDeath);

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> onEntityJoin(entity));

		ServerChunkEvents.CHUNK_LOAD.register((world, chunk, loaded) -> {
			if (EventTracer.isRunning()) {
				try {
					JsonObject data = new JsonObject();
					data.addProperty("chunk_x", chunk.getPos().getRegionX());
					data.addProperty("chunk_z", chunk.getPos().getRegionZ());
					data.addProperty("dimension", world.dimension().toString());

					recordEvent("chunk_load", null, data);
				} catch (Exception e) {
					EventTracer.saveError(e, "chunk_load");
				}
			}
		});
	}

	private static void onServerTick(MinecraftServer server) {
		if (!EventTracer.isRunning()) return;

		long now = System.currentTimeMillis();
		if (now - lastPlayerTickTime < PLAYER_TICK_INTERVAL_MS) return;
		lastPlayerTickTime = now;

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			try {
				Vec3 pos = player.position();
				JsonObject data = new JsonObject();
				data.addProperty("x", pos.x);
				data.addProperty("y", pos.y);
				data.addProperty("z", pos.z);
				data.addProperty("sprinting", player.isSprinting());
				data.addProperty("sneaking", player.isCrouching());
				data.addProperty("on_ground", player.onGround());
				data.addProperty("health", player.getHealth());
				data.addProperty("food_level", player.getFoodData().getFoodLevel());

				try {
					var biomeHolder = player.level().getBiome(player.blockPosition());
					biomeHolder.unwrapKey().ifPresent(key -> data.addProperty("biome", key.toString()));
				} catch (Exception ignored) {
				}

				data.addProperty("dimension", player.level().dimension().toString());

				recordEvent("player_move", player, data);
			} catch (Exception e) {
				EventTracer.saveError(e, "player_move");
			}
		}
	}

	private static void onBlockPlace(Player player, Block block, BlockPos pos) {
		if (!EventTracer.isRunning()) return;
		try {
			Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);

			JsonObject data = new JsonObject();
			data.addProperty("block", blockId != null ? blockId.toString() : "unknown");
			data.addProperty("x", pos.getX());
			data.addProperty("y", pos.getY());
			data.addProperty("z", pos.getZ());
			data.addProperty("dimension", player.level().dimension().toString());

			recordEvent("block_place", player, data);
		} catch (Exception e) {
			EventTracer.saveError(e, "block_place");
		}
	}

	private static void onPlayerInteract(Player player, BlockPos pos) {
		if (!EventTracer.isRunning()) return;
		try {
			Identifier blockId = BuiltInRegistries.BLOCK.getKey(player.level().getBlockState(pos).getBlock());

			JsonObject data = new JsonObject();
			data.addProperty("block", blockId != null ? blockId.toString() : "unknown");
			data.addProperty("x", pos.getX());
			data.addProperty("y", pos.getY());
			data.addProperty("z", pos.getZ());
			data.addProperty("dimension", player.level().dimension().toString());

			recordEvent("player_interact_block", player, data);
		} catch (Exception e) {
			EventTracer.saveError(e, "player_interact_block");
		}
	}

	private static void onEntityDamage(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source,
									  float baseAmount, float amount, boolean blocked) {
		if (!EventTracer.isRunning()) return;
		if (!(entity instanceof Player player)) return;
		try {
			JsonObject data = new JsonObject();
			data.addProperty("amount", amount);
			data.addProperty("base_amount", baseAmount);
			data.addProperty("blocked", blocked);
			data.addProperty("dimension", player.level().dimension().toString());

			if (source.getEntity() != null) {
				Entity sourceEntity = source.getEntity();
				Identifier sourceId = BuiltInRegistries.ENTITY_TYPE.getKey(sourceEntity.getType());
				data.addProperty("source_entity", sourceId != null ? sourceId.toString() : "unknown");
			}

			recordEvent("player_damaged", player, data);
		} catch (Exception e) {
			EventTracer.saveError(e, "player_damaged");
		}
	}

	private static void onEntityDeath(LivingEntity entity, net.minecraft.world.damagesource.DamageSource damageSource) {
		if (!EventTracer.isRunning()) return;
		try {
			Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
			Vec3 pos = entity.position();

			JsonObject data = new JsonObject();
			data.addProperty("entity", entityId != null ? entityId.toString() : "unknown");
			data.addProperty("x", pos.x);
			data.addProperty("y", pos.y);
			data.addProperty("z", pos.z);
			data.addProperty("dimension", entity.level().dimension().toString());
			data.addProperty("was_player", entity instanceof Player);

			recordEvent("entity_death", null, data);
		} catch (Exception e) {
			EventTracer.saveError(e, "entity_death");
		}
	}

	private static void onEntityJoin(Entity entity) {
		if (!EventTracer.isRunning()) return;
		try {
			Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
			Vec3 pos = entity.position();

			JsonObject data = new JsonObject();
			data.addProperty("entity", entityId != null ? entityId.toString() : "unknown");
			data.addProperty("x", pos.x);
			data.addProperty("y", pos.y);
			data.addProperty("z", pos.z);
			data.addProperty("dimension", entity.level().dimension().toString());
			data.addProperty("is_player", entity instanceof Player);

			recordEvent("entity_join", null, data);
		} catch (Exception e) {
			EventTracer.saveError(e, "entity_join");
		}
	}

	private static void recordEvent(String type, Player player, JsonObject data) {
		JsonObject event = new JsonObject();
		event.addProperty("timestamp_ms", Instant.now().toEpochMilli());
		event.addProperty("event_type", type);
		if (player != null) {
			event.addProperty("player", player.getName().getString());
			event.addProperty("player_uuid", player.getUUID().toString());
		}
		event.add("data", data);
		EventTracer.record(event);
	}
}
