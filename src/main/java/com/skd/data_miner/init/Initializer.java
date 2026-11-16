package com.skd.data_miner.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.skd.data_miner.DataMiner;
import com.skd.data_miner.logs.LogRedirector;
import com.skd.data_miner.modimpact.ModAnalyzer;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.SharedConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Initializer {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
			.withZone(ZoneId.systemDefault());

	public static Path baseDir;

	public static void init() {
		boolean isClient = isClientSide();
		String folderName = isClient ? "data_miner/info_client" : "data_miner/info_server";

		baseDir = Path.of(folderName);

		try {
			Files.createDirectories(baseDir.resolve("startup/registries"));
			Files.createDirectories(baseDir.resolve("startup/errors"));
			Files.createDirectories(baseDir.resolve("performance"));
			Files.createDirectories(baseDir.resolve("performance/latency"));
			Files.createDirectories(baseDir.resolve("events/errors"));
			Files.createDirectories(baseDir.resolve("logs"));

			generateModsJson();
			generateInfoJson();
			ModAnalyzer.generateRegistryImpact();
			ModAnalyzer.generateModAnalysis();

			LogRedirector.init(baseDir);

			DataMiner.LOGGER.info("DataMiner folder structure created at {}", baseDir.toAbsolutePath());
		} catch (IOException e) {
			DataMiner.LOGGER.error("Failed to create folder structure", e);
		}
	}

	private static void generateModsJson() throws IOException {
		JsonObject root = new JsonObject();
		root.addProperty("total_mods", FabricLoader.getInstance().getAllMods().size());

		JsonArray mods = new JsonArray();
		for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
			ModMetadata metadata = container.getMetadata();
			JsonObject m = new JsonObject();
			m.addProperty("mod_id", metadata.getId());
			m.addProperty("display_name", metadata.getName());
			m.addProperty("version", metadata.getVersion().getFriendlyString());
			m.addProperty("namespace", metadata.getId());
			JsonArray deps = new JsonArray();
			for (ModDependency dep : metadata.getDependencies()) {
				deps.add(dep.getModId() + ":" + dep.getVersionRequirements());
			}
			m.add("dependencies", deps);
			mods.add(m);
		}
		root.add("mods", mods);

		Files.writeString(baseDir.resolve("startup/mods.json"), GSON.toJson(root));
		DataMiner.LOGGER.info("Generated mods.json with {} mods", FabricLoader.getInstance().getAllMods().size());
	}

	private static void generateInfoJson() throws IOException {
		JsonObject root = new JsonObject();
		root.addProperty("generated_at", DATE_FMT.format(Instant.now()));
		root.addProperty("minecraft_version", SharedConstants.getCurrentVersion().name());
		root.addProperty("side", isClientSide() ? "client" : "server");
		root.addProperty("java_version", System.getProperty("java.version"));
		root.addProperty("java_vendor", System.getProperty("java.vendor"));
		root.addProperty("os_name", System.getProperty("os.name"));
		root.addProperty("os_version", System.getProperty("os.version"));
		root.addProperty("os_arch", System.getProperty("os.arch"));
		root.addProperty("available_processors", Runtime.getRuntime().availableProcessors());
		root.addProperty("max_memory_mb", Runtime.getRuntime().maxMemory() / 1024 / 1024);
		root.addProperty("locale", Locale.getDefault().toString());

		Files.writeString(baseDir.resolve("startup/info.json"), GSON.toJson(root));
		DataMiner.LOGGER.info("Generated info.json");
	}

	private static boolean isClientSide() {
		return FabricLoader.getInstance().getEnvironmentType() == net.fabricmc.api.EnvType.CLIENT;
	}
}
