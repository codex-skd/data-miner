# Data Miner (Fabric)

Comprehensive diagnostic and profiling mod for Minecraft. Dumps every game registry to JSON, monitors FPS/MSPT, traces live events, measures action latency, analyzes mod impact, redirects noisy third-party logs, and detects known issues — all without blocking the game thread.

This is the **Fabric** build of Data Miner for Minecraft **26.2**. A NeoForge build exists separately.

## Requirements

- Minecraft **26.2**
- Fabric Loader **0.19.3**+
- Fabric API **0.156.0+26.2**
- Java **25**

## Quick Start

1. Drop `data_miner-26.2-fabric-*.jar` in `mods/`
2. Launch the game
3. DataMiner creates its folder structure on startup (async, no freezes)
4. Latency tracker starts automatically by default (`latencyAlwaysOn: true`)

## Commands

| Command | Description |
|---|---|
| `/data_miner dump` | Export all registries to JSON (background) |
| `/data_miner mods analyze` | Regenerate full mod analysis |
| `/data_miner perf start` | Start FPS + MSPT monitoring |
| `/data_miner perf stop` | Stop and save performance report |
| `/data_miner events start` | Trace player actions, entities, chunks |
| `/data_miner events stop` | Stop and save event report |
| `/data_miner latency start` | Start eating/breaking/tick latency measurement |
| `/data_miner latency stop` | Stop and save latency report |
| `/data_miner latency stats` | Show live latency stats without stopping |

## Output Structure

```
data_miner/info_client/  (or data_miner/info_server/)
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

Config file: `config/data_miner.json`

| Key | Default | Description |
|---|---|---|
| `latencyAlwaysOn` | `true` | Run latency tracer automatically |
| `dumpOnStartup` | `false` | Auto-dump registries on game start |
| `dumpBlocks` / `dumpItems` / `dumpEntities` / ... | `true` | Toggle per-registry dumps |

## License

All Rights Reserved.
