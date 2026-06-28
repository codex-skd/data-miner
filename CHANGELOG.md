# Changelog

All notable changes to DataMiner are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.6.3] - 2026-06-25

### Fixed

- **Server crash**: `/dataminer vision` no longer throws `NoClassDefFoundError` on dedicated servers. `VisionAnalyzer.isClientReady()` guard added.
- API key default changed to `CHANGE_ME` (was hardcoded real key).

## [0.6.2] - 2026-06-24

### Fixed

- **Game freeze fix**: All heavy I/O operations (registry dumps, report saves) now run on background threads via `DataMinerExecutor`, no longer blocking the game thread.
- `DUMP_ON_STARTUP` default changed to `false` to prevent startup freeze.
- Commands respond immediately and process work asynchronously; progress is logged to console.
- `Initializer.init()` and `RegistryDumper.dumpAll()` moved off the main thread on startup.
- All `/dataminer * stop` commands return instantly, saving happens in background.
- `/dataminer dump` returns instantly, dump runs in background.

## [0.6.1] - 2026-06-24

### Added

- **Vision Analysis** — screen capture + AI-powered visual inspection.
  - `/dataminer vision analyze` captures the game screen and sends it to an AI API.
  - `/dataminer vision start/stop` for periodic automated capture and analysis.
  - `ScreenCapture.java` using vanilla `Screenshot.grab` for framebuffer capture.
  - `VisionAnalyzer.java` with dual API support: OpenAI-compatible and Google Gemini.
  - Screenshots saved to `vision/screenshots/`, analyses to `vision/analyses/`.
  - Player context included in each analysis (position, dimension, health, food, FPS).
  - Configurable capture interval, API endpoint, model, system prompt, and API key.
- `visionApiType` config option to switch between `"openai"` and `"gemini"` API formats.
- `vision/` directories auto-created on startup by `Initializer.java`.
- README updated with full Vision Analysis documentation and setup guide.

## [0.4.1] - 2026-06-22

### Changed

- First beta release. All features tested and functional.
- Release type promoted from Alpha to Beta.

## [0.4.0] - 2026-06-22

### Added

- `/dataminer events start` and `/dataminer events stop` commands for live game event tracing.
- `EventTracer.java` collecting events in memory and exporting them to timestamped JSON in `events/`.
- `EventHandlers.java` with hooks for:
  - `PlayerTickEvent` — player position, sprinting, sneaking, health, food, biome, dimension (sampled every 1s).
  - `BlockEvent.EntityPlaceEvent` — block placed with position and dimension.
  - `PlayerInteractEvent.RightClickBlock` — block interaction with position and hand.
  - `LivingDamageEvent.Pre` — damage to players with amount and source entity.
  - `LivingDeathEvent` — entity deaths with position, type, dimension.
  - `EntityJoinLevelEvent` — entities entering the world.
  - `ChunkEvent.Load` — chunk loads with coordinates and dimension.
- Event errors auto-captured to `events/errors/` with stack traces.
- Event report JSON includes `total_events`, `total_errors`, and all captured event data.

## [0.3.0] - 2026-06-22

### Added

- Folder structure initialization on startup (`Initializer.java`).
- `info_client_data_miner/` or `info_server_data_miner/` with organized subdirectories.
- `startup/mods.json` listing all loaded mods with IDs, versions, and dependencies.
- `startup/info.json` with MC version, Java, OS, RAM, locale, and side info.
- `startup/registries/` for all registry dumps (moved from `dataminer_dumps`).
- `startup/errors/` for errors captured during startup.
- `events/errors/` placeholder for in-game event error logging.
- `ErrorCollector.java` capturing uncaught exceptions via `Thread.setDefaultUncaughtExceptionHandler`.
- Performance reports now saved with timestamped filenames in `performance/` folder.

### Changed

- Registry dumps moved to `startup/registries/` subdirectory.
- Performance reports moved to `performance/` subdirectory with timestamp naming.

## [0.2.0] - 2026-06-22

### Added

- `/dataminer dump` command to trigger registry dump manually.
- `/dataminer perf start` and `/dataminer perf stop` commands for performance monitoring.
- `PerformanceMonitor.java` tracking FPS (min/max/avg) and MSPT (min/max/avg) during sessions.
- `PerfEventHandlers.java` with `ClientTickEvent` for FPS and `ServerTickEvent` for MSPT.
- Enriched `blocks.json` with `hardness`, `blast_resistance`, `light_emission`, `has_block_entity`, and `sound_type` (volume, pitch, break/step/place/hit/fall sounds).
- Enriched `items.json` with `max_stack_size`, `max_damage`, `rarity`, and `food_properties` (nutrition, saturation, can_always_eat).
- Enriched `entities.json` with `width`, `height`, `category`, `fire_immune`, `can_summon`, `client_tracking_range`, `update_interval`, `description_id`.
- Performance report saved to `dataminer_dumps/performance.json`.

### Removed

- Biomes, enchantments, and dimension types from dump list (not accessible via `BuiltInRegistries` in this Minecraft version; will be re-added via dynamic registry access in a future version).

## [0.1.0] - 2026-06-22

### Added

- Initial project setup with NeoForge MDK for Minecraft 26.1.2.
- `DataMiner.java` main mod class with `@Mod` annotation and `FMLCommonSetupEvent` listener.
- `DataMinerConfig.java` with per-registry toggle flags and auto-dump option.
- `RegistryDumper.java` that iterates `BuiltInRegistries` and exports each registry to a JSON file.
- Dump output to `dataminer_dumps/` directory (configurable).
- Supported registries: blocks, items, entity types, mob effects, sound events, creative mode tabs, potions, villager professions, attributes.
- Language file `en_us.json` with future command strings.
- Mixin config placeholder for future mixin hooks.
