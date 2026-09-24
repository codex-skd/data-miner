package com.skd.data_miner.modimpact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.skd.data_miner.DataMiner;
import com.skd.data_miner.init.Initializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModAnalyzer {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public static void generateRegistryImpact() {
		DataMiner.LOGGER.info("Analyzing mod registry impact...");

		Map<String, ModStats> stats = new HashMap<>();
		for (Block block : BuiltInRegistries.BLOCK) {
			String ns = BuiltInRegistries.BLOCK.getKey(block).getNamespace();
			stats.computeIfAbsent(ns, ModStats::new).blocks++;
		}
		for (Item item : BuiltInRegistries.ITEM) {
			String ns = BuiltInRegistries.ITEM.getKey(item).getNamespace();
			stats.computeIfAbsent(ns, ModStats::new).items++;
		}
		for (EntityType<?> et : BuiltInRegistries.ENTITY_TYPE) {
			String ns = BuiltInRegistries.ENTITY_TYPE.getKey(et).getNamespace();
			stats.computeIfAbsent(ns, ModStats::new).entities++;
		}

		JsonObject root = new JsonObject();
		root.addProperty("analyzer_version", "0.7.2");
		JsonArray mods = new JsonArray();
		stats.entrySet().stream()
				.sorted((a, b) -> b.getValue().total() - a.getValue().total())
				.forEach(e -> {
					JsonObject m = new JsonObject();
					m.addProperty("namespace", e.getKey());
					ModStats s = e.getValue();
					m.addProperty("blocks", s.blocks);
					m.addProperty("items", s.items);
					m.addProperty("entity_types", s.entities);
					m.addProperty("total_entries", s.total());
					mods.add(m);
				});
		root.add("mods", mods);

		try {
			Path outPath = Initializer.baseDir.resolve("startup/mod_impact.json");
			Files.writeString(outPath, GSON.toJson(root));
			DataMiner.LOGGER.info("Mod impact analysis saved to {}", outPath);
		} catch (IOException e) {
			DataMiner.LOGGER.error("Failed to write mod impact analysis", e);
		}
	}

	public static void generateModAnalysis() {
		DataMiner.LOGGER.info("Generating full mod analysis...");

		Map<String, ModStats> stats = new HashMap<>();
		for (Block block : BuiltInRegistries.BLOCK) {
			String ns = BuiltInRegistries.BLOCK.getKey(block).getNamespace();
			stats.computeIfAbsent(ns, ModStats::new).blocks++;
		}
		for (Item item : BuiltInRegistries.ITEM) {
			String ns = BuiltInRegistries.ITEM.getKey(item).getNamespace();
			stats.computeIfAbsent(ns, ModStats::new).items++;
		}
		for (EntityType<?> et : BuiltInRegistries.ENTITY_TYPE) {
			String ns = BuiltInRegistries.ENTITY_TYPE.getKey(et).getNamespace();
			stats.computeIfAbsent(ns, ModStats::new).entities++;
		}

		Set<String> allModIds = new HashSet<>();
		for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
			allModIds.add(mod.getMetadata().getId());
		}

		JsonObject root = new JsonObject();
		root.addProperty("analyzer_version", "0.7.2");
		root.addProperty("total_mods_loaded", FabricLoader.getInstance().getAllMods().size());

		JsonArray modList = new JsonArray();
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			ModMetadata metadata = container.getMetadata();
			JsonObject m = new JsonObject();
			m.addProperty("mod_id", metadata.getId());
			m.addProperty("display_name", metadata.getName());
			m.addProperty("version", metadata.getVersion().getFriendlyString());
			m.addProperty("namespace", metadata.getId());

			ModStats s = stats.getOrDefault(metadata.getId(), new ModStats(metadata.getId()));
			m.addProperty("blocks_registered", s.blocks);
			m.addProperty("items_registered", s.items);
			m.addProperty("entity_types_registered", s.entities);
			m.addProperty("total_registered", s.total());

			JsonArray deps = new JsonArray();
			List<String> issues = new ArrayList<>();
			for (ModDependency dep : metadata.getDependencies()) {
				JsonObject d = new JsonObject();
				d.addProperty("mod_id", dep.getModId());
				d.addProperty("type", dep.getKind().name());
				d.addProperty("version_range", dep.getVersionRequirements().toString());
				deps.add(d);

				if (dep.getKind() == ModDependency.Kind.DEPENDS && !allModIds.contains(dep.getModId())) {
					issues.add("Missing dependency: " + dep.getModId());
				}
			}
			m.add("dependencies", deps);

			if (s.entities > 100)
				issues.add("High entity type count (>100): may contribute to mob lag");
			if (s.blocks > 500)
				issues.add("High block count (>500): may increase RAM usage");
			if (s.items > 1000)
				issues.add("High item count (>1000): may impact inventory/creative tab");

			if (!issues.isEmpty()) {
				JsonArray iss = new JsonArray();
				issues.forEach(iss::add);
				m.add("potential_issues", iss);
			}

			modList.add(m);
		}
		root.add("mods", modList);

		try {
			Path outPath = Initializer.baseDir.resolve("startup/mod_analysis.json");
			Files.writeString(outPath, GSON.toJson(root));
			DataMiner.LOGGER.info("Full mod analysis saved to {}", outPath);
		} catch (IOException e) {
			DataMiner.LOGGER.error("Failed to write mod analysis", e);
		}
	}

	public static JsonObject snapshotWorldContext(MinecraftServer server) {
		JsonObject ctx = new JsonObject();
		int totalEntities = 0, totalChunks = 0;
		Map<String, Integer> entityCounts = new HashMap<>();

		for (ServerLevel level : server.getAllLevels()) {
			for (Entity entity : level.getAllEntities()) {
				totalEntities++;
				String ns = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getNamespace();
				entityCounts.merge(ns, 1, Integer::sum);
			}
			totalChunks += level.getChunkSource().getLoadedChunksCount();
		}

		ctx.addProperty("total_entities", totalEntities);
		ctx.addProperty("total_chunks", totalChunks);

		JsonArray topEntities = new JsonArray();
		entityCounts.entrySet().stream()
				.sorted((a, b) -> b.getValue() - a.getValue())
				.limit(15)
				.forEach(e -> {
					JsonObject nsObj = new JsonObject();
					nsObj.addProperty("namespace", e.getKey());
					nsObj.addProperty("entities", e.getValue());
					topEntities.add(nsObj);
				});
		ctx.add("entities_by_namespace", topEntities);
		return ctx;
	}

	private static class ModStats {
		final String namespace;
		int blocks, items, entities;

		ModStats(String namespace) { this.namespace = namespace; }
		int total() { return blocks + items + entities; }
	}
}
