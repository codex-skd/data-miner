# Graph Report - 26.2  (2026-08-02)

## Corpus Check
- 32 files · ~55,668 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 228 nodes · 401 edges · 21 communities
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS · INFERRED: 2 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `955a370e`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- LatencyTracer
- .recordEvent
- ModAnalyzer.java
- RegistryDumper
- IssueRegistry.java
- .register
- DataMiner.java
- PerformanceMonitor
- CurseForge — Variables del proyecto
- Flujo de trabajo — Data Miner (NeoForge)
- CapturedAppender
- Initializer.java
- Data Miner
- Changelog
- CLAUDE.md — data_miner (26.2)
- gradlew

## God Nodes (most connected - your core abstractions)
1. `LatencyTracer` - 14 edges
2. `CurseForge — Variables del proyecto` - 13 edges
3. `Flujo de trabajo — Data Miner (NeoForge)` - 11 edges
4. `EventHandlers` - 10 edges
5. `EventTracer` - 9 edges
6. `PerformanceMonitor` - 9 edges
7. `RegistryDumper` - 8 edges
8. `CapturedAppender` - 8 edges
9. `LatencyEventHandlers` - 7 edges
10. `Data Miner` - 7 edges

## Surprising Connections (you probably didn't know these)
- `IssueRegistry` --references--> `IssueDetector`  [EXTRACTED]
  src/main/java/com/skd/data_miner/logs/issues/IssueRegistry.java → src/main/java/com/skd/data_miner/logs/issues/IssueDetector.java

## Import Cycles
- None detected.

## Communities (21 total, 0 thin omitted)

### Community 0 - "LatencyTracer"
Cohesion: 0.13
Nodes (12): BlockPos, LeftClickBlock, BreakEntry, EventBusSubscriber, Pre, SubscribeEvent, LatencyEventHandlers, Gson (+4 more)

### Community 1 - ".recordEvent"
Cohesion: 0.26
Nodes (11): EntityJoinLevelEvent, EntityPlaceEvent, LivingDeathEvent, Load, Player, RightClickBlock, EventHandlers, EventBusSubscriber (+3 more)

### Community 2 - "ModAnalyzer.java"
Cohesion: 0.17
Nodes (10): MinecraftServer, Post, Gson, JsonObject, ModAnalyzer, ModStats, EventBusSubscriber, Pre (+2 more)

### Community 3 - "RegistryDumper"
Cohesion: 0.23
Nodes (9): BooleanValue, Builder, ConfigValue, ModConfigSpec, Registry, DataMinerConfig, Gson, JsonObject (+1 more)

### Community 4 - "IssueRegistry.java"
Cohesion: 0.18
Nodes (10): Pattern, IssueDetector, JsonObject, extract(), IssueRegistry, Gson, JsonObject, Override (+2 more)

### Community 5 - ".register"
Cohesion: 0.21
Nodes (7): CommandDispatcher, CommandSourceStack, DataMinerCommands, DataMinerExecutor, EventTracer, Gson, JsonObject

### Community 6 - "DataMiner.java"
Cohesion: 0.20
Nodes (9): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, RegisterCommandsEvent, DataMiner, Logger, ErrorCollector (+1 more)

### Community 7 - "PerformanceMonitor"
Cohesion: 0.19
Nodes (6): ClientPerfHandlers, EventBusSubscriber, Pre, SubscribeEvent, Gson, PerformanceMonitor

### Community 8 - "CurseForge — Variables del proyecto"
Cohesion: 0.14
Nodes (13): Changelog, CurseForge — Variables del proyecto, Descripcion del proyecto, Estructura del changelog (HTML), Flujo completo, Parámetros del upload, Proyecto, Rama (+5 more)

### Community 9 - "Flujo de trabajo — Data Miner (NeoForge)"
Cohesion: 0.17
Nodes (11): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Data Miner (NeoForge), Flujo por tarea, Idioma (+3 more)

### Community 10 - "CapturedAppender"
Cohesion: 0.29
Nodes (4): AbstractAppender, LogEvent, CapturedAppender, Override

### Community 11 - "Initializer.java"
Cohesion: 0.31
Nodes (4): Initializer, Gson, Logger, LogRedirector

### Community 12 - "Data Miner"
Cohesion: 0.25
Nodes (7): Commands, Configuration, Data Miner, License, Output Structure, Quick Start, Requirements

### Community 13 - "Changelog"
Cohesion: 0.33
Nodes (5): [0.0.0-beta.1] - 2026-07-26, [1.0.0] - 2026-07-27, Añadido, Cambiado, Changelog

### Community 14 - "CLAUDE.md — data_miner (26.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — data_miner (26.2), Prioridad de instrucciones, Workflow del mod

### Community 15 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **32 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `Cambiado`, `Añadido`, `Requirements` (+27 more)
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `EventTracer` connect `.register` to `.recordEvent`?**
  _High betweenness centrality (0.031) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `Cambiado` to the rest of the system?**
  _32 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `LatencyTracer` be split into smaller, more focused modules?**
  _Cohesion score 0.12643678160919541 - nodes in this community are weakly interconnected._
- **Should `CurseForge — Variables del proyecto` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._