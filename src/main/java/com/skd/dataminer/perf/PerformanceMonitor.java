package com.skd.dataminer.perf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skd.dataminer.DataMiner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PerformanceMonitor {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static boolean running = false;
    private static long startTime;
    private static long endTime;

    private static int fpsMin = Integer.MAX_VALUE;
    private static int fpsMax = Integer.MIN_VALUE;
    private static long fpsSum = 0;
    private static int fpsSamples = 0;

    private static double msptMin = Double.MAX_VALUE;
    private static double msptMax = Double.MIN_VALUE;
    private static double msptSum = 0;
    private static int msptSamples = 0;

    public static void start() {
        resetData();
        running = true;
        startTime = System.currentTimeMillis();
        DataMiner.LOGGER.info("Performance monitor started");
    }

    public static void stop() {
        running = false;
        endTime = System.currentTimeMillis();
        DataMiner.LOGGER.info("Performance monitor stopped after {}ms", endTime - startTime);
    }

    public static boolean isRunning() {
        return running;
    }

    public static void recordFrame(int fps) {
        if (!running) return;
        if (fps < fpsMin) fpsMin = fps;
        if (fps > fpsMax) fpsMax = fps;
        fpsSum += fps;
        fpsSamples++;
    }

    public static void recordTick(double mspt) {
        if (!running) return;
        if (mspt < msptMin) msptMin = mspt;
        if (mspt > msptMax) msptMax = mspt;
        msptSum += mspt;
        msptSamples++;
    }

    public static Path saveReport() throws IOException {
        JsonObject report = new JsonObject();

        report.addProperty("monitor_version", "0.3.0");
        report.addProperty("start_time", startTime);
        report.addProperty("end_time", endTime);
        report.addProperty("duration_ms", endTime - startTime);
        report.addProperty("duration_seconds", (endTime - startTime) / 1000.0);

        if (fpsSamples > 0) {
            JsonObject fpsObj = new JsonObject();
            fpsObj.addProperty("min", fpsMin);
            fpsObj.addProperty("max", fpsMax);
            fpsObj.addProperty("avg", (double) fpsSum / fpsSamples);
            fpsObj.addProperty("samples", fpsSamples);
            report.add("fps", fpsObj);
        }

        if (msptSamples > 0) {
            JsonObject msptObj = new JsonObject();
            msptObj.addProperty("min", msptMin);
            msptObj.addProperty("max", msptMax);
            msptObj.addProperty("avg", msptSum / msptSamples);
            msptObj.addProperty("samples", msptSamples);
            report.add("mspt", msptObj);
        }

        Path outDir = com.skd.dataminer.init.Initializer.baseDir.resolve("performance");
        Files.createDirectories(outDir);
        String ts = java.time.Instant.now().atZone(java.time.ZoneId.systemDefault())
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path reportPath = outDir.resolve(ts + ".json");
        Files.writeString(reportPath, GSON.toJson(report));
        DataMiner.LOGGER.info("Performance report saved to {}", reportPath);
        return reportPath;
    }

    private static void resetData() {
        fpsMin = Integer.MAX_VALUE;
        fpsMax = Integer.MIN_VALUE;
        fpsSum = 0;
        fpsSamples = 0;

        msptMin = Double.MAX_VALUE;
        msptMax = Double.MIN_VALUE;
        msptSum = 0;
        msptSamples = 0;

        startTime = 0;
        endTime = 0;
    }
}
