package com.skd.dataminer.dumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.skd.dataminer.DataMiner;
import com.skd.dataminer.DataMinerConfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegistryDumper {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void dumpAll() {
        Path outputDir = Paths.get(DataMinerConfig.DUMP_OUTPUT_DIR.get());
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            DataMiner.LOGGER.error("Failed to create dump directory: {}", outputDir, e);
            return;
        }

        if (DataMinerConfig.DUMP_BLOCKS.get()) {
            dumpBlocks(outputDir.resolve("blocks.json"));
        }
        if (DataMinerConfig.DUMP_ITEMS.get()) {
            dumpItems(outputDir.resolve("items.json"));
        }
        if (DataMinerConfig.DUMP_ENTITIES.get()) {
            dumpEntities(outputDir.resolve("entities.json"));
        }
        if (DataMinerConfig.DUMP_BIOMES.get()) {
            dumpRegistry(BuiltInRegistries.BIOME, outputDir.resolve("biomes.json"), "biomes");
        }
        if (DataMinerConfig.DUMP_ENCHANTMENTS.get()) {
            dumpRegistry(BuiltInRegistries.ENCHANTMENT, outputDir.resolve("enchantments.json"), "enchantments");
        }
        if (DataMinerConfig.DUMP_STATUS_EFFECTS.get()) {
            dumpRegistry(BuiltInRegistries.MOB_EFFECT, outputDir.resolve("status_effects.json"), "status_effects");
        }
        if (DataMinerConfig.DUMP_SOUND_EVENTS.get()) {
            dumpRegistry(BuiltInRegistries.SOUND_EVENT, outputDir.resolve("sound_events.json"), "sound_events");
        }
        if (DataMinerConfig.DUMP_CREATIVE_TABS.get()) {
            dumpRegistry(BuiltInRegistries.CREATIVE_MODE_TAB, outputDir.resolve("creative_tabs.json"), "creative_tabs");
        }
        if (DataMinerConfig.DUMP_DIMENSIONS.get()) {
            dumpRegistry(BuiltInRegistries.DIMENSION_TYPE, outputDir.resolve("dimension_types.json"), "dimension_types");
        }
        if (DataMinerConfig.DUMP_POTIONS.get()) {
            dumpRegistry(BuiltInRegistries.POTION, outputDir.resolve("potions.json"), "potions");
        }
        if (DataMinerConfig.DUMP_VILLAGER_PROFESSIONS.get()) {
            dumpRegistry(BuiltInRegistries.VILLAGER_PROFESSION, outputDir.resolve("villager_professions.json"), "villager_professions");
        }
        if (DataMinerConfig.DUMP_ATTRIBUTES.get()) {
            dumpRegistry(BuiltInRegistries.ATTRIBUTE, outputDir.resolve("attributes.json"), "attributes");
        }
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
                var defaultState = block.defaultBlockState();
                SoundType sound = block.getSoundType(defaultState);
                entryJson.addProperty("hardness", getFloatOrZero(() -> defaultState.getDestroySpeed(null, null)));
                entryJson.addProperty("blast_resistance", getFloatOrZero(block::getExplosionResistance));
                entryJson.addProperty("light_emission", defaultState.getLightEmission());
                entryJson.addProperty("requires_correct_tool", defaultState.requiresCorrectToolForDrops());
                entryJson.addProperty("has_collision", defaultState.hasBlockCollision());
                entryJson.addProperty("randomly_ticks", block.isRandomlyTicking(defaultState));

                JsonObject soundObj = new JsonObject();
                soundObj.addProperty("volume", sound.getVolume());
                soundObj.addProperty("pitch", sound.getPitch());
                soundObj.addProperty("break_sound", sound.getBreakSound().getLocation().toString());
                soundObj.addProperty("step_sound", sound.getStepSound().getLocation().toString());
                soundObj.addProperty("place_sound", sound.getPlaceSound().getLocation().toString());
                soundObj.addProperty("hit_sound", sound.getHitSound().getLocation().toString());
                soundObj.addProperty("fall_sound", sound.getFallSound().getLocation().toString());
                entryJson.add("sound_type", soundObj);
            } catch (Exception e) {
                entryJson.addProperty("error", "Failed to extract block properties: " + e.getMessage());
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
                entryJson.addProperty("max_stack_size", item.getDefaultMaxStackSize());
                entryJson.addProperty("max_damage", item.getMaxDamage());
                entryJson.addProperty("is_fire_resistant", item.isFireResistant());
                entryJson.addProperty("rarity", item.getRarity(item.getDefaultInstance()).toString());
                entryJson.addProperty("is_edible", item.isEdible());

                if (item.isEdible()) {
                    FoodProperties food = item.getFoodProperties(item.getDefaultInstance(), null);
                    if (food != null) {
                        JsonObject foodObj = new JsonObject();
                        foodObj.addProperty("nutrition", food.nutrition());
                        foodObj.addProperty("saturation", food.saturation());
                        foodObj.addProperty("can_always_eat", food.canAlwaysEat());
                        foodObj.addProperty("is_fast_food", food.isFastFood());
                        foodObj.addProperty("eat_seconds", food.eatDurationTicks() / 20f);
                        entryJson.add("food_properties", foodObj);
                    }
                }
            } catch (Exception e) {
                entryJson.addProperty("error", "Failed to extract item properties: " + e.getMessage());
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
                entryJson.addProperty("dimensions", entityType.getDescription().toString());
            } catch (Exception e) {
                entryJson.addProperty("error", "Failed to extract entity properties: " + e.getMessage());
            }

            entries.add(entryJson);
        }

        root.add("entries", entries);
        writeJson(outputPath, root, entries.size());
    }

    private static <T> void dumpRegistry(net.minecraft.core.Registry<T> registry, Path outputPath, String registryName) {
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

    private static float getFloatOrZero(FloatSupplier supplier) {
        try {
            return supplier.getAsFloat();
        } catch (Exception e) {
            return 0f;
        }
    }

    @FunctionalInterface
    private interface FloatSupplier {
        float getAsFloat() throws Exception;
    }
}
