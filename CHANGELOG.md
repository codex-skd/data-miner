# Changelog

All notable changes to DataMiner are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
