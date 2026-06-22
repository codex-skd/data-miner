package com.skd.dataminer.event;

import com.google.gson.JsonObject;
import com.skd.dataminer.DataMiner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.time.Instant;

@EventBusSubscriber(modid = DataMiner.MODID)
public class EventHandlers {

    private static long lastPlayerTickTime = 0;
    private static final long PLAYER_TICK_INTERVAL_MS = 1000;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!EventTracer.isRunning()) return;

        long now = System.currentTimeMillis();
        if (now - lastPlayerTickTime < PLAYER_TICK_INTERVAL_MS) return;
        lastPlayerTickTime = now;

        try {
            Player player = event.getEntity();
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

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!EventTracer.isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        try {
            BlockPos pos = event.getPos();
            BlockState state = event.getPlacedBlock();
            Identifier blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());

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

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (!EventTracer.isRunning()) return;
        try {
            Player player = event.getEntity();
            BlockPos pos = event.getPos();
            Identifier blockId = BuiltInRegistries.BLOCK.getKey(event.getLevel().getBlockState(pos).getBlock());

            JsonObject data = new JsonObject();
            data.addProperty("block", blockId != null ? blockId.toString() : "unknown");
            data.addProperty("x", pos.getX());
            data.addProperty("y", pos.getY());
            data.addProperty("z", pos.getZ());
            data.addProperty("hand", event.getHand().name());
            data.addProperty("dimension", player.level().dimension().toString());

            recordEvent("player_interact_block", player, data);
        } catch (Exception e) {
            EventTracer.saveError(e, "player_interact_block");
        }
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent.Pre event) {
        if (!EventTracer.isRunning()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        try {
            JsonObject data = new JsonObject();
            data.addProperty("amount", event.getNewDamage());
            data.addProperty("dimension", player.level().dimension().toString());

            if (event.getSource().getEntity() != null) {
                Entity source = event.getSource().getEntity();
                Identifier sourceId = BuiltInRegistries.ENTITY_TYPE.getKey(source.getType());
                data.addProperty("source_entity", sourceId != null ? sourceId.toString() : "unknown");
            }

            recordEvent("player_damaged", player, data);
        } catch (Exception e) {
            EventTracer.saveError(e, "player_damaged");
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!EventTracer.isRunning()) return;
        try {
            Entity entity = event.getEntity();
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

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!EventTracer.isRunning()) return;
        try {
            Entity entity = event.getEntity();
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

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!EventTracer.isRunning()) return;
        try {
            JsonObject data = new JsonObject();
            data.addProperty("chunk_x", event.getChunk().getPos().getRegionX());
            data.addProperty("chunk_z", event.getChunk().getPos().getRegionZ());
            data.addProperty("dimension", event.getChunk().getLevel().dimension().toString());

            recordEvent("chunk_load", null, data);
        } catch (Exception e) {
            EventTracer.saveError(e, "chunk_load");
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
