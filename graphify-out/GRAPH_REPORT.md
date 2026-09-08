# Graph Report - 1.21.1  (2026-09-08)

## Corpus Check
- 34 files · ~56,660 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 230 nodes · 407 edges · 21 communities
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 3 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5e306793`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- .register
- LatencyTracer
- DataMiner.java
- Initializer.java
- .recordEvent
- ModAnalyzer.java
- IssueRegistry.java
- RegistryDumper.java
- Flujo de trabajo — Data Miner (NeoForge)
- Project Variables — Data Miner (1.21.1)
- Data Miner
- [0.0.0-beta.1] - 2026-09-01
- CLAUDE.md — data_miner (26.2)
- gradlew
- PerformanceMonitor

## God Nodes (most connected - your core abstractions)
1. `LatencyTracer` - 14 edges
2. `Flujo de trabajo — Data Miner (NeoForge)` - 11 edges
3. `EventHandlers` - 10 edges
4. `CapturedAppender` - 10 edges
5. `EventTracer` - 9 edges
6. `IssueRegistry` - 9 edges
7. `PerformanceMonitor` - 9 edges
8. `RegistryDumper` - 8 edges
9. `LatencyEventHandlers` - 7 edges
10. `DataMiner` - 6 edges

## Surprising Connections (you probably didn't know these)
- `IssueRegistry` --references--> `IssueDetector`  [EXTRACTED]
  src/main/java/com/skd/data_miner/logs/issues/IssueRegistry.java → src/main/java/com/skd/data_miner/logs/issues/IssueDetector.java

## Import Cycles
- None detected.

## Communities (21 total, 0 thin omitted)

### Community 0 - ".register"
Cohesion: 0.21
Nodes (7): CommandDispatcher, CommandSourceStack, DataMinerCommands, DataMinerExecutor, EventTracer, Gson, JsonObject

### Community 1 - "LatencyTracer"
Cohesion: 0.13
Nodes (12): BlockPos, LeftClickBlock, BreakEntry, EventBusSubscriber, Pre, SubscribeEvent, LatencyEventHandlers, Gson (+4 more)

### Community 2 - "DataMiner.java"
Cohesion: 0.20
Nodes (9): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, RegisterCommandsEvent, DataMiner, Logger, ErrorCollector (+1 more)

### Community 3 - "Initializer.java"
Cohesion: 0.19
Nodes (6): AbstractAppender, LogEvent, CapturedAppender, Override, Logger, LogRedirector

### Community 4 - ".recordEvent"
Cohesion: 0.26
Nodes (11): EntityJoinLevelEvent, EntityPlaceEvent, LivingDeathEvent, Load, Player, RightClickBlock, EventHandlers, EventBusSubscriber (+3 more)

### Community 6 - "ModAnalyzer.java"
Cohesion: 0.21
Nodes (7): MinecraftServer, Initializer, Gson, Gson, JsonObject, ModAnalyzer, ModStats

### Community 7 - "IssueRegistry.java"
Cohesion: 0.16
Nodes (11): Pattern, IssueDetector, JsonObject, extract(), IssueRegistry, Gson, JsonObject, Override (+3 more)

### Community 8 - "RegistryDumper.java"
Cohesion: 0.23
Nodes (9): BooleanValue, Builder, ConfigValue, ModConfigSpec, Registry, DataMinerConfig, Gson, JsonObject (+1 more)

### Community 9 - "Flujo de trabajo — Data Miner (NeoForge)"
Cohesion: 0.17
Nodes (11): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Data Miner (NeoForge), Flujo por tarea, Idioma (+3 more)

### Community 10 - "Project Variables — Data Miner (1.21.1)"
Cohesion: 0.29
Nodes (6): Optional, Project Variables — Data Miner (1.21.1), Rama, Repo GitLab, Required, Tag

### Community 11 - "Data Miner"
Cohesion: 0.33
Nodes (5): Data Miner, License, Quick Start, Requirements, Status

### Community 12 - "[0.0.0-beta.1] - 2026-09-01"
Cohesion: 0.20
Nodes (9): [0.0.0-beta.1] - 2026-09-01, [0.0.0-beta.2] - 2026-09-02, [0.0.0-beta.3] - 2026-09-08, Added, Added, Data Miner (1.21.1) — Changelog, Fixed, Removed (+1 more)

### Community 13 - "CLAUDE.md — data_miner (26.2)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — data_miner (26.2), Prioridad de instrucciones, Workflow del mod

### Community 14 - "gradlew"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 19 - "PerformanceMonitor"
Cohesion: 0.12
Nodes (11): Post, ClientPerfHandlers, EventBusSubscriber, Pre, SubscribeEvent, EventBusSubscriber, Pre, SubscribeEvent (+3 more)

## Knowledge Gaps
- **26 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `Added`, `Removed`, `Fixed` (+21 more)
  These have ≤1 connection - possible missing edges or undocumented components.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `Added` to the rest of the system?**
  _26 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `LatencyTracer` be split into smaller, more focused modules?**
  _Cohesion score 0.12643678160919541 - nodes in this community are weakly interconnected._
- **Should `PerformanceMonitor` be split into smaller, more focused modules?**
  _Cohesion score 0.11956521739130435 - nodes in this community are weakly interconnected._