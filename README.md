# Data Miner

Comprehensive diagnostic and profiling mod for Minecraft 1.21.1 (NeoForge). Dumps every game registry to JSON, monitors FPS/MSPT, traces live events, measures action latency, analyzes mod impact, redirects noisy third-party logs, and detects known issues — all without blocking the game thread.

## Status

Beta (`0.0.0-beta.1`). API port of the 26.2 source (22 classes, no mixins, no dependencies) to the 1.21.1 API. `./gradlew build` and `./gradlew runServer` verified: `Done`, 0 FATAL, DataMiner's async startup reports generate cleanly.

## Requirements

- Minecraft **1.21.1**
- NeoForge **21.1.249**+
- Java **21**
- No dependencies

## Quick Start

1. Drop `data_miner-*.jar` in `mods/` (client and/or server).
2. Launch the game. DataMiner creates its folder structure on startup (async, no freezes).
3. Use the `/data_miner` commands (`dump`, `perf`, `events`, `latency`, `mods analyze`).

## License

All Rights Reserved. Original mod by Stalking Dragons.
