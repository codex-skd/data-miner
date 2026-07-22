# DataMiner

Comprehensive diagnostic and profiling mod for Minecraft. Dumps every game registry to JSON, monitors FPS/MSPT, traces live events, measures action latency, analyzes mod impact, redirects noisy third-party logs, and detects known issues — all without blocking the game thread.

## Requirements

- Minecraft **26.1.2**
- NeoForge **26.1.2.76**+
- Java **25**

## Quick Start

1. Drop `dataminer-*.jar` in `mods/`
2. Launch the game
3. DataMiner creates its folder structure on startup (async, no freezes)
4. Latency tracker starts automatically by default (`latencyAlwaysOn: true`)

## Commands

| Command | Description |
|---|---|
| `/dataminer dump` | Export all registries to JSON (background) |
| `/dataminer mods analyze` | Regenerate full mod analysis |
| `/dataminer perf start` | Start FPS + MSPT monitoring |
| `/dataminer perf stop` | Stop and save performance report |
| `/dataminer events start` | Trace player actions, entities, chunks |
| `/dataminer events stop` | Stop and save event report |
| `/dataminer latency start` | Start eating/breaking/tick latency measurement |
| `/dataminer latency stop` | Stop and save latency report |
| `/dataminer latency stats` | Show live latency stats without stopping |

## Output Structure

```
info_client_data_miner/  (or info_server_data_miner/)
├── startup/
│   ├── registries/          # All game registries in enriched JSON
│   ├── logs/
│   │   ├── captured.log     # WARN/ERROR redirected from other mods
│   │   └── issues.jsonl     # Detected issues as JSON Lines
│   ├── mods.json            # Loaded mods with versions/deps
│   ├── mod_impact.json      # Blocks/items/entities per namespace
│   ├── mod_analysis.json    # Per-mod profile with potential issues
│   ├── info.json            # MC version, Java, OS, RAM, locale
│   └── errors/              # Startup exceptions
├── performance/
│   ├── *.json               # FPS/MSPT session reports
│   └── latency/
│       └── *.json           # Eating/breaking/slow-tick reports
├── events/
│   ├── *.json               # Live event traces
│   └── errors/              # Event handler exceptions
```

## Configuration

Config file: `config/dataminer-common.toml`

| Key | Default | Description |
|---|---|---|
| `latencyAlwaysOn` | `true` | Run latency tracer automatically |
| `dumpOnStartup` | `false` | Auto-dump registries on game start |

## License

All Rights Reserved.
