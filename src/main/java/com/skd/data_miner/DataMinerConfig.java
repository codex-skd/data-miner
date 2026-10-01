package com.skd.data_miner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataMinerConfig {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance()
			.getConfigDir().resolve("data_miner.json");

	public static boolean DUMP_ON_STARTUP = false;
	public static String DUMP_OUTPUT_DIR = "dataminer_dumps";
	public static boolean DUMP_BLOCKS = true;
	public static boolean DUMP_ITEMS = true;
	public static boolean DUMP_ENTITIES = true;
	public static boolean DUMP_BIOMES = true;
	public static boolean DUMP_ENCHANTMENTS = true;
	public static boolean DUMP_STATUS_EFFECTS = true;
	public static boolean DUMP_SOUND_EVENTS = true;
	public static boolean DUMP_CREATIVE_TABS = true;
	public static boolean DUMP_DIMENSIONS = true;
	public static boolean DUMP_POTIONS = true;
	public static boolean DUMP_VILLAGER_PROFESSIONS = true;
	public static boolean DUMP_ATTRIBUTES = true;
	public static boolean LATENCY_ALWAYS_ON = true;

	public static void load() {
		if (!Files.exists(CONFIG_PATH)) {
			save();
			return;
		}

		try {
			ConfigData data = GSON.fromJson(Files.readString(CONFIG_PATH), ConfigData.class);
			if (data == null) {
				save();
				return;
			}

			DUMP_ON_STARTUP = data.dumpOnStartup;
			DUMP_OUTPUT_DIR = data.dumpOutputDir;
			DUMP_BLOCKS = data.dumpBlocks;
			DUMP_ITEMS = data.dumpItems;
			DUMP_ENTITIES = data.dumpEntities;
			DUMP_BIOMES = data.dumpBiomes;
			DUMP_ENCHANTMENTS = data.dumpEnchantments;
			DUMP_STATUS_EFFECTS = data.dumpStatusEffects;
			DUMP_SOUND_EVENTS = data.dumpSoundEvents;
			DUMP_CREATIVE_TABS = data.dumpCreativeTabs;
			DUMP_DIMENSIONS = data.dumpDimensions;
			DUMP_POTIONS = data.dumpPotions;
			DUMP_VILLAGER_PROFESSIONS = data.dumpVillagerProfessions;
			DUMP_ATTRIBUTES = data.dumpAttributes;
			LATENCY_ALWAYS_ON = data.latencyAlwaysOn;
		} catch (IOException e) {
			DataMiner.LOGGER.error("Failed to load DataMiner config", e);
		}
	}

	public static void save() {
		try {
			ConfigData data = new ConfigData();
			data.dumpOnStartup = DUMP_ON_STARTUP;
			data.dumpOutputDir = DUMP_OUTPUT_DIR;
			data.dumpBlocks = DUMP_BLOCKS;
			data.dumpItems = DUMP_ITEMS;
			data.dumpEntities = DUMP_ENTITIES;
			data.dumpBiomes = DUMP_BIOMES;
			data.dumpEnchantments = DUMP_ENCHANTMENTS;
			data.dumpStatusEffects = DUMP_STATUS_EFFECTS;
			data.dumpSoundEvents = DUMP_SOUND_EVENTS;
			data.dumpCreativeTabs = DUMP_CREATIVE_TABS;
			data.dumpDimensions = DUMP_DIMENSIONS;
			data.dumpPotions = DUMP_POTIONS;
			data.dumpVillagerProfessions = DUMP_VILLAGER_PROFESSIONS;
			data.dumpAttributes = DUMP_ATTRIBUTES;
			data.latencyAlwaysOn = LATENCY_ALWAYS_ON;

			Files.createDirectories(CONFIG_PATH.getParent());
			Files.writeString(CONFIG_PATH, GSON.toJson(data));
		} catch (IOException e) {
			DataMiner.LOGGER.error("Failed to save DataMiner config", e);
		}
	}

	private static class ConfigData {
		boolean dumpOnStartup = false;
		String dumpOutputDir = "dataminer_dumps";
		boolean dumpBlocks = true;
		boolean dumpItems = true;
		boolean dumpEntities = true;
		boolean dumpBiomes = true;
		boolean dumpEnchantments = true;
		boolean dumpStatusEffects = true;
		boolean dumpSoundEvents = true;
		boolean dumpCreativeTabs = true;
		boolean dumpDimensions = true;
		boolean dumpPotions = true;
		boolean dumpVillagerProfessions = true;
		boolean dumpAttributes = true;
		boolean latencyAlwaysOn = true;
	}
}
