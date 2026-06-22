# DataMiner

DataMiner extracts all Minecraft game registries to structured JSON files for analysis and reference.

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
└── events/
    └── errors/
```

### Generated Files

| File | Content |
|---|---|
| `startup/mods.json` | All loaded mods with ID, version, dependencies |
| `startup/info.json` | MC version, Java, OS, RAM, locale, side |
| `startup/registries/*.json` | All game registries with detailed properties |
| `performance/*.json` | FPS + MSPT data from `/dataminer perf start/stop` sessions |
| `startup/errors/*.json` | Uncaught exceptions during startup |
| `events/errors/*.json` | Future: exceptions during in-game events |

## Commands

| Command | Description |
|---|---|
| `/dataminer dump` | Trigger a registry dump manually |
| `/dataminer perf start` | Start performance monitoring (FPS + MSPT) |
| `/dataminer perf stop` | Stop monitoring and save timestamped report |

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

## Project Structure

```
src/main/java/com/skd/dataminer/
├── DataMiner.java              # @Mod entry point
├── DataMinerConfig.java        # NeoForge config with toggles
├── init/
│   └── Initializer.java        # Folder structure + startup JSONs
├── error/
│   └── ErrorCollector.java     # Uncaught exception handler
├── command/
│   └── DataMinerCommands.java  # /dataminer commands
├── dumper/
│   └── RegistryDumper.java     # Registry iteration + JSON export
└── perf/
    ├── PerformanceMonitor.java # FPS + MSPT tracking logic
    └── PerfEventHandlers.java  # ClientTick + ServerTick hooks
```

## License

All Rights Reserved.
