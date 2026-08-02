# Graph Report - 26.2  (2026-08-02)

## Corpus Check
- 36 files · ~9,982 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 224 nodes · 398 edges · 23 communities (22 shown, 1 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 2 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `8306a582`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Data Mining
- Server Performance
- Mod Initialization
- Event Handling
- Issue Detection
- Latency Events
- Log Capturing
- Mod Initialization (Client)
- Registry Dumping
- Client Performance
- Config Management
- Build Script
- Mod Icon
- Flujo de trabajo — Data Miner (Fabric)
- Changelog
- Data Miner (Fabric)
- CLAUDE.md — data_miner (Fabric 26.2)

## God Nodes (most connected - your core abstractions)
1. `LatencyTracer` - 14 edges
2. `CurseForge — Variables del proyecto` - 13 edges
3. `Flujo de trabajo — Data Miner (Fabric)` - 11 edges
4. `EventHandlers` - 9 edges
5. `EventTracer` - 9 edges
6. `PerformanceMonitor` - 9 edges
7. `RegistryDumper` - 8 edges
8. `CapturedAppender` - 8 edges
9. `LatencyEventHandlers` - 7 edges
10. `Data Miner (Fabric)` - 7 edges

## Surprising Connections (you probably didn't know these)
- `IssueRegistry` --references--> `IssueDetector`  [EXTRACTED]
  src/main/java/com/skd/data_miner/logs/issues/IssueRegistry.java → src/main/java/com/skd/data_miner/logs/issues/IssueDetector.java

## Import Cycles
- None detected.

## Communities (23 total, 1 thin omitted)

### Community 0 - "Data Mining"
Cohesion: 0.39
Nodes (3): EventTracer, Gson, JsonObject

### Community 1 - "Server Performance"
Cohesion: 0.13
Nodes (9): CommandDispatcher, CommandSourceStack, DataMinerCommands, DataMinerExecutor, Gson, JsonObject, LatencyTracer, MinecraftServer (+1 more)

### Community 2 - "Mod Initialization"
Cohesion: 0.16
Nodes (9): Initializer, Gson, Logger, LogRedirector, Gson, JsonObject, MinecraftServer, ModAnalyzer (+1 more)

### Community 3 - "Event Handling"
Cohesion: 0.32
Nodes (9): Block, DamageSource, Entity, LivingEntity, EventHandlers, BlockPos, JsonObject, MinecraftServer (+1 more)

### Community 4 - "Issue Detection"
Cohesion: 0.11
Nodes (14): AbstractAppender, LogEvent, Pattern, CapturedAppender, Override, IssueDetector, JsonObject, extract() (+6 more)

### Community 5 - "Latency Events"
Cohesion: 0.31
Nodes (7): InteractionHand, BreakEntry, EatEntry, BlockPos, MinecraftServer, Player, LatencyEventHandlers

### Community 6 - "Log Capturing"
Cohesion: 0.14
Nodes (13): Changelog, CurseForge — Variables del proyecto, Descripcion del proyecto, Estructura del changelog (HTML), Flujo completo, Parámetros del upload, Proyecto, Rama (+5 more)

### Community 7 - "Mod Initialization (Client)"
Cohesion: 0.27
Nodes (6): ModInitializer, DataMiner, Logger, Override, ErrorCollector, Gson

### Community 8 - "Registry Dumping"
Cohesion: 0.44
Nodes (4): Registry, Gson, JsonObject, RegistryDumper

### Community 9 - "Client Performance"
Cohesion: 0.15
Nodes (6): ClientModInitializer, DataMinerClient, Override, ClientPerfHandlers, Gson, PerformanceMonitor

### Community 10 - "Config Management"
Cohesion: 0.53
Nodes (3): ConfigData, DataMinerConfig, Gson

### Community 11 - "Build Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 15 - "Flujo de trabajo — Data Miner (Fabric)"
Cohesion: 0.17
Nodes (11): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Data Miner (Fabric), Flujo por tarea, Idioma (+3 more)

### Community 16 - "Changelog"
Cohesion: 0.25
Nodes (7): [0.0.0-beta.1] - 2026-08-02, [0.0.0-beta.2] - 2026-08-02, [1.0.0] - 2026-08-02, Añadido, Cambiado, Cambiado, Changelog

### Community 17 - "Data Miner (Fabric)"
Cohesion: 0.25
Nodes (7): Commands, Configuration, Data Miner (Fabric), License, Output Structure, Quick Start, Requirements

### Community 18 - "CLAUDE.md — data_miner (Fabric 26.2)"
Cohesion: 0.40
Nodes (4): CLAUDE.md — data_miner (Fabric 26.2), Notas del port Fabric, Prioridad de instrucciones, Workflow del mod

## Knowledge Gaps
- **35 isolated node(s):** `Workflow del mod`, `Prioridad de instrucciones`, `Notas del port Fabric`, `Cambiado`, `Cambiado` (+30 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **What connects `Workflow del mod`, `Prioridad de instrucciones`, `Notas del port Fabric` to the rest of the system?**
  _35 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Server Performance` be split into smaller, more focused modules?**
  _Cohesion score 0.12561576354679804 - nodes in this community are weakly interconnected._
- **Should `Issue Detection` be split into smaller, more focused modules?**
  _Cohesion score 0.11375661375661375 - nodes in this community are weakly interconnected._
- **Should `Log Capturing` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._