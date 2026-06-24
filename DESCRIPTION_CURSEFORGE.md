# DataMiner

**The ultimate Minecraft data extraction and analysis toolkit.**

DataMiner dumps every game registry to structured JSON, monitors performance in real time, traces live game events, and now captures your screen for AI-powered visual inspection — all from a single lightweight mod built for modpack developers, server admins, and curious players alike.

---

## Features at a Glance

| # | Feature | What It Does |
|---|---|---|
| 1 | **Registry Dumps** | Exports every game registry (blocks, items, entities, and more) to clean JSON |
| 2 | **Performance Monitor** | Tracks FPS and MSPT with min/max/avg over timed sessions |
| 3 | **Event Tracer** | Records player actions, block interactions, entity events, and chunk loads |
| 4 | **Vision Analysis** | Captures your screen and sends it to an AI for automated visual inspection |
| 5 | **Error Collection** | Catches uncaught exceptions and saves them with full stack traces |

All data is exported to a clean folder structure under `info_client_data_miner/` (or `info_server_data_miner/` on dedicated servers).

---

## 1. Registry Dumps

> *Know exactly what's in your game.*

On startup (or on demand), DataMiner walks through every Minecraft registry and exports each entry to a structured JSON file with all relevant properties.

### Dumped Registries

| File | Contents |
|---|---|
| `blocks.json` | Hardness, blast resistance, light emission, sound type, block entity flag |
| `items.json` | Max stack size, max damage, rarity, food properties |
| `entities.json` | Hitbox size, category, fire immunity, tracking range, description ID |
| `sound_events.json` | All registered sound event identifiers |
| `creative_tabs.json` | All creative mode tab identifiers |
| `potions.json` | All potion type identifiers and effects |
| `villager_professions.json` | All villager profession identifiers |
| `attributes.json` | All entity attribute identifiers and default values |
| `status_effects.json` | All mob effect identifiers |

### Example Output

```json
{
  "registry_name": "blocks",
  "size": 1234,
  "entries": [
    {
      "id": "minecraft:stone",
      "namespace": "minecraft",
      "path": "stone",
      "type": "net.minecraft.world.level.block.Block",
      "hardness": 1.5,
      "blast_resistance": 6.0,
      "light_emission": 0,
      "has_block_entity": false,
      "sound_type": {
        "volume": 1.0,
        "pitch": 1.0,
        "break_sound": "minecraft:block.stone.break",
        "step_sound": "minecraft:block.stone.step",
        "place_sound": "minecraft:block.stone.place",
        "hit_sound": "minecraft:block.stone.hit",
        "fall_sound": "minecraft:block.stone.fall"
      }
    }
  ]
}
```

### Commands

```
/dataminer dump
```

Triggers a manual registry dump at any time. Works on both client and server.

---

## 2. Performance Monitor

> *Measure what matters.*

Start a monitoring session, play normally, and stop it to get a detailed FPS and MSPT report with min, max, and average values over the entire session.

### Tracked Metrics

| Metric | Description |
|---|---|
| **FPS** | Frames per second (client-side) — min, max, average, sample count |
| **MSPT** | Milliseconds per tick (server-side) — min, max, average, sample count |

### Example Report

```json
{
  "monitor_version": "0.3.0",
  "start_time": "2026-06-22_15-30-00",
  "end_time": "2026-06-22_15-35-00",
  "duration_ms": 300000,
  "duration_seconds": 300,
  "fps": {
    "min": 45,
    "max": 144,
    "avg": 118.5,
    "samples": 6000
  },
  "mspt": {
    "min": 2.1,
    "max": 45.3,
    "avg": 8.7,
    "samples": 6000
  }
}
```

### Commands

```
/dataminer perf start     → Begin monitoring
/dataminer perf stop      → Stop and save timestamped report
```

---

## 3. Event Tracer

> *See everything that happens in your world.*

The event tracer hooks into the game's event bus and records player actions, entity lifecycles, block interactions, and chunk activity — all with precise timestamps and coordinates.

### Traced Events

| Event | Data Collected |
|---|---|
| **Player Move** | Position (x/y/z), sprinting, sneaking, on ground, health, food, biome, dimension *(sampled every 1s)* |
| **Block Place** | Block ID, position, dimension |
| **Block Interact** | Block ID, position, hand used, dimension |
| **Player Damaged** | Damage amount, dimension, source entity |
| **Entity Death** | Entity ID, position, dimension, was player flag |
| **Entity Join** | Entity ID, position, dimension, is player flag |
| **Chunk Load** | Chunk region X/Z, dimension |

### Example Event

```json
{
  "timestamp_ms": 1719063000123,
  "event_type": "player_move",
  "player": "Steve",
  "player_uuid": "550e8400-e29b-41d4-a716-446655440000",
  "data": {
    "x": 128.5,
    "y": 64.0,
    "z": -340.2,
    "sprinting": false,
    "sneaking": false,
    "on_ground": true,
    "health": 20.0,
    "food_level": 18,
    "biome": "minecraft:plains",
    "dimension": "minecraft:overworld"
  }
}
```

### Commands

```
/dataminer events start    → Begin tracing
/dataminer events stop     → Stop and save timestamped report
```

---

## 4. Vision Analysis

> *Let AI inspect your game for you.*

The vision analyzer captures your game screen and sends it to an AI API for automated visual inspection. It detects rendering glitches, missing textures, UI bugs, z-fighting, lighting errors, and anything else that looks wrong — no human needed.

### How It Works

1. A screenshot of your current game view is captured.
2. Player context is recorded (position, dimension, health, food, FPS).
3. The screenshot is sent to an AI model with vision capabilities.
4. The AI returns a detailed analysis of what it sees.
5. Both the screenshot and analysis are saved for review.

