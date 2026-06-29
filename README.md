# DataMiner

Comprehensive diagnostic and profiling mod for Minecraft. Dumps every game registry to JSON, monitors performance (FPS/MSPT), traces live game events, measures action latency, analyzes mod impact, captures screenshots for AI visual inspection, and logs errors — all with zero game-thread blocking.

## Requirements

- Minecraft **26.1.2**
- NeoForge **26.1.2.76**+
- Java **25**

## Quick Start

1. Drop `dataminer-*.jar` in `mods/`
2. Launch the game
3. DataMiner creates its folder structure on startup (async, no freeze)
4. Latency tracker runs automatically by default (`latencyAlwaysOn: true`)

## Commands

| Command | Description |
|---|---|
| `/dataminer dump` | Export all registries to JSON (background) |
| `/dataminer perf start` | Start FPS + MSPT monitoring |
| `/dataminer perf stop` | Stop and save performance report |
| `/dataminer events start` | Trace player actions, entities, chunks |
| `/dataminer events stop` | Stop and save event report |
| `/dataminer latency start` | Start measuring eating/breaking/tick latency |
| `/dataminer latency stop` | Stop and save latency report |
| `/dataminer vision analyze` | Capture screen + AI analysis (client only) |
| `/dataminer vision start` | Periodic capture + AI (client only) |
| `/dataminer vision stop` | Stop and save vision report |

## Output Structure

```
info_client_data_miner/  (or info_server_data_miner/)
├── startup/
│   ├── registries/        # All game registries in enriched JSON
│   ├── mods.json          # Loaded mods with versions/deps
│   ├── mod_impact.json    # Blocks/items/entities per namespace
│   ├── info.json          # MC version, Java, OS, RAM, locale
│   └── errors/            # Startup exceptions
├── performance/
│   ├── *_perf.json        # FPS/MSPT session reports
│   └── latency/
│       └── *_latency.json # Eating/breaking/slow-tick reports
├── events/
│   ├── *_events.json      # Live event traces
│   └── errors/            # Event handler exceptions
└── vision/
    ├── screenshots/       # Captured PNG files
    └── analyses/          # AI analysis results
```

## Configuration

Config file: `config/dataminer-common.toml`

### Registry Dump

| Key | Default | Description |
|---|---|---|
| `dumpOnStartup` | `false` | Auto-dump all registries on game start |
| `dumpBlocks` | `true` | Include block registry |
| `dumpItems` | `true` | Include item registry |
| `dumpEntities` | `true` | Include entity type registry |
| `dumpStatusEffects` | `true` | Include status effect registry |
| `dumpSoundEvents` | `true` | Include sound event registry |
| `dumpCreativeTabs` | `true` | Include creative tab registry |
| `dumpPotions` | `true` | Include potion registry |
| `dumpVillagerProfessions` | `true` | Include villager profession registry |
| `dumpAttributes` | `true` | Include attribute registry |

### Latency Tracer

| Key | Default | Description |
|---|---|---|
| `latencyAlwaysOn` | `true` | Run latency tracer automatically at all times |

### Vision Analysis

| Key | Default | Description |
|---|---|---|
| `visionApiType` | `gemini` | `"openai"` or `"gemini"` |
| `visionApiEndpoint` | gemini endpoint | API URL |
| `visionApiKey` | `CHANGE_ME` | Your API key |
| `visionModel` | `gemini-2.5-flash` | Model name |
| `visionCaptureInterval` | `10` | Seconds between captures |

## Project Structure

```
src/main/java/com/skd/dataminer/
├── DataMiner.java              # @Mod entry point
├── DataMinerConfig.java        # NeoForge config
├── DataMinerExecutor.java      # Background I/O thread pool
├── init/
│   └── Initializer.java        # Folder structure + startup JSONs
├── error/
│   └── ErrorCollector.java     # Uncaught exception handler
├── command/
│   └── DataMinerCommands.java  # All /dataminer subcommands
├── dumper/
│   └── RegistryDumper.java     # Registry iteration + enriched JSON
├── perf/
│   ├── PerformanceMonitor.java # FPS + MSPT tracking
│   └── PerfEventHandlers.java  # Server tick hooks
├── event/
│   ├── EventTracer.java        # Live event collection
│   └── EventHandlers.java      # Game event hooks
├── latency/
│   ├── LatencyTracer.java      # Action latency tracker
│   ├── LatencyEventHandlers.java # Eating + block break hooks
│   └── ClientPerfHandlers.java # Client FPS capture
├── modimpact/
│   └── ModAnalyzer.java        # Registry impact + world context
└── vision/
    ├── ScreenCapture.java      # Framebuffer capture
    └── VisionAnalyzer.java     # OpenAI/Gemini API integration
```

## License

All Rights Reserved.
