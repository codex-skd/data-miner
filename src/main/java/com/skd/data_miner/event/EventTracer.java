package com.skd.data_miner.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventTracer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            .withZone(ZoneId.systemDefault());

    private static boolean running = false;
    private static long startTime;
    private static final List<JsonObject> events = Collections.synchronizedList(new ArrayList<>());
    private static int eventCount = 0;
    private static int errorCount = 0;

    public static void start() {
        events.clear();
        eventCount = 0;
        errorCount = 0;
        running = true;
        startTime = System.currentTimeMillis();
        DataMiner.LOGGER.info("Event tracer started");
    }

    public static void stop() {
        running = false;
        DataMiner.LOGGER.info("Event tracer stopped. Collected {} events, {} errors", eventCount, errorCount);
    }

    public static boolean isRunning() {
        return running;
    }

    public static void record(JsonObject event) {
        if (!running) return;
        events.add(event);
        eventCount++;
    }

    public static void saveError(Throwable t, String eventType) {
        if (!running) return;
        errorCount++;
        try {
            String timestamp = DATE_FMT.format(Instant.now());
            Path errDir = Initializer.baseDir.resolve("events/errors");
            Files.createDirectories(errDir);

            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));

            JsonObject err = new JsonObject();
            err.addProperty("timestamp", timestamp);
            err.addProperty("event_type", eventType);
            err.addProperty("error", t.getClass().getName() + ": " + t.getMessage());
            err.addProperty("stacktrace", sw.toString());

            Path errFile = errDir.resolve(timestamp + "_" + eventType + ".json");
            Files.writeString(errFile, GSON.toJson(err));
        } catch (IOException ignored) {
        }
    }

    public static Path saveReport() throws IOException {
        JsonObject report = new JsonObject();
        report.addProperty("tracer_version", "0.4.0");
        report.addProperty("start_time", startTime);
        report.addProperty("end_time", System.currentTimeMillis());
        report.addProperty("duration_ms", System.currentTimeMillis() - startTime);
        report.addProperty("total_events", eventCount);
        report.addProperty("total_errors", errorCount);

        JsonArray eventArray = new JsonArray();
        events.forEach(eventArray::add);
        report.add("events", eventArray);

        Path outDir = Initializer.baseDir.resolve("events");
        Files.createDirectories(outDir);
        String ts = DATE_FMT.format(Instant.now());
        Path reportPath = outDir.resolve(ts + "_events.json");
        Files.writeString(reportPath, GSON.toJson(report));
        DataMiner.LOGGER.info("Event report saved to {} with {} events", reportPath, eventCount);
        return reportPath;
    }
}
