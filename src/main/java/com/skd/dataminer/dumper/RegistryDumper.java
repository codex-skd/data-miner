package com.skd.dataminer.dumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.skd.dataminer.DataMiner;
import com.skd.dataminer.DataMinerConfig;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

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
            dumpRegistry(BuiltInRegistries.BLOCK, outputDir.resolve("blocks.json"), "blocks");
        }
        if (DataMinerConfig.DUMP_ITEMS.get()) {
            dumpRegistry(BuiltInRegistries.ITEM, outputDir.resolve("items.json"), "items");
        }
        if (DataMinerConfig.DUMP_ENTITIES.get()) {
            dumpRegistry(BuiltInRegistries.ENTITY_TYPE, outputDir.resolve("entities.json"), "entity_types");
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

    private static <T> void dumpRegistry(net.minecraft.core.Registry<T> registry, Path outputPath, String registryName) {
        DataMiner.LOGGER.info("Dumping registry: {}", registryName);
        JsonObject root = new JsonObject();
        root.addProperty("registry_name", registryName);
        root.addProperty("size", registry.size());

        JsonArray entries = new JsonArray();
        for (T entry : registry) {
            Identifier id = registry.getKey(entry);
            if (id == null) {
                continue;
            }
            JsonObject entryJson = new JsonObject();
            entryJson.addProperty("id", id.toString());
            entryJson.addProperty("namespace", id.getNamespace());
            entryJson.addProperty("path", id.getPath());
            entryJson.addProperty("type", entry.getClass().getName());
            entryJson.addProperty("to_string", entry.toString());
            entries.add(entryJson);
        }

        root.add("entries", entries);

        try {
            Files.writeString(outputPath, GSON.toJson(root));
            DataMiner.LOGGER.info("Dumped {} entries to {}", entries.size(), outputPath);
        } catch (IOException e) {
            DataMiner.LOGGER.error("Failed to write registry dump: {}", outputPath, e);
        }
    }
}
