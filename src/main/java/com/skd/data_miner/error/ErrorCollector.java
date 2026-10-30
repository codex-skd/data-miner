package com.skd.data_miner.error;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skd.data_miner.DataMiner;
import com.skd.data_miner.init.Initializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ErrorCollector {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            .withZone(ZoneId.systemDefault());

    public static void register() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            DataMiner.LOGGER.error("Uncaught exception in thread {}", thread.getName(), throwable);
            saveError(throwable, "startup");
        });
    }

    public static void saveError(Throwable throwable, String phase) {
        if (Initializer.baseDir == null) return;

        try {
            String timestamp = DATE_FMT.format(Instant.now());
            Path errorPath = Initializer.baseDir.resolve(phase + "/errors/" + timestamp + ".json");

            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));

            JsonObject root = new JsonObject();
            root.addProperty("timestamp", timestamp);
            root.addProperty("phase", phase);
            root.addProperty("type", throwable.getClass().getName());
            root.addProperty("message", throwable.getMessage());
            root.addProperty("thread", Thread.currentThread().getName());
            root.addProperty("stacktrace", sw.toString());

            JsonObject cause = new JsonObject();
            Throwable t = throwable.getCause();
            int depth = 0;
            while (t != null && depth < 10) {
                StringWriter csw = new StringWriter();
                t.printStackTrace(new PrintWriter(csw));
                cause.addProperty("cause_" + depth, t.getClass().getName() + ": " + t.getMessage() + "\n" + csw);
                t = t.getCause();
                depth++;
            }
            if (depth > 0) root.add("causes", cause);

            Files.createDirectories(errorPath.getParent());
            Files.writeString(errorPath, GSON.toJson(root));
            DataMiner.LOGGER.info("Error saved to {}", errorPath);
        } catch (IOException e) {
            DataMiner.LOGGER.error("Failed to save error report", e);
        }
    }
}
