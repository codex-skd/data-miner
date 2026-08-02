# Graph Report - .  (2026-08-02)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 173 nodes · 356 edges · 15 communities (14 shown, 1 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 2 edges (avg confidence: 0.8)
- Token cost: 646 input · 136 output

## Graph Freshness
- Built from commit: `6010ef9a`
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

## God Nodes (most connected - your core abstractions)
1. `LatencyTracer` - 14 edges
2. `EventHandlers` - 9 edges
3. `EventTracer` - 9 edges
4. `PerformanceMonitor` - 9 edges
5. `RegistryDumper` - 8 edges
6. `CapturedAppender` - 8 edges
7. `LatencyEventHandlers` - 7 edges
8. `Initializer` - 6 edges
9. `IssueDetector` - 5 edges
10. `IssueRegistry` - 5 edges

## Surprising Connections (you probably didn't know these)
- `IssueRegistry` --references--> `IssueDetector`  [EXTRACTED]
  src/main/java/com/skd/data_miner/logs/issues/IssueRegistry.java → src/main/java/com/skd/data_miner/logs/issues/IssueDetector.java

## Import Cycles
- None detected.

## Communities (15 total, 1 thin omitted)

### Community 0 - "Data Mining"
Cohesion: 0.15
Nodes (9): CommandDispatcher, CommandSourceStack, DataMinerCommands, DataMinerExecutor, EventTracer, Gson, JsonObject, Gson (+1 more)

### Community 1 - "Server Performance"
Cohesion: 0.15
Nodes (6): MinecraftServer, Gson, JsonObject, LatencyTracer, MinecraftServer, PerfEventHandlers

### Community 2 - "Mod Initialization"
Cohesion: 0.16
Nodes (9): Initializer, Gson, Logger, LogRedirector, Gson, JsonObject, MinecraftServer, ModAnalyzer (+1 more)

### Community 3 - "Event Handling"
Cohesion: 0.32
Nodes (9): Block, DamageSource, Entity, LivingEntity, EventHandlers, BlockPos, JsonObject, MinecraftServer (+1 more)

### Community 4 - "Issue Detection"
Cohesion: 0.18
Nodes (10): Pattern, IssueDetector, JsonObject, extract(), IssueRegistry, Gson, JsonObject, Override (+2 more)

### Community 5 - "Latency Events"
Cohesion: 0.35
Nodes (6): InteractionHand, BreakEntry, EatEntry, BlockPos, Player, LatencyEventHandlers

### Community 6 - "Log Capturing"
Cohesion: 0.29
Nodes (4): AbstractAppender, LogEvent, CapturedAppender, Override

### Community 7 - "Mod Initialization (Client)"
Cohesion: 0.27
Nodes (6): ModInitializer, DataMiner, Logger, Override, ErrorCollector, Gson

### Community 8 - "Registry Dumping"
Cohesion: 0.44
Nodes (4): Registry, Gson, JsonObject, RegistryDumper

### Community 9 - "Client Performance"
Cohesion: 0.24
Nodes (4): ClientModInitializer, DataMinerClient, Override, ClientPerfHandlers

### Community 10 - "Config Management"
Cohesion: 0.53
Nodes (3): ConfigData, DataMinerConfig, Gson

### Community 11 - "Build Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **1 isolated node(s):** `Icon for Data Miner mod`
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PerformanceMonitor` connect `Data Mining` to `Client Performance`, `Server Performance`?**
  _High betweenness centrality (0.060) - this node is a cross-community bridge._
- **What connects `Icon for Data Miner mod` to the rest of the system?**
  _1 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Data Mining` be split into smaller, more focused modules?**
  _Cohesion score 0.14624505928853754 - nodes in this community are weakly interconnected._
- **Should `Server Performance` be split into smaller, more focused modules?**
  _Cohesion score 0.14624505928853754 - nodes in this community are weakly interconnected._