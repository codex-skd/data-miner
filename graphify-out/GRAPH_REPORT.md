# Graph Report - 26.1.2  (2026-08-02)

## Corpus Check
- 33 files · ~57,866 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 274 nodes · 446 edges · 21 communities
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS · INFERRED: 2 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9e6cd732`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- LatencyEventHandlers.java
- .recordEvent
- .register
- PerformanceMonitor
- RegistryDumper
- IssueRegistry.java
- DataMiner.java
- CapturedAppender
- Changelog
- Initializer.java
- CurseForge — Variables del proyecto
- Flujo de trabajo — Data Miner (NeoForge)
- Data Miner
- v0.0.0-beta.1 — Initial CurseForge Release
- v1.0.0 — First Stable Release
- CLAUDE.md — data_miner (26.1.2)
- gradlew

## God Nodes (most connected - your core abstractions)
1. `Changelog` - 17 edges
2. `LatencyTracer` - 14 edges
3. `CurseForge — Variables del proyecto` - 13 edges
4. `Flujo de trabajo — Data Miner (NeoForge)` - 11 edges
5. `EventHandlers` - 10 edges
6. `EventTracer` - 9 edges
7. `PerformanceMonitor` - 9 edges
8. `RegistryDumper` - 8 edges
9. `CapturedAppender` - 8 edges
10. `LatencyEventHandlers` - 7 edges

## Surprising Connections (you probably didn't know these)
- `IssueRegistry` --references--> `IssueDetector`  [EXTRACTED]
  src/main/java/com/skd/data_miner/logs/issues/IssueRegistry.java → src/main/java/com/skd/data_miner/logs/issues/IssueDetector.java

## Import Cycles
- None detected.

## Communities (21 total, 0 thin omitted)

### Community 0 - "LatencyEventHandlers.java"
Cohesion: 0.22
Nodes (9): BlockPos, LeftClickBlock, BreakEntry, EventBusSubscriber, Pre, SubscribeEvent, LatencyEventHandlers, Start (+1 more)

### Community 1 - ".recordEvent"
Cohesion: 0.26
Nodes (11): EntityJoinLevelEvent, EntityPlaceEvent, LivingDeathEvent, Load, Player, RightClickBlock, EventHandlers, EventBusSubscriber (+3 more)

### Community 2 - ".register"
Cohesion: 0.12
Nodes (10): CommandDispatcher, CommandSourceStack, DataMinerCommands, DataMinerExecutor, EventTracer, Gson, JsonObject, Gson (+2 more)

### Community 3 - "PerformanceMonitor"
Cohesion: 0.12
Nodes (11): Post, ClientPerfHandlers, EventBusSubscriber, Pre, SubscribeEvent, EventBusSubscriber, Pre, SubscribeEvent (+3 more)

### Community 4 - "RegistryDumper"
Cohesion: 0.23
Nodes (9): BooleanValue, Builder, ConfigValue, ModConfigSpec, Registry, DataMinerConfig, Gson, JsonObject (+1 more)

### Community 5 - "IssueRegistry.java"
Cohesion: 0.18
Nodes (10): Pattern, IssueDetector, JsonObject, extract(), IssueRegistry, Gson, JsonObject, Override (+2 more)

### Community 6 - "DataMiner.java"
Cohesion: 0.20
Nodes (9): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, RegisterCommandsEvent, DataMiner, Logger, ErrorCollector (+1 more)

### Community 7 - "CapturedAppender"
Cohesion: 0.29
Nodes (4): AbstractAppender, LogEvent, CapturedAppender, Override

### Community 8 - "Changelog"
Cohesion: 0.05
Nodes (42): [0.0.0-beta.1] - 2026-07-14, [0.1.0] - 2026-06-22, [0.2.0] - 2026-06-22, [0.3.0] - 2026-06-22, [0.4.0] - 2026-06-22, [0.4.1] - 2026-06-22, [0.6.1] - 2026-06-24, [0.6.2] - 2026-06-24 (+34 more)

### Community 9 - "Initializer.java"
Cohesion: 0.16
Nodes (9): MinecraftServer, Initializer, Gson, Logger, LogRedirector, Gson, JsonObject, ModAnalyzer (+1 more)

### Community 11 - "CurseForge — Variables del proyecto"
Cohesion: 0.14
Nodes (13): Changelog, CurseForge — Variables del proyecto, Descripcion del proyecto, Estructura del changelog (HTML), Flujo completo, Parámetros del upload, Proyecto, Rama (+5 more)

### Community 12 - "Flujo de trabajo — Data Miner (NeoForge)"
Cohesion: 0.17
Nodes (11): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Data Miner (NeoForge), Flujo por tarea, Idioma (+3 more)

### Community 13 - "Data Miner"
Cohesion: 0.25
Nodes (7): Commands, Configuration, Data Miner, License, Output Structure, Quick Start, Requirements

### Community 14 - "v0.0.0-beta.1 — Initial CurseForge Release"
Cohesion: 0.40
Nodes (4): Added, Changed, Fixed, v0.0.0-beta.1 — Initial CurseForge Release

### Community 15 - "v1.0.0 — First Stable Release"
Cohesion: 0.40
Nodes (4): Added, Changed, Fixed, v1.0.0 — First Stable Release

### Community 16 - "CLAUDE.md — data_miner (26.1.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — data_miner (26.1.2), Prioridad de instrucciones, Workflow del mod

### Community 17 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **61 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `Cambiado`, `Corregido`, `Añadido` (+56 more)
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `LatencyTracer` connect `.register` to `LatencyEventHandlers.java`?**
  _High betweenness centrality (0.029) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `Cambiado` to the rest of the system?**
  _61 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `.register` be split into smaller, more focused modules?**
  _Cohesion score 0.12315270935960591 - nodes in this community are weakly interconnected._
- **Should `PerformanceMonitor` be split into smaller, more focused modules?**
  _Cohesion score 0.11956521739130435 - nodes in this community are weakly interconnected._
- **Should `Changelog` be split into smaller, more focused modules?**
  _Cohesion score 0.046511627906976744 - nodes in this community are weakly interconnected._
- **Should `CurseForge — Variables del proyecto` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._