# Changelog

All notable changes to DataMiner are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.0] - 2026-06-22

### Added

- Initial project setup with NeoForge MDK for Minecraft 26.1.2.
- `DataMiner.java` main mod class with `@Mod` annotation and `FMLCommonSetupEvent` listener.
- `DataMinerConfig.java` with per-registry toggle flags and auto-dump option.
- `RegistryDumper.java` that iterates `BuiltInRegistries` and exports each registry to a JSON file.
- Dump output to `dataminer_dumps/` directory (configurable).
- Supported registries: blocks, items, entity types, biomes, enchantments, mob effects, sound events, creative mode tabs, dimension types, potions, villager professions, attributes.
- Language file `en_us.json` with future command strings.
- Mixin config placeholder for future mixin hooks.
