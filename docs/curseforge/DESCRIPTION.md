Comprehensive diagnostic and profiling mod for Minecraft 26.1.2. Dumps every game registry to enriched JSON, monitors FPS/MSPT, traces live game events, measures action latency (eating, block breaking, slow ticks), profiles mod impact with per-mod analysis, and logs errors — all with zero game-thread blocking.

## Features

- **Registry Dumps** — All BuiltInRegistries exported to JSON with detailed properties (hardness, food, sound, dimensions)
- **Performance Monitor** — FPS (min/max/avg) + MSPT via `/dataminer perf start/stop`
- **Latency Analyzer** — Automatically tracks eating time, block breaking time, and slow ticks (>50ms MSPT). Runs by default (`latencyAlwaysOn: true`), no command needed
- **Mod-Aware Slow Ticks** — Each slow tick includes entities_by_mod breakdown to identify which mod causes lag
- **Full Mod Analysis** — `startup/mod_analysis.json` with per-mod ID, version, registry counts, dependencies, and potential issues (missing deps, high entity counts)
- **Event Tracer** — Live game events: player movement, block placement, damage, entity spawns/deaths, chunk loads
- **Error Collection** — All uncaught exceptions logged with full stack traces to `startup/errors/` and `events/errors/`
- **Async I/O** — All file operations run on background threads, zero game freezes

## Commands

| Command | Description |
|---|---|
| `/dataminer dump` | Export all registries to JSON (background) |
| `/dataminer mods analyze` | Regenerate full mod analysis |
| `/dataminer perf start/stop` | Start/stop FPS + MSPT monitoring |
| `/dataminer events start/stop` | Start/stop live game event tracing |
| `/dataminer latency start/stop` | Start/stop latency measurement |

## Output Structure

```
info_client_data_miner/  (or info_server_data_miner/)
├── startup/
│   ├── registries/        # All game registries in enriched JSON
│   ├── mods.json          # Loaded mods with versions/deps
│   ├── mod_impact.json    # Blocks/items/entities per namespace
│   ├── mod_analysis.json  # Per-mod profile with potential issues
│   ├── info.json          # MC version, Java, OS, RAM, locale
│   └── errors/            # Startup exceptions
├── performance/
│   ├── *.json             # FPS/MSPT session reports
│   └── latency/
│       └── *.json         # Eating/breaking/slow-tick reports with mod context
├── events/
│   ├── *.json             # Live event traces
│   └── errors/            # Event handler exceptions
```

## Configuration

Config file: `config/dataminer-common.toml`

### Key Options

| Key | Default | Description |
|---|---|---|
| `latencyAlwaysOn` | `true` | Run latency tracer automatically |
| `dumpOnStartup` | `false` | Auto-dump registries on game start |

## Requirements

- NeoForge **26.1.2.76**+
- Java **25**
- Client and Server (Both)
