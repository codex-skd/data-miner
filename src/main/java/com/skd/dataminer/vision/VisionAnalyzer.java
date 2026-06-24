package com.skd.dataminer.vision;

import com.google.gson.*;
import com.skd.dataminer.DataMiner;
import com.skd.dataminer.DataMinerConfig;
import com.skd.dataminer.error.ErrorCollector;
import com.skd.dataminer.init.Initializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VisionAnalyzer {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            .withZone(ZoneId.systemDefault());
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private static boolean running;
    private static ScheduledExecutorService scheduler;
    private static final List<AnalysisResult> results = Collections.synchronizedList(new ArrayList<>());
    private static Instant startTime;
    private static Path screenshotsDir;
    private static Path analysesDir;

    private static boolean aiEnabled;
    private static String apiType;
    private static String apiEndpoint;
    private static String apiKey;
    private static String model;
    private static String systemPrompt;

    public static void analyze() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getMainRenderTarget() == null) {
            DataMiner.LOGGER.warn("VisionAnalyzer: not on a render-capable side");
            return;
        }

        mc.execute(() -> {
            try {
                captureAndSend();
            } catch (Exception e) {
                DataMiner.LOGGER.error("VisionAnalyzer: capture failed", e);
            }
        });
    }

    public static void start() {
        if (running) return;

        loadConfig();

        screenshotsDir = Initializer.baseDir.resolve("vision/screenshots");
        analysesDir = Initializer.baseDir.resolve("vision/analyses");

        try {
            Files.createDirectories(screenshotsDir);
            Files.createDirectories(analysesDir);
        } catch (IOException e) {
            DataMiner.LOGGER.error("VisionAnalyzer: failed to create output dirs", e);
        }

        results.clear();
        startTime = Instant.now();
        running = true;

        int interval = DataMinerConfig.VISION_CAPTURE_INTERVAL.get();
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "dataminer-vision");
            t.setDaemon(true);
            return t;
        });

        scheduler.scheduleAtFixedRate(() -> {
            if (!running) return;
            analyze();
        }, 0, interval, TimeUnit.SECONDS);

        DataMiner.LOGGER.info("VisionAnalyzer started — capturing every {}s", interval);
    }

    public static void stop() {
        if (!running) return;
        running = false;

        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }

        try {
            saveReport();
        } catch (IOException e) {
            DataMiner.LOGGER.error("VisionAnalyzer: failed to save report", e);
        }

        DataMiner.LOGGER.info("VisionAnalyzer stopped — {} captures", results.size());
    }

    public static boolean isRunning() {
        return running;
    }

    public static Path saveReport() throws IOException {
        Instant endTime = Instant.now();
        long durationMs = Duration.between(startTime, endTime).toMillis();

        JsonObject report = new JsonObject();
        report.addProperty("analyzer_version", "0.1.0");
        report.addProperty("start_time", DATE_FMT.format(startTime));
        report.addProperty("end_time", DATE_FMT.format(endTime));
        report.addProperty("duration_ms", durationMs);
        report.addProperty("total_captures", results.size());
        report.addProperty("ai_enabled", aiEnabled);
        if (aiEnabled) {
            report.addProperty("ai_type", apiType);
            report.addProperty("ai_model", model);
        }

        JsonArray entries = new JsonArray();
        for (AnalysisResult r : results) {
            entries.add(r.toJson());
        }
        report.add("captures", entries);

        Path reportPath = analysesDir.resolve(DATE_FMT.format(endTime) + "_vision_report.json");
        Files.writeString(reportPath, GSON.toJson(report));
        results.clear();
        return reportPath;
    }

    private static void loadConfig() {
        apiEndpoint = DataMinerConfig.VISION_API_ENDPOINT.get().trim();
        aiEnabled = !apiEndpoint.isEmpty();
        if (aiEnabled) {
            apiType = DataMinerConfig.VISION_API_TYPE.get().trim().toLowerCase();
            apiKey = DataMinerConfig.VISION_API_KEY.get();
            model = DataMinerConfig.VISION_MODEL.get();
            systemPrompt = DataMinerConfig.VISION_SYSTEM_PROMPT.get();
        }
    }

    private static void captureAndSend() {
        if (!running && results.isEmpty()) {
            loadConfig();
            screenshotsDir = Initializer.baseDir.resolve("vision/screenshots");
            analysesDir = Initializer.baseDir.resolve("vision/analyses");
        }

        String timestamp = DATE_FMT.format(Instant.now());
        String filename = timestamp + ".png";

        Path screenshotPath;
        try {
            Files.createDirectories(screenshotsDir);
            screenshotPath = ScreenCapture.capture(screenshotsDir, filename);
        } catch (IOException e) {
            DataMiner.LOGGER.error("VisionAnalyzer: screenshot failed", e);
            return;
        }

        AnalysisResult result = new AnalysisResult();
        result.timestamp = timestamp;
        result.screenshotPath = screenshotPath.toString();
        capturePlayerInfo(result);

        if (aiEnabled) {
            try {
                String analysis = callAiApi(screenshotPath);
                result.aiAnalysis = analysis;
                result.aiModel = model;
            } catch (Exception e) {
                result.aiError = e.getMessage();
                DataMiner.LOGGER.error("VisionAnalyzer: AI analysis failed", e);
            }
        }

        if (!aiEnabled) {
            result.aiAnalysis = "AI analysis disabled — no API endpoint configured.";
        }

        saveAnalysisResult(result);
        results.add(result);
    }

    private static void capturePlayerInfo(AnalysisResult result) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            result.playerDimension = player.level().dimension().toString();
            result.playerX = player.getX();
            result.playerY = player.getY();
            result.playerZ = player.getZ();
            result.playerHealth = player.getHealth();
            result.playerFood = player.getFoodData().getFoodLevel();
        }
        result.fps = mc.getFps();
    }

    private static String callAiApi(Path screenshotPath) throws IOException, InterruptedException {
        if ("gemini".equals(apiType)) {
            return callGeminiApi(screenshotPath);
        }
        return callOpenAiApi(screenshotPath);
    }

    private static String callOpenAiApi(Path screenshotPath) throws IOException, InterruptedException {
        byte[] imageBytes = Files.readAllBytes(screenshotPath);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);
        requestBody.addProperty("max_tokens", 1024);

        JsonArray messages = new JsonArray();

        if (!systemPrompt.isEmpty()) {
            JsonObject sysMsg = new JsonObject();
            sysMsg.addProperty("role", "system");
            sysMsg.addProperty("content", systemPrompt);
            messages.add(sysMsg);
        }

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");

        JsonArray userContent = new JsonArray();
        JsonObject textPart = new JsonObject();
        textPart.addProperty("type", "text");
        textPart.addProperty("text", "Analyze this Minecraft screenshot. Describe what you see on screen, any visible UI elements, and note any visual glitches, rendering errors, texture issues, or anything that looks wrong or out of place. Be specific about coordinates, entities, and blocks visible.");
        userContent.add(textPart);

        JsonObject imagePart = new JsonObject();
        imagePart.addProperty("type", "image_url");
        JsonObject imageUrlObj = new JsonObject();
        imageUrlObj.addProperty("url", "data:image/png;base64," + base64Image);
        imagePart.add("image_url", imageUrlObj);
        userContent.add(imagePart);

        userMsg.add("content", userContent);
        messages.add(userMsg);

        requestBody.add("messages", messages);

        return sendHttpRequest(requestBody);
    }

    private static String callGeminiApi(Path screenshotPath) throws IOException, InterruptedException {
        byte[] imageBytes = Files.readAllBytes(screenshotPath);
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        JsonObject requestBody = new JsonObject();

        if (!systemPrompt.isEmpty()) {
            JsonObject sysInstruction = new JsonObject();
            JsonObject sysParts = new JsonObject();
            sysParts.addProperty("text", systemPrompt);
            sysInstruction.add("parts", sysParts);
            requestBody.add("systemInstruction", sysInstruction);
        }

        JsonArray contents = new JsonArray();
        JsonObject userContent = new JsonObject();
        userContent.addProperty("role", "user");

        JsonArray parts = new JsonArray();

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", "Analyze this Minecraft screenshot. Describe what you see on screen, any visible UI elements, and note any visual glitches, rendering errors, texture issues, or anything that looks wrong or out of place. Be specific about coordinates, entities, and blocks visible.");
        parts.add(textPart);

        JsonObject imagePart = new JsonObject();
        JsonObject inlineData = new JsonObject();
        inlineData.addProperty("mimeType", "image/png");
        inlineData.addProperty("data", base64Image);
        imagePart.add("inlineData", inlineData);
        parts.add(imagePart);

        userContent.add("parts", parts);
        contents.add(userContent);
        requestBody.add("contents", contents);

        return sendHttpRequest(requestBody);
    }

    private static String sendHttpRequest(JsonObject requestBody) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpoint))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestBody)));

        if (!apiKey.isEmpty()) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API returned status " + response.statusCode() + ": " + response.body());
        }

        return parseResponse(JsonParser.parseString(response.body()).getAsJsonObject());
    }

    private static String parseResponse(JsonObject responseJson) throws IOException {
        if ("gemini".equals(apiType)) {
            return parseGeminiResponse(responseJson);
        }
        return parseOpenAiResponse(responseJson);
    }

    private static String parseOpenAiResponse(JsonObject responseJson) throws IOException {
        JsonArray choices = responseJson.getAsJsonArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IOException("API response has no choices array");
        }

        JsonObject firstChoice = choices.get(0).getAsJsonObject();
        JsonObject message = firstChoice.getAsJsonObject("message");
        if (message == null) {
            throw new IOException("API response has no message object");
        }

        JsonElement content = message.get("content");
        if (content == null || content.isJsonNull()) {
            throw new IOException("API response message has no content");
        }

        return content.getAsString();
    }

    private static String parseGeminiResponse(JsonObject responseJson) throws IOException {
        JsonArray candidates = responseJson.getAsJsonArray("candidates");
        if (candidates == null || candidates.isEmpty()) {
            throw new IOException("API response has no candidates array");
        }

        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
        JsonObject content = firstCandidate.getAsJsonObject("content");
        if (content == null) {
            throw new IOException("API response candidate has no content");
        }

        JsonArray parts = content.getAsJsonArray("parts");
        if (parts == null || parts.isEmpty()) {
            throw new IOException("API response content has no parts");
        }

        JsonElement text = parts.get(0).getAsJsonObject().get("text");
        if (text == null || text.isJsonNull()) {
            throw new IOException("API response part has no text");
        }

        return text.getAsString();
    }

    private static void saveAnalysisResult(AnalysisResult result) {
        try {
            Files.createDirectories(analysesDir);
            Path analysisPath = analysesDir.resolve(result.timestamp + "_analysis.json");
            Files.writeString(analysisPath, GSON.toJson(result.toJson()));
        } catch (IOException e) {
            DataMiner.LOGGER.error("VisionAnalyzer: failed to save analysis", e);
        }
    }

    private static class AnalysisResult {
        String timestamp;
        String screenshotPath;
        String aiAnalysis;
        String aiModel;
        String aiError;
        String playerDimension;
        double playerX, playerY, playerZ;
        float playerHealth;
        int playerFood;
        int fps;

        JsonObject toJson() {
            JsonObject obj = new JsonObject();
            obj.addProperty("timestamp", timestamp);
            obj.addProperty("screenshot", screenshotPath);

            if (aiAnalysis != null) {
                obj.addProperty("ai_analysis", aiAnalysis);
                obj.addProperty("ai_model", aiModel);
            }
            if (aiError != null) {
                obj.addProperty("ai_error", aiError);
            }

            JsonObject player = new JsonObject();
            if (playerDimension != null) {
                player.addProperty("dimension", playerDimension);
                JsonObject pos = new JsonObject();
                pos.addProperty("x", Math.round(playerX * 100.0) / 100.0);
                pos.addProperty("y", Math.round(playerY * 100.0) / 100.0);
                pos.addProperty("z", Math.round(playerZ * 100.0) / 100.0);
                player.add("position", pos);
                player.addProperty("health", playerHealth);
                player.addProperty("food", playerFood);
            }
            player.addProperty("fps", fps);
            obj.add("player", player);

            return obj;
        }
    }
}
