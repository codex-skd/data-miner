package com.skd.data_miner.latency;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.skd.data_miner.DataMiner;
import com.skd.data_miner.init.Initializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LatencyTracer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            .withZone(ZoneId.systemDefault());

    private static boolean running = false;
    private static long startTime;
    private static final List<JsonObject> samples = Collections.synchronizedList(new ArrayList<>());

    private static double eatMin = Double.MAX_VALUE, eatMax = 0, eatSum = 0;
    private static int eatCount = 0;

    private static double breakMin = Double.MAX_VALUE, breakMax = 0, breakSum = 0;
    private static int breakCount = 0;

    private static double tickMin = Double.MAX_VALUE, tickMax = 0, tickSum = 0;
    private static int tickSlowCount = 0;
    private static final double SLOW_TICK_THRESHOLD = 50.0;

    private static long tickStartNano;

    public static void start() {
        if (running) return;
        samples.clear();
        resetStats();
        running = true;
        startTime = System.currentTimeMillis();
        DataMiner.LOGGER.info("Latency tracer started");
    }

    public static void ensureRunning() {
        if (!running) start();
    }

    public static void stop() {
        running = false;
        DataMiner.LOGGER.info("Latency tracer stopped. Eats:{} Breaks:{} SlowTicks:{}",
                eatCount, breakCount, tickSlowCount);
    }

    public static boolean isRunning() {
        return running;
    }

    public static String getStats() {
        long now = System.currentTimeMillis();
        long durationSec = (now - startTime) / 1000;
        StringBuilder sb = new StringBuilder();
        sb.append("§6=== Latency Stats ===§r\n");
        sb.append("§7Duration: §f").append(formatDuration(durationSec)).append("§r\n");
        sb.append("§7Eating: §f").append(eatCount).append(" samples")
                .append(eatCount > 0 ? String.format(" §7min/avg/max: §f%.1f/%.1f/%.1f ms", eatMin, eatSum / eatCount, eatMax) : "")
                .append("§r\n");
        sb.append("§7Block Break: §f").append(breakCount).append(" samples")
                .append(breakCount > 0 ? String.format(" §7min/avg/max: §f%.1f/%.1f/%.1f ms", breakMin, breakSum / breakCount, breakMax) : "")
                .append("§r\n");
        sb.append("§7Slow Ticks: §f").append(tickSlowCount).append(" ticks (>50ms)")
                .append(tickSlowCount > 0 ? String.format(" §7min/avg/max: §f%.1f/%.1f/%.1f mspt", tickMin, tickSum / tickSlowCount, tickMax) : "")
                .append("§r\n");
        return sb.toString();
    }

    private static String formatDuration(long secs) {
        long h = secs / 3600;
        long m = (secs % 3600) / 60;
        long s = secs % 60;
        if (h > 0) return String.format("%dh %dm %ds", h, m, s);
        if (m > 0) return String.format("%dm %ds", m, s);
        return String.format("%ds", s);
    }

    public static void recordEat(String item, double ms) {
        if (!running) return;
        eatCount++;
        eatSum += ms;
        if (ms < eatMin) eatMin = ms;
        if (ms > eatMax) eatMax = ms;

        JsonObject s = new JsonObject();
        s.addProperty("type", "eat");
        s.addProperty("item", item);
        s.addProperty("latency_ms", ms);
        s.addProperty("timestamp", System.currentTimeMillis());
        samples.add(s);
    }

    public static void recordBlockBreak(String block, double ms) {
        if (!running) return;
        breakCount++;
        breakSum += ms;
        if (ms < breakMin) breakMin = ms;
        if (ms > breakMax) breakMax = ms;

        JsonObject s = new JsonObject();
        s.addProperty("type", "block_break");
        s.addProperty("block", block);
        s.addProperty("latency_ms", ms);
        s.addProperty("timestamp", System.currentTimeMillis());
        samples.add(s);
    }

    public static void recordSlowTick(double mspt, int loadedEntities, int loadedChunks, JsonObject modContext) {
        if (!running) return;
        if (mspt < SLOW_TICK_THRESHOLD) return;
        tickSlowCount++;
        tickSum += mspt;
        if (mspt < tickMin) tickMin = mspt;
        if (mspt > tickMax) tickMax = mspt;

        JsonObject s = new JsonObject();
        s.addProperty("type", "slow_tick");
        s.addProperty("mspt", mspt);
        s.addProperty("loaded_entities", loadedEntities);
        s.addProperty("loaded_chunks", loadedChunks);
        s.addProperty("timestamp", System.currentTimeMillis());
        if (modContext != null) s.add("entities_by_mod", modContext);
        samples.add(s);
    }

    public static Path saveReport() throws IOException {
        JsonObject report = new JsonObject();
        report.addProperty("tracer_version", "0.7.0");
        report.addProperty("start_time", startTime);
        report.addProperty("end_time", System.currentTimeMillis());
        report.addProperty("duration_ms", System.currentTimeMillis() - startTime);

        if (eatCount > 0) {
            JsonObject eatObj = new JsonObject();
            eatObj.addProperty("min_ms", eatMin);
            eatObj.addProperty("max_ms", eatMax);
            eatObj.addProperty("avg_ms", eatSum / eatCount);
            eatObj.addProperty("count", eatCount);
            report.add("eating", eatObj);
        }

        if (breakCount > 0) {
            JsonObject breakObj = new JsonObject();
            breakObj.addProperty("min_ms", breakMin);
            breakObj.addProperty("max_ms", breakMax);
            breakObj.addProperty("avg_ms", breakSum / breakCount);
            breakObj.addProperty("count", breakCount);
            report.add("block_breaking", breakObj);
        }

        if (tickSlowCount > 0) {
            JsonObject tickObj = new JsonObject();
            tickObj.addProperty("min_mspt", tickMin);
            tickObj.addProperty("max_mspt", tickMax);
            tickObj.addProperty("avg_mspt", tickSum / tickSlowCount);
            tickObj.addProperty("slow_ticks", tickSlowCount);
            tickObj.addProperty("threshold_mspt", SLOW_TICK_THRESHOLD);
            report.add("slow_ticks", tickObj);
        }

        JsonArray sampleArray = new JsonArray();
        samples.forEach(sampleArray::add);
        report.add("samples", sampleArray);

        Path outDir = Initializer.baseDir.resolve("performance/latency");
        Files.createDirectories(outDir);
        String ts = DATE_FMT.format(Instant.now());
        Path reportPath = outDir.resolve(ts + "_latency.json");
        Files.writeString(reportPath, GSON.toJson(report));
        DataMiner.LOGGER.info("Latency report saved to {}", reportPath);
        return reportPath;
    }

    private static void resetStats() {
        eatMin = Double.MAX_VALUE; eatMax = 0; eatSum = 0; eatCount = 0;
        breakMin = Double.MAX_VALUE; breakMax = 0; breakSum = 0; breakCount = 0;
        tickMin = Double.MAX_VALUE; tickMax = 0; tickSum = 0; tickSlowCount = 0;
    }
}
