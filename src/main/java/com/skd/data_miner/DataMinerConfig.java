package com.skd.data_miner;

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

    public static final ModConfigSpec.BooleanValue LATENCY_ALWAYS_ON = BUILDER
            .comment("If true, latency tracer runs automatically at all times without needing /dataminer latency start")
            .define("latencyAlwaysOn", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
