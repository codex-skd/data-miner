# DataMiner

DataMiner extracts all Minecraft game registries to structured JSON files for analysis and reference. It also includes **Vision Analysis** — a screen capture tool that sends screenshots to an AI API for automated visual inspection of your game.

## Requirements

- Minecraft **26.1.2**
- NeoForge **26.1.2.76** or later
- Java **25**

## How to Build

```bash
./gradlew build
```

The compiled `.jar` will be in `build/libs/`.

## How to Use

1. Place the mod `.jar` in your `mods/` folder.
2. Launch the game.
3. On startup, DataMiner creates folder structure and dumps all registries.

### Output Structure

```
info_client_data_miner/   (or info_server_data_miner/)
├── startup/
│   ├── registries/
│   │   ├── blocks.json
│   │   ├── items.json
│   │   ├── entities.json
│   │   ├── sound_events.json
│   │   ├── creative_tabs.json
│   │   ├── potions.json
│   │   ├── villager_professions.json
│   │   ├── attributes.json
│   │   └── status_effects.json
│   ├── errors/
│   ├── mods.json
│   └── info.json
├── performance/
│   └── 2026-06-22_15-30-00.json
├── events/
│   └── errors/
└── vision/
    ├── screenshots/
    │   └── 2026-06-22_15-30-00.png
    └── analyses/
        ├── 2026-06-22_15-30-00_analysis.json
        └── 2026-06-22_15-35-00_vision_report.json
```

### Generated Files

| File | Content |
|---|---|
| `startup/mods.json` | All loaded mods with ID, version, dependencies |
| `startup/info.json` | MC version, Java, OS, RAM, locale, side |
| `startup/registries/*.json` | All game registries with detailed properties |
| `performance/*.json` | FPS + MSPT data from `/dataminer perf start/stop` sessions |
| `startup/errors/*.json` | Uncaught exceptions during startup |
| `events/errors/*.json` | Errors during event capture |
| `events/*_events.json` | Live game events (player actions, blocks, entities) |
| `vision/screenshots/*.png` | Screenshots captured for AI analysis |
| `vision/analyses/*_analysis.json` | Per-screenshot AI analysis with player context |
| `vision/analyses/*_vision_report.json` | Session report from `/dataminer vision stop` |

## Commands

| Command | Description |
|---|---|
| `/dataminer dump` | Trigger a registry dump manually |
| `/dataminer perf start` | Start performance monitoring (FPS + MSPT) |
| `/dataminer perf stop` | Stop monitoring and save timestamped report |
| `/dataminer events start` | Start live event tracing (player actions, blocks, entities) |
| `/dataminer latency start` | Start latency tracer (actions + slow ticks) |
| `/dataminer latency stop` | Stop tracing and save latency report |
| `/dataminer vision analyze` | Capture screen and send to AI for visual analysis |
| `/dataminer vision start` | Start periodic screen capture + AI analysis |
| `/dataminer vision stop` | Stop and save vision session report |

## Configuration

Config file is generated at `config/dataminer-common.toml` after first launch.

| Option | Type | Default | Description |
|---|---|---|---|
| `dumpOnStartup` | bool | `true` | Auto-dump all registries on game startup |
| `dumpOutputDir` | string | `dataminer_dumps` | Output directory for JSON files (legacy) |
| `dumpBlocks` | bool | `true` | Include block registry |
| `dumpItems` | bool | `true` | Include item registry |
| `dumpEntities` | bool | `true` | Include entity type registry |
| `dumpBiomes` | bool | `true` | Include biome registry (future) |
| `dumpEnchantments` | bool | `true` | Include enchantment registry (future) |
| `dumpStatusEffects` | bool | `true` | Include status effect registry |
| `dumpSoundEvents` | bool | `true` | Include sound event registry |
| `dumpCreativeTabs` | bool | `true` | Include creative tab registry |
| `dumpDimensions` | bool | `true` | Include dimension type registry (future) |
| `dumpPotions` | bool | `true` | Include potion registry |
| `dumpVillagerProfessions` | bool | `true` | Include villager profession registry |
| `dumpAttributes` | bool | `true` | Include attribute registry |

### Vision Analysis

The vision analyzer captures your game screen and sends it to an AI API for automated visual inspection. It detects rendering glitches, missing textures, UI bugs, and other visual issues.

Two API types are supported:

#### OpenAI-compatible

Works with OpenAI, Groq, OpenRouter, LM Studio, Ollama, and any OpenAI-compatible endpoint.

```toml
visionApiType = "openai"
visionApiEndpoint = "https://api.openai.com/v1/chat/completions"
visionApiKey = "sk-..."
visionModel = "gpt-4o"
```

#### Google Gemini (free tier)

Get a free API key at [aistudio.google.com](https://aistudio.google.com). Up to 1,500 requests/day at no cost.

```toml
visionApiType = "gemini"
visionApiEndpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
visionApiKey = "your-gemini-api-key"
visionModel = "gemini-2.0-flash"
```

#### Vision Config Options

| Option | Type | Default | Description |
|---|---|---|---|
| `visionApiType` | string | `"openai"` | API format: `"openai"` or `"gemini"` |
| `visionApiEndpoint` | string | `""` | API URL (leave empty to disable AI) |
| `visionApiKey` | string | `""` | API key (not required for local LLMs) |
| `visionModel` | string | `"gpt-4o"` | Model name |
| `visionSystemPrompt` | string | `"You are a Minecraft gameplay analyst..."` | System prompt for the AI |
| `visionCaptureInterval` | int | `10` | Seconds between auto-captures (1–3600) |

## Project Structure

```
src/main/java/com/skd/dataminer/
├── DataMiner.java              # @Mod entry point
├── DataMinerConfig.java        # NeoForge config with toggles
├── init/
│   └── Initializer.java        # Folder structure + startup JSONs
├── error/
│   └── ErrorCollector.java     # Uncaught exception handler
├── event/
│   ├── EventTracer.java         # Live event collection + JSON export
│   └── EventHandlers.java       # @SubscribeEvent game event hooks
├── command/
│   └── DataMinerCommands.java  # /dataminer commands
├── dumper/
│   └── RegistryDumper.java     # Registry iteration + JSON export
└── perf/
    ├── PerformanceMonitor.java # FPS + MSPT tracking logic
    └── PerfEventHandlers.java  # ClientTick + ServerTick hooks
└── vision/
    ├── ScreenCapture.java     # Framebuffer capture via Screenshot.grab
    └── VisionAnalyzer.java    # AI analysis engine (OpenAI + Gemini)
```

## License

All Rights Reserved.
