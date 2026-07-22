package com.skd.data_miner.init;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skd.data_miner.DataMiner;
import com.skd.data_miner.logs.LogRedirector;
import com.skd.data_miner.modimpact.ModAnalyzer;
import net.minecraft.SharedConstants;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;

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
        String folderName = isClient ? "info_client_data_miner" : "info_server_data_miner";

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
        root.addProperty("total_mods", ModList.get().size());

        JsonArray mods = new JsonArray();
        for (IModInfo mod : ModList.get().getMods()) {
            JsonObject m = new JsonObject();
            m.addProperty("mod_id", mod.getModId());
            m.addProperty("display_name", mod.getDisplayName());
            m.addProperty("version", mod.getVersion().toString());
            m.addProperty("namespace", mod.getNamespace());
            JsonArray deps = new JsonArray();
            mod.getDependencies().forEach(d -> deps.add(d.getModId() + ":" + d.getVersionRange()));
            m.add("dependencies", deps);
            mods.add(m);
        }
        root.add("mods", mods);

        Files.writeString(baseDir.resolve("startup/mods.json"), GSON.toJson(root));
        DataMiner.LOGGER.info("Generated mods.json with {} mods", ModList.get().size());
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
        try {
            Class.forName("net.minecraft.client.Minecraft");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
