package com.skd.data_miner.logs.issues;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skd.data_miner.init.Initializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssueRegistry {

    private static final Gson GSON_COMPACT = new GsonBuilder().disableHtmlEscaping().create();
    private static final List<IssueDetector> DETECTORS = new ArrayList<>();

    private record PendingWrite(Path file, String line) {}

    private static final BlockingQueue<PendingWrite> WRITE_QUEUE = new ArrayBlockingQueue<>(4096);
    private static final AtomicBoolean WRITER_STARTED = new AtomicBoolean(false);

    static {
        DETECTORS.add(new IssueDetector() {
            private final Pattern p = Pattern.compile(
                    "Refmap\\s+([\\w./]+)\\s+is\\s+missing|refmap\\s+([\\w./]+)\\s+not\\s+found|Mixin.*refmap.*missing",
                    Pattern.CASE_INSENSITIVE);

            @Override public Pattern pattern() { return p; }
            @Override public String type() { return "missing_refmap"; }

            @Override
            public JsonObject extract(String loggerName, String message, String thread) {
                JsonObject json = new JsonObject();
                json.addProperty("logger", loggerName);
                Matcher m = p.matcher(message);
                String refmap = m.find() ? (m.group(1) != null ? m.group(1) : m.group(2)) : "unknown";
                json.addProperty("refmap", refmap);
                json.addProperty("message", message.length() > 200 ? message.substring(0, 200) + "..." : message);
                json.addProperty("thread", thread);
                json.addProperty("timestamp", Instant.now().toString());
                return json;
            }
        });

        DETECTORS.add(new IssueDetector() {
            private final Pattern p = Pattern.compile(
                    "access\\s+transformer|AccessTransformer|accesstransformer|could\\s+not\\s+apply\\s+transformer",
                    Pattern.CASE_INSENSITIVE);

            @Override public Pattern pattern() { return p; }
            @Override public String type() { return "access_transformer"; }

            @Override
            public JsonObject extract(String loggerName, String message, String thread) {
                JsonObject json = new JsonObject();
                json.addProperty("logger", loggerName);
                json.addProperty("message", message.length() > 300 ? message.substring(0, 300) + "..." : message);
                json.addProperty("thread", thread);
                json.addProperty("timestamp", Instant.now().toString());
                return json;
            }
        });

        DETECTORS.add(new IssueDetector() {
            private final Pattern p = Pattern.compile(
                    "missing\\s+texture|texture\\s+.*not\\s+found|failed\\s+to\\s+load\\s+texture|unable\\s+to\\s+load\\s+texture",
                    Pattern.CASE_INSENSITIVE);

            @Override public Pattern pattern() { return p; }
            @Override public String type() { return "missing_texture"; }

            @Override
            public JsonObject extract(String loggerName, String message, String thread) {
                JsonObject json = new JsonObject();
                json.addProperty("logger", loggerName);
                json.addProperty("message", message.length() > 300 ? message.substring(0, 300) + "..." : message);
                json.addProperty("thread", thread);
                json.addProperty("timestamp", Instant.now().toString());
                return json;
            }
        });

        DETECTORS.add(new IssueDetector() {
            private final Pattern p = Pattern.compile(
                    "pack\\.meta|pack\\s+metadata|invalid\\s+pack|resourcepack.*error|datapack.*error",
                    Pattern.CASE_INSENSITIVE);

            @Override public Pattern pattern() { return p; }
            @Override public String type() { return "pack_metadata"; }

            @Override
            public JsonObject extract(String loggerName, String message, String thread) {
                JsonObject json = new JsonObject();
                json.addProperty("logger", loggerName);
                json.addProperty("message", message.length() > 300 ? message.substring(0, 300) + "..." : message);
                json.addProperty("thread", thread);
                json.addProperty("timestamp", Instant.now().toString());
                return json;
            }
        });

        DETECTORS.add(new IssueDetector() {
            private final Pattern p = Pattern.compile(
                    "Error con mod \\[(\\w+)\\].*loot table for this container \\[ResourceKey\\[minecraft:loot_table / ([^\\]]+)\\]\\] does not exist",
                    Pattern.CASE_INSENSITIVE);

            @Override public Pattern pattern() { return p; }
            @Override public String type() { return "lootr_missing_loot_table"; }

            @Override
            public JsonObject extract(String loggerName, String message, String thread) {
                JsonObject json = new JsonObject();
                json.addProperty("logger", loggerName);
                Matcher m = p.matcher(message);
                if (m.find()) {
                    json.addProperty("mod_id", m.group(1));
                    json.addProperty("loot_table", m.group(2));
                }
                json.addProperty("message", message);
                json.addProperty("thread", thread);
                json.addProperty("timestamp", Instant.now().toString());
                return json;
            }
        });
    }

    public static String check(String loggerName, String message, String thread) {
        for (IssueDetector d : DETECTORS) {
            if (d.pattern().matcher(message).find()) {
                JsonObject json = d.extract(loggerName, message, thread);
                saveIssue(d.type(), json);
                return "§c[" + d.type() + "]§r ";
            }
        }
        return "";
    }

    private static void saveIssue(String type, JsonObject json) {
        if (Initializer.baseDir == null) return;
        json.addProperty("type", type);
        enqueue(Initializer.baseDir.resolve("startup/logs/issues.jsonl"),
                GSON_COMPACT.toJson(json) + System.lineSeparator());

        if ("lootr_missing_loot_table".equals(type)
                && json.has("mod_id") && json.has("loot_table") && json.has("timestamp")) {
            JsonObject minimal = new JsonObject();
            minimal.addProperty("mod_id", json.get("mod_id").getAsString());
            minimal.addProperty("loot_table", json.get("loot_table").getAsString());
            minimal.addProperty("timestamp", json.get("timestamp").getAsString());
            enqueue(Initializer.baseDir.resolve("startup/logs/lootr_missing_tables.jsonl"),
                    GSON_COMPACT.toJson(minimal) + System.lineSeparator());
        }
    }

    private static void enqueue(Path file, String line) {
        ensureWriter();
        WRITE_QUEUE.offer(new PendingWrite(file, line));
    }

    private static void ensureWriter() {
        if (WRITER_STARTED.compareAndSet(false, true)) {
            Thread t = new Thread(IssueRegistry::drainLoop, "DataMiner-IssueWriter");
            t.setDaemon(true);
            t.start();
        }
    }

    private static void drainLoop() {
        List<PendingWrite> batch = new ArrayList<>(256);
        while (true) {
            try {
                PendingWrite first = WRITE_QUEUE.poll(500, TimeUnit.MILLISECONDS);
                if (first == null) continue;
                batch.clear();
                batch.add(first);
                WRITE_QUEUE.drainTo(batch, 255);
                for (PendingWrite pw : batch) {
                    try {
                        Files.createDirectories(pw.file().getParent());
                        Files.writeString(pw.file(), pw.line(),
                                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        System.err.println("[DataMiner] Failed to save issue report: " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
