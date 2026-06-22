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
3. On startup, DataMiner dumps all registries to `dataminer_dumps/` inside the game directory.

### Output Files

| File | Registry |
|---|---|
| `blocks.json` | All registered blocks |
| `items.json` | All registered items |
| `entities.json` | Entity types |
| `biomes.json` | Biomes |
| `enchantments.json` | Enchantments |
| `status_effects.json` | Mob effects / status effects |
| `sound_events.json` | Sound events |
| `creative_tabs.json` | Creative mode tabs |
| `dimension_types.json` | Dimension types |
| `potions.json` | Potion types |
| `villager_professions.json` | Villager professions |
| `attributes.json` | Entity attributes |

## Commands

| Command | Permission | Description |
|---|---|---|
| `/dataminer dump` | Level 2 | Trigger a registry dump manually |
| `/dataminer perf start` | Level 2 | Start performance monitoring (FPS + MSPT) |
| `/dataminer perf stop` | Level 2 | Stop monitoring and save `performance.json` |

### Performance Report (`performance.json`)

```json
{
  "monitor_version": "0.2.0",
  "start_time": 1234567890,
  "end_time": 1234570000,
  "duration_ms": 2110,
  "duration_seconds": 2.11,
  "fps": {
    "min": 58,
    "max": 120,
    "avg": 89.5,
    "samples": 42
  },
  "mspt": {
    "min": 1.2,
    "max": 8.7,
    "avg": 2.3,
    "samples": 42
  }
}
```

### Configuration

Config file is generated at `config/dataminer-common.toml` after first launch.

| Option | Type | Default | Description |
|---|---|---|---|
| `dumpOnStartup` | bool | `true` | Auto-dump all registries on game startup |
| `dumpOutputDir` | string | `dataminer_dumps` | Output directory for JSON files |
| `dumpBlocks` | bool | `true` | Include block registry |
| `dumpItems` | bool | `true` | Include item registry |
| `dumpEntities` | bool | `true` | Include entity type registry |
| `dumpBiomes` | bool | `true` | Include biome registry |
| `dumpEnchantments` | bool | `true` | Include enchantment registry |
| `dumpStatusEffects` | bool | `true` | Include status effect registry |
| `dumpSoundEvents` | bool | `true` | Include sound event registry |
| `dumpCreativeTabs` | bool | `true` | Include creative tab registry |
| `dumpDimensions` | bool | `true` | Include dimension type registry |
| `dumpPotions` | bool | `true` | Include potion registry |
| `dumpVillagerProfessions` | bool | `true` | Include villager profession registry |
| `dumpAttributes` | bool | `true` | Include attribute registry |

## Project Structure

```
src/main/java/com/skd/dataminer/
├── DataMiner.java          # @Mod entry point, triggers dump on startup
├── DataMinerConfig.java    # NeoForge config with toggles per registry
├── command/
│   └── DataMinerCommands.java  # /dataminer dump|perf commands
├── dumper/
│   └── RegistryDumper.java     # Iterates BuiltInRegistries, exports to JSON
└── perf/
    ├── PerformanceMonitor.java  # FPS + MSPT tracking logic
    └── PerfEventHandlers.java   # ClientTick + ServerTick event hooks

src/main/resources/
├── assets/dataminer/lang/en_us.json  # Language strings
└── dataminer.mixins.json             # Mixin config (placeholder)

src/main/templates/META-INF/
└── neoforge.mods.toml               # Mod metadata template
```

## License

All Rights Reserved.
