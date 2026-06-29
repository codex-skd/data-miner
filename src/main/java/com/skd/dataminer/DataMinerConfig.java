package com.skd.dataminer;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DataMinerConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DUMP_ON_STARTUP = BUILDER
            .comment("Whether to dump all registries to JSON on game startup")
            .define("dumpOnStartup", false);

    public static final ModConfigSpec.ConfigValue<String> DUMP_OUTPUT_DIR = BUILDER
            .comment("Directory to output dump files (relative to game directory)")
            .define("dumpOutputDir", "dataminer_dumps");

    public static final ModConfigSpec.BooleanValue DUMP_BLOCKS = BUILDER
            .comment("Dump block registry")
            .define("dumpBlocks", true);

    public static final ModConfigSpec.BooleanValue DUMP_ITEMS = BUILDER
            .comment("Dump item registry")
            .define("dumpItems", true);

    public static final ModConfigSpec.BooleanValue DUMP_ENTITIES = BUILDER
            .comment("Dump entity type registry")
            .define("dumpEntities", true);

    public static final ModConfigSpec.BooleanValue DUMP_BIOMES = BUILDER
            .comment("Dump biome registry")
            .define("dumpBiomes", true);

    public static final ModConfigSpec.BooleanValue DUMP_ENCHANTMENTS = BUILDER
            .comment("Dump enchantment registry")
            .define("dumpEnchantments", true);

    public static final ModConfigSpec.BooleanValue DUMP_STATUS_EFFECTS = BUILDER
            .comment("Dump status effect (mob effect) registry")
            .define("dumpStatusEffects", true);

    public static final ModConfigSpec.BooleanValue DUMP_SOUND_EVENTS = BUILDER
            .comment("Dump sound event registry")
            .define("dumpSoundEvents", true);

    public static final ModConfigSpec.BooleanValue DUMP_CREATIVE_TABS = BUILDER
            .comment("Dump creative mode tab registry")
            .define("dumpCreativeTabs", true);

    public static final ModConfigSpec.BooleanValue DUMP_DIMENSIONS = BUILDER
            .comment("Dump dimension type registry")
            .define("dumpDimensions", true);

    public static final ModConfigSpec.BooleanValue DUMP_POTIONS = BUILDER
            .comment("Dump potion registry")
            .define("dumpPotions", true);

    public static final ModConfigSpec.BooleanValue DUMP_VILLAGER_PROFESSIONS = BUILDER
            .comment("Dump villager profession registry")
            .define("dumpVillagerProfessions", true);

    public static final ModConfigSpec.BooleanValue DUMP_ATTRIBUTES = BUILDER
            .comment("Dump attribute registry")
            .define("dumpAttributes", true);

    // --- Vision analysis config ---
    public static final ModConfigSpec.ConfigValue<String> VISION_API_TYPE = BUILDER
            .comment("API type: 'openai' for OpenAI-compatible endpoints, 'gemini' for Google Gemini")
            .define("visionApiType", "gemini");

    public static final ModConfigSpec.ConfigValue<String> VISION_API_ENDPOINT = BUILDER
            .comment("API endpoint URL for vision analysis (leave empty to disable AI analysis)")
            .define("visionApiEndpoint", "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent");

    public static final ModConfigSpec.ConfigValue<String> VISION_API_KEY = BUILDER
            .comment("API key for the vision endpoint (optional for local LLMs like Ollama)")
            .define("visionApiKey", "CHANGE_ME");

    public static final ModConfigSpec.ConfigValue<String> VISION_MODEL = BUILDER
            .comment("Model name for vision analysis (e.g., gpt-4o, llava, minicpm-v)")
            .define("visionModel", "gemini-2.5-flash");

    public static final ModConfigSpec.ConfigValue<String> VISION_SYSTEM_PROMPT = BUILDER
            .comment("System prompt sent to the AI for vision analysis")
            .define("visionSystemPrompt", "You are a Minecraft gameplay analyst. Your task is to inspect screenshots and identify visual issues: rendering glitches, missing textures, UI bugs, z-fighting, lighting errors, entity problems, or anything that looks wrong.");

    public static final ModConfigSpec.IntValue VISION_CAPTURE_INTERVAL = BUILDER
            .comment("Seconds between automatic screen captures when vision mode is active")
            .defineInRange("visionCaptureInterval", 10, 1, 3600);

    // --- Latency tracer config ---
    public static final ModConfigSpec.BooleanValue LATENCY_ALWAYS_ON = BUILDER
            .comment("If true, latency tracer runs automatically at all times without needing /dataminer latency start")
            .define("latencyAlwaysOn", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
