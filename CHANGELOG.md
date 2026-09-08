# Data Miner (1.21.1) — Changelog

Branch `minecraft/1.21.1/neoforge-21.1.249/production`. History independent of the 26.2 branch.

## [0.0.0-beta.3] - 2026-09-08

### Added

- **Spanish (`es_es`) locale**: full translation of all 6 command-feedback keys
  (`/dataminer dump` and `/dataminer perf` messages). Taken from the Mystical Realms
  Translation & Fixes resource-pack QA pass so it ships with the mod. No code change.

## [0.0.0-beta.2] - 2026-09-02

### Removed

- **Client reload detection with server reporting.** The client `PreparableReloadListener` that
  logged a stack trace and sent a `ClientReloadPayload` to the server on every resource reload,
  plus its payload type and server handler (`DataMinerClientNetworking`,
  `DataMinerServerNetworking`, `DataMinerNetworkPayloads`), have been deleted.

### Fixed

- **Client freeze / extremely slow resource-pack reload (F3+T).** `CapturedAppender` now hands
  formatted lines to a bounded queue drained by a single daemon writer thread with one flush per
  batch, instead of `flush()`-ing under a lock on every WARN/ERROR record on the logging thread.
  `IssueRegistry` file writes moved off the logging thread onto their own daemon writer. Under a
  large modpack's reload the logging threads no longer serialize on per-line disk I/O.

- **Server tick cost while the latency tracer is running.** `PerfEventHandlers` now captures the
  all-entities / all-chunks slow-tick snapshot at most once per second instead of on every slow
  tick (which, on an overloaded server, meant every tick).

## [0.0.0-beta.1] - 2026-09-01

### Added

- **Initial port to Minecraft 1.21.1 / NeoForge 21.1.249** (Java 21). API port of the 26.2 source
  (22 classes, no mixins, no dependencies). Full diagnostic toolkit unchanged: registry dumps,
  FPS/MSPT monitor, event tracer, latency analyzer, per-mod impact analysis, log redirect,
  known-issue detection, error collection — all async.

### Technical

- `net.minecraft.resources.Identifier` → `ResourceLocation` (~40 sites);
  `FMLEnvironment.getDist()` → `FMLEnvironment.dist`;
  `SharedConstants.getCurrentVersion().name()` → `.getName()`;
  the client resource-reload-listener anonymous class reworked to the 1.21.1
  `PreparableReloadListener.reload(PreparationBarrier, ResourceManager, ProfilerFiller,
  ProfilerFiller, Executor, Executor)` signature.
- **Bug fixed** (also present on 26.2): `Initializer.isClientSide()` detected the client by
  `Class.forName("net.minecraft.client.Minecraft")`. Under NeoForge's `RuntimeDistCleaner` that
  throws a `RuntimeException` (not `ClassNotFoundException`) on the dedicated server, aborting the
  async startup thread. Now uses `FMLEnvironment.dist.isClient()`.
- Build: `net.neoforged.moddev` template retargeted to NeoForge 21.1.249 / Java 21;
  `modLoader`/`loaderVersion` added to `neoforge.mods.toml`; `mixins.json` `JAVA_21`
  (mixin list is empty — the mod uses none).
- Verified: `./gradlew build` OK; `./gradlew runServer` → `Done (4.8s)`, 0 FATAL, DataMiner
  generates `mods.json` / `info.json` / `mod_impact.json` / `mod_analysis.json` with no errors.
