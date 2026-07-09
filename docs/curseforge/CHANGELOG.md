# CurseForge Changelog

## v0.8.1 (Beta)

- **Single session file** — Issue detections now append to one `issues.jsonl` per session instead of spawning one JSON per event. Cleaner, faster, zero clutter.

## v0.8.0 (Beta)

- **Log redirect** — WARN/ERROR noise from other mods is automatically captured and moved out of `latest.log` into DataMiner's own `logs/captured.log`. Your main log stays clean.
- **Smart issue detection** — Known problem patterns (missing refmaps, broken access transformers, missing textures, invalid pack metadata) are auto-detected and recorded as structured JSON Lines in `startup/logs/issues.jsonl`.
- **Live latency stats** — New command `/dataminer latency stats` shows eating time, block break time, and slow tick data in real time without stopping the tracer.
- **Extensible detectors** — Adding more issue patterns is trivial via the `IssueDetector` interface. No rewrites needed.

## v0.7.4

- Fixed ClassCastException crash on slow tick detection (server)

## v0.7.3

- Removed Vision Analysis (screenshots + AI API) due to privacy policy compliance

## v0.7.2

- **Slow ticks now include mod context** — each slow tick shows `entities_by_mod` breakdown to identify which mod causes lag
- **Full mod analysis** (`startup/mod_analysis.json`) — per-mod ID, version, registry counts, dependencies, and potential issues (missing deps, high entity/block counts)
- `/dataminer mods analyze` command to regenerate analysis on demand

## v0.7.1

- **Block breaking latency** — measures time from first click to block break via `LeftClickBlock` + `PlayerTickEvent`
- **Latency tracer always-on** by default (`latencyAlwaysOn: true`), no command needed
- Tracks: eating time, block breaking time, slow ticks (>50ms MSPT)

## v0.7.0

- **Latency Analyzer** — `/dataminer latency start/stop` for eating, block breaking, and slow tick measurement
- **Mod Impact Analyzer** — `startup/mod_impact.json` with blocks/items/entities per namespace
- FPS tracking moved to client-only class to prevent server crashes

## v0.6.3

- Fixed server crash on `/dataminer vision` (dedicated servers)
- API key default changed to `CHANGE_ME`

## v0.6.2

- All I/O moved to background threads — no more game freezes
- `dumpOnStartup` default set to `false`
- Commands respond instantly, processing happens async

## v0.6.1

- **Vision Analysis** — `/dataminer vision analyze` captures screenshot + AI analysis (OpenAI + Gemini)
- `/dataminer vision start/stop` for periodic captures
- Configurable API endpoint, model, system prompt, interval

## v0.4.1

- First beta release — all features tested and functional

## v0.4.0

- **Live Event Tracer** — `/dataminer events start/stop` captures player movement, block placement, damage, entity spawns/deaths, chunk loads

## v0.3.0

- Organized folder structure: `info_client_data_miner/` or `info_server_data_miner/`
- `startup/mods.json`, `startup/info.json`, `startup/registries/`
- Error collector for uncaught exceptions

## v0.2.0

- `/dataminer dump` and `/dataminer perf start/stop` commands
- Enriched JSON exports with block/item/entity properties
- FPS + MSPT performance monitoring

## v0.1.0

- Initial release — registry dumps to JSON for blocks, items, entities, and more