### AI Providers

Two API formats are supported out of the box:

#### Google Gemini (free)

Get a free API key at [aistudio.google.com](https://aistudio.google.com). Up to **1,500 requests per day** at no cost.

```toml
visionApiType = "gemini"
visionApiEndpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
visionApiKey = "your-gemini-api-key"
visionModel = "gemini-2.0-flash"
```

#### OpenAI / Compatible

Works with OpenAI, Groq, OpenRouter, and any OpenAI-compatible endpoint.

```toml
visionApiType = "openai"
visionApiEndpoint = "https://api.openai.com/v1/chat/completions"
visionApiKey = "sk-your-key"
visionModel = "gpt-4o"
```

#### No AI? No Problem

Leave `visionApiEndpoint` empty and screenshots will still be captured with full player context — ready for manual review or your own external tooling.

### Example Analysis Output

```json
{
  "timestamp": "2026-06-22_15-30-00",
  "screenshot": "vision/screenshots/2026-06-22_15-30-00.png",
  "ai_analysis": "The screenshot shows the player standing in a plains biome...",
  "ai_model": "gemini-2.0-flash",
  "player": {
    "dimension": "minecraft:overworld",
    "position": { "x": 128.5, "y": 64.0, "z": -340.2 },
    "health": 20.0,
    "food": 18,
    "fps": 120
  }
}
```

### Commands

```
/dataminer vision analyze   → Capture screen and analyze now
/dataminer vision start     → Start periodic capture + analysis
/dataminer vision stop      → Stop and save session report
```

---

## 5. Error Collection

> *Never miss a crash again.*

DataMiner registers a global uncaught exception handler on startup. Any crash or unhandled error is captured with its full stack trace and saved as a timestamped JSON file — including chained causes up to 10 levels deep.

Errors are organized by phase:

| Directory | What It Contains |
|---|---|
| `startup/errors/` | Errors during game initialization |
| `events/errors/` | Errors during event tracing |

---

## Command Reference

| Command | Description |
|---|---|
| `/dataminer dump` | Trigger a manual registry dump |
| `/dataminer perf start` | Start performance monitoring (FPS + MSPT) |
| `/dataminer perf stop` | Stop monitoring and save report |
| `/dataminer events start` | Start live event tracing |
| `/dataminer events stop` | Stop tracing and save report |
| `/dataminer vision analyze` | Capture screen and send to AI |
| `/dataminer vision start` | Start periodic screen capture + analysis |
| `/dataminer vision stop` | Stop and save vision session report |

---

## Configuration

All settings are in `config/dataminer-common.toml`, generated automatically on first launch.

### Registry Dumps

| Option | Type | Default | Description |
|---|---|---|---|
| `dumpOnStartup` | bool | `true` | Auto-dump all registries on game startup |
| `dumpOutputDir` | string | `dataminer_dumps` | Legacy output directory |
| `dumpBlocks` | bool | `true` | Include block registry |
| `dumpItems` | bool | `true` | Include item registry |
| `dumpEntities` | bool | `true` | Include entity type registry |
| `dumpBiomes` | bool | `true` | Include biome registry *(future)* |
| `dumpEnchantments` | bool | `true` | Include enchantment registry *(future)* |
| `dumpStatusEffects` | bool | `true` | Include status effect registry |
| `dumpSoundEvents` | bool | `true` | Include sound event registry |
| `dumpCreativeTabs` | bool | `true` | Include creative tab registry |
| `dumpDimensions` | bool | `true` | Include dimension type registry *(future)* |
| `dumpPotions` | bool | `true` | Include potion registry |
| `dumpVillagerProfessions` | bool | `true` | Include villager profession registry |
| `dumpAttributes` | bool | `true` | Include attribute registry |

### Vision Analysis

| Option | Type | Default | Description |
|---|---|---|---|
| `visionApiType` | string | `"openai"` | API format: `"openai"` or `"gemini"` |
| `visionApiEndpoint` | string | `""` | API URL (leave empty to disable AI) |
| `visionApiKey` | string | `""` | API key |
| `visionModel` | string | `"gpt-4o"` | Model name |
| `visionSystemPrompt` | string | *see below* | System prompt sent to the AI |
| `visionCaptureInterval` | int | `10` | Seconds between auto-captures (1–3600) |

**Default system prompt:**

> You are a Minecraft gameplay analyst. Your task is to inspect screenshots and identify visual issues: rendering glitches, missing textures, UI bugs, z-fighting, lighting errors, entity problems, or anything that looks wrong.

---

## Output Structure

Everything DataMiner produces lives under a single clean directory:

```
info_client_data_miner/
├── startup/
│   ├── registries/          ← Registry JSON dumps
│   ├── errors/              ← Startup error logs
│   ├── mods.json            ← All loaded mods
│   └── info.json            ← System info
├── performance/             ← FPS + MSPT reports
├── events/                  ← Game event reports
│   └── errors/              ← Event error logs
└── vision/
    ├── screenshots/         ← Captured PNG images
    └── analyses/            ← AI analysis results + session reports
```

---

## Requirements

- **Minecraft** 26.1.2
- **NeoForge** 26.1.2.76 or later
- **Java** 25

---

## Getting Started

1. Drop `dataminer.jar` into your `mods/` folder.
2. Launch the game.
3. On startup, DataMiner initializes its folder structure and dumps all registries.
4. Open `info_client_data_miner/startup/registries/` — your data is ready.
5. Use the `/dataminer` commands to explore the other features.

---

> Built by **Stalking Dragons** — part of the [SKD — Legacy Pack](https://www.curseforge.com/minecraft/modpacks/skd-legacy-pack).
