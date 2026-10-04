package com.skd.data_miner.logs.issues;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skd.data_miner.DataMiner;
import com.skd.data_miner.init.Initializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssueRegistry {

    private static final Gson GSON_COMPACT = new GsonBuilder().disableHtmlEscaping().create();
    private static final List<IssueDetector> DETECTORS = new ArrayList<>();

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

    private static synchronized void saveIssue(String type, JsonObject json) {
        if (Initializer.baseDir == null) return;
        json.addProperty("type", type);
        try {
            Path file = Initializer.baseDir.resolve("startup/logs/issues.jsonl");
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON_COMPACT.toJson(json) + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            DataMiner.LOGGER.error("Failed to save issue report", e);
        }
    }
}
