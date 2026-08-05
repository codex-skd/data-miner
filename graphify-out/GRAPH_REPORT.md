# Graph Report - 26.2  (2026-08-06)

## Corpus Check
- 33 files · ~55,742 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 231 nodes · 403 edges · 20 communities
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS · INFERRED: 2 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `b588ed95`
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

## Communities (20 total, 0 thin omitted)

### Community 0 - "LatencyTracer"
Cohesion: 0.13
Nodes (12): BlockPos, LeftClickBlock, BreakEntry, EventBusSubscriber, Pre, SubscribeEvent, LatencyEventHandlers, Gson (+4 more)

### Community 1 - ".recordEvent"
Cohesion: 0.26
Nodes (11): EntityJoinLevelEvent, EntityPlaceEvent, LivingDeathEvent, Load, Player, RightClickBlock, EventHandlers, EventBusSubscriber (+3 more)

### Community 2 - "ModAnalyzer.java"
Cohesion: 0.16
Nodes (9): MinecraftServer, Initializer, Gson, Logger, LogRedirector, Gson, JsonObject, ModAnalyzer (+1 more)

### Community 3 - "RegistryDumper"
Cohesion: 0.23
Nodes (9): BooleanValue, Builder, ConfigValue, ModConfigSpec, Registry, DataMinerConfig, Gson, JsonObject (+1 more)

### Community 4 - "IssueRegistry.java"
Cohesion: 0.11
Nodes (14): AbstractAppender, LogEvent, Pattern, CapturedAppender, Override, IssueDetector, JsonObject, extract() (+6 more)

### Community 5 - ".register"
Cohesion: 0.19
Nodes (7): CommandDispatcher, CommandSourceStack, DataMinerCommands, DataMinerExecutor, EventTracer, Gson, JsonObject

### Community 6 - "DataMiner.java"
Cohesion: 0.20
Nodes (9): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, RegisterCommandsEvent, DataMiner, Logger, ErrorCollector (+1 more)

### Community 7 - "PerformanceMonitor"
Cohesion: 0.12
Nodes (11): Post, ClientPerfHandlers, EventBusSubscriber, Pre, SubscribeEvent, EventBusSubscriber, Pre, SubscribeEvent (+3 more)

### Community 8 - "CurseForge — Variables del proyecto"
Cohesion: 0.14
Nodes (13): Changelog, CurseForge — Variables del proyecto, Descripcion del proyecto, Estructura del changelog (HTML), Flujo completo, Parámetros del upload, Proyecto, Rama (+5 more)

### Community 9 - "Flujo de trabajo — Data Miner (NeoForge)"
Cohesion: 0.17
Nodes (11): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Data Miner (NeoForge), Flujo por tarea, Idioma (+3 more)

### Community 12 - "Data Miner"
Cohesion: 0.25
Nodes (7): Commands, Configuration, Data Miner, License, Output Structure, Quick Start, Requirements

### Community 13 - "Changelog"
Cohesion: 0.25
Nodes (7): [0.0.0-beta.1] - 2026-07-26, [1.0.0] - 2026-07-27, [1.0.1] - 2026-08-05, Añadido, Cambiado, Change, Changelog

### Community 14 - "CLAUDE.md — data_miner (26.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — data_miner (26.2), Prioridad de instrucciones, Workflow del mod

### Community 15 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **33 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `Change`, `Cambiado`, `Añadido` (+28 more)
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `LatencyTracer` connect `LatencyTracer` to `.register`?**
  _High betweenness centrality (0.041) - this node is a cross-community bridge._
- **Why does `EventTracer` connect `.register` to `.recordEvent`?**
  _High betweenness centrality (0.030) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `Change` to the rest of the system?**
  _33 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `LatencyTracer` be split into smaller, more focused modules?**
  _Cohesion score 0.1330049261083744 - nodes in this community are weakly interconnected._
- **Should `IssueRegistry.java` be split into smaller, more focused modules?**
  _Cohesion score 0.11375661375661375 - nodes in this community are weakly interconnected._
- **Should `PerformanceMonitor` be split into smaller, more focused modules?**
  _Cohesion score 0.11956521739130435 - nodes in this community are weakly interconnected._
- **Should `CurseForge — Variables del proyecto` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._