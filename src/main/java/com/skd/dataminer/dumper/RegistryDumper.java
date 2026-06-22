package com.skd.dataminer.dumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.skd.dataminer.DataMiner;
import com.skd.dataminer.DataMinerConfig;
import com.skd.dataminer.init.Initializer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegistryDumper {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void dumpAll() {
        Path outputDir = Initializer.baseDir.resolve("startup/registries");
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            DataMiner.LOGGER.error("Failed to create dump directory: {}", outputDir, e);
            return;
        }

        if (DataMinerConfig.DUMP_BLOCKS.get()) dumpBlocks(outputDir.resolve("blocks.json"));
        if (DataMinerConfig.DUMP_ITEMS.get()) dumpItems(outputDir.resolve("items.json"));
        if (DataMinerConfig.DUMP_SOUND_EVENTS.get()) dumpSimple(BuiltInRegistries.SOUND_EVENT, outputDir.resolve("sound_events.json"), "sound_events");
        if (DataMinerConfig.DUMP_CREATIVE_TABS.get()) dumpSimple(BuiltInRegistries.CREATIVE_MODE_TAB, outputDir.resolve("creative_tabs.json"), "creative_tabs");
        if (DataMinerConfig.DUMP_POTIONS.get()) dumpSimple(BuiltInRegistries.POTION, outputDir.resolve("potions.json"), "potions");
        if (DataMinerConfig.DUMP_VILLAGER_PROFESSIONS.get()) dumpSimple(BuiltInRegistries.VILLAGER_PROFESSION, outputDir.resolve("villager_professions.json"), "villager_professions");
        if (DataMinerConfig.DUMP_ATTRIBUTES.get()) dumpSimple(BuiltInRegistries.ATTRIBUTE, outputDir.resolve("attributes.json"), "attributes");
        if (DataMinerConfig.DUMP_ENTITIES.get()) dumpEntities(outputDir.resolve("entities.json"));
        if (DataMinerConfig.DUMP_STATUS_EFFECTS.get()) dumpSimple(BuiltInRegistries.MOB_EFFECT, outputDir.resolve("status_effects.json"), "status_effects");
    }

    private static void dumpBlocks(Path outputPath) {
        DataMiner.LOGGER.info("Dumping registry: blocks");
        var registry = BuiltInRegistries.BLOCK;
        JsonObject root = new JsonObject();
        root.addProperty("registry_name", "blocks");
        root.addProperty("size", registry.size());

        JsonArray entries = new JsonArray();
        for (Block block : registry) {
            Identifier id = registry.getKey(block);
            if (id == null) continue;

            JsonObject entryJson = new JsonObject();
            entryJson.addProperty("id", id.toString());
            entryJson.addProperty("namespace", id.getNamespace());
            entryJson.addProperty("path", id.getPath());
            entryJson.addProperty("type", block.getClass().getName());

            try {
                var state = block.defaultBlockState();
                entryJson.addProperty("hardness", state.getDestroySpeed(null, null));
                entryJson.addProperty("blast_resistance", block.getExplosionResistance());
                entryJson.addProperty("light_emission", state.getLightEmission());
                entryJson.addProperty("has_block_entity", state.hasBlockEntity());

                try {
                    SoundType soundType = state.getSoundType();
                    JsonObject soundObj = new JsonObject();
                    soundObj.addProperty("volume", soundType.getVolume());
                    soundObj.addProperty("pitch", soundType.getPitch());
                    Identifier breakId = BuiltInRegistries.SOUND_EVENT.getKey(soundType.getBreakSound());
                    soundObj.addProperty("break_sound", breakId != null ? breakId.toString() : "unknown");
                    Identifier stepId = BuiltInRegistries.SOUND_EVENT.getKey(soundType.getStepSound());
                    soundObj.addProperty("step_sound", stepId != null ? stepId.toString() : "unknown");
                    Identifier placeId = BuiltInRegistries.SOUND_EVENT.getKey(soundType.getPlaceSound());
                    soundObj.addProperty("place_sound", placeId != null ? placeId.toString() : "unknown");
                    Identifier hitId = BuiltInRegistries.SOUND_EVENT.getKey(soundType.getHitSound());
                    soundObj.addProperty("hit_sound", hitId != null ? hitId.toString() : "unknown");
                    Identifier fallId = BuiltInRegistries.SOUND_EVENT.getKey(soundType.getFallSound());
                    soundObj.addProperty("fall_sound", fallId != null ? fallId.toString() : "unknown");
                    entryJson.add("sound_type", soundObj);
                } catch (Exception e) {
                    entryJson.addProperty("sound_type_error", e.getMessage());
                }
            } catch (Exception e) {
                entryJson.addProperty("error", e.getMessage());
            }

            entries.add(entryJson);
        }

        root.add("entries", entries);
        writeJson(outputPath, root, entries.size());
    }

    private static void dumpItems(Path outputPath) {
        DataMiner.LOGGER.info("Dumping registry: items");
        var registry = BuiltInRegistries.ITEM;
        JsonObject root = new JsonObject();
        root.addProperty("registry_name", "items");
        root.addProperty("size", registry.size());

        JsonArray entries = new JsonArray();
        for (Item item : registry) {
            Identifier id = registry.getKey(item);
            if (id == null) continue;

            JsonObject entryJson = new JsonObject();
            entryJson.addProperty("id", id.toString());
            entryJson.addProperty("namespace", id.getNamespace());
            entryJson.addProperty("path", id.getPath());
            entryJson.addProperty("type", item.getClass().getName());

            try {
                ItemStack stack = item.getDefaultInstance();
                entryJson.addProperty("max_stack_size", stack.getMaxStackSize());
                entryJson.addProperty("max_damage", stack.getMaxDamage());
                entryJson.addProperty("rarity", stack.getRarity().toString());

                try {
                    FoodProperties food = stack.get(DataComponents.FOOD);
                    if (food != null) {
                        JsonObject foodObj = new JsonObject();
                        foodObj.addProperty("nutrition", food.nutrition());
                        foodObj.addProperty("saturation", food.saturation());
                        foodObj.addProperty("can_always_eat", food.canAlwaysEat());
                        entryJson.add("food_properties", foodObj);
                    }
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                entryJson.addProperty("error", e.getMessage());
            }

            entries.add(entryJson);
        }

        root.add("entries", entries);
        writeJson(outputPath, root, entries.size());
    }

    private static void dumpEntities(Path outputPath) {
        DataMiner.LOGGER.info("Dumping registry: entity_types");
        var registry = BuiltInRegistries.ENTITY_TYPE;
        JsonObject root = new JsonObject();
        root.addProperty("registry_name", "entity_types");
        root.addProperty("size", registry.size());

        JsonArray entries = new JsonArray();
        for (EntityType<?> entityType : registry) {
            Identifier id = registry.getKey(entityType);
            if (id == null) continue;

            JsonObject entryJson = new JsonObject();
            entryJson.addProperty("id", id.toString());
            entryJson.addProperty("namespace", id.getNamespace());
            entryJson.addProperty("path", id.getPath());
            entryJson.addProperty("type", entityType.getClass().getName());

            try {
                entryJson.addProperty("width", entityType.getWidth());
                entryJson.addProperty("height", entityType.getHeight());
                entryJson.addProperty("category", entityType.getCategory().getName());
                entryJson.addProperty("fire_immune", entityType.fireImmune());
                entryJson.addProperty("can_summon", entityType.canSummon());
                entryJson.addProperty("client_tracking_range", entityType.clientTrackingRange());
                entryJson.addProperty("update_interval", entityType.updateInterval());
                entryJson.addProperty("description_id", entityType.getDescriptionId());
            } catch (Exception e) {
                entryJson.addProperty("error", e.getMessage());
            }

            entries.add(entryJson);
        }

        root.add("entries", entries);
        writeJson(outputPath, root, entries.size());
    }

    private static <T> void dumpSimple(net.minecraft.core.Registry<T> registry, Path outputPath, String registryName) {
        DataMiner.LOGGER.info("Dumping registry: {}", registryName);
        JsonObject root = new JsonObject();
        root.addProperty("registry_name", registryName);
        root.addProperty("size", registry.size());

        JsonArray entries = new JsonArray();
        for (T entry : registry) {
            Identifier id = registry.getKey(entry);
            if (id == null) continue;
            JsonObject entryJson = new JsonObject();
            entryJson.addProperty("id", id.toString());
            entryJson.addProperty("namespace", id.getNamespace());
            entryJson.addProperty("path", id.getPath());
            entryJson.addProperty("type", entry.getClass().getName());
            entryJson.addProperty("to_string", entry.toString());
            entries.add(entryJson);
        }

        root.add("entries", entries);
        writeJson(outputPath, root, entries.size());
    }

    private static void writeJson(Path outputPath, JsonObject root, int entryCount) {
        try {
            Files.writeString(outputPath, GSON.toJson(root));
            DataMiner.LOGGER.info("Dumped {} entries to {}", entryCount, outputPath);
        } catch (IOException e) {
            DataMiner.LOGGER.error("Failed to write registry dump: {}", outputPath, e);
        }
    }
}
