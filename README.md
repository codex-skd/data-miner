# DataMiner

Comprehensive diagnostic and profiling mod for Minecraft. Dumps every game registry to JSON, monitors performance (FPS/MSPT), traces live game events, measures action latency, analyzes mod impact, redirects noisy third-party logs, and detects known mod issues — all with zero game-thread blocking.

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
| `/dataminer mods analyze` | Regenerate full mod analysis |
| `/dataminer perf start` | Start FPS + MSPT monitoring |
| `/dataminer perf stop` | Stop and save performance report |
| `/dataminer events start` | Trace player actions, entities, chunks |
| `/dataminer events stop` | Stop and save event report |
| `/dataminer latency start` | Start measuring eating/breaking/tick latency |
| `/dataminer latency stop` | Stop and save latency report |
| `/dataminer latency stats` | Show live latency stats without stopping |

## Output Structure

```
info_client_data_miner/  (or info_server_data_miner/)
├── startup/
│   ├── registries/          # All game registries in enriched JSON
│   ├── logs/
│   │   ├── captured.log     # Redirected WARN/ERROR from other mods
│   │   └── issues.jsonl     # Detected issues as JSON Lines
│   ├── mods.json            # Loaded mods with versions/deps
│   ├── mod_impact.json      # Blocks/items/entities per namespace
│   ├── mod_analysis.json    # Per-mod profile with potential issues
│   ├── info.json            # MC version, Java, OS, RAM, locale
│   └── errors/              # Startup exceptions
├── performance/
│   ├── *.json               # FPS/MSPT session reports
│   └── latency/
│       └── *.json           # Eating/breaking/slow-tick reports with mod context
├── events/
│   ├── *.json               # Live event traces
│   └── errors/              # Event handler exceptions
```

## Configuration

Config file: `config/dataminer-common.toml`

| Key | Default | Description |
|---|---|---|
| `latencyAlwaysOn` | `true` | Run latency tracer automatically at all times |
| `dumpOnStartup` | `false` | Auto-dump registries on game start |

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
├── logs/
│   ├── CapturedAppender.java   # Log4j2 appender for log capture
│   ├── LogRedirector.java      # Appender registration + filter setup
│   └── issues/
│       ├── IssueDetector.java  # Pattern detector interface
│       └── IssueRegistry.java  # Pattern registry + JSONL writer
├── modimpact/
│   └── ModAnalyzer.java        # Registry impact + world context
└── mixin/
```

## License

All Rights Reserved.
