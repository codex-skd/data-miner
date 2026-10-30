# Changelog

Todos los cambios notables de DataMiner se documentan en este archivo.

El formato sigue [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
y el proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
---

## [Unreleased]

### Change

- **Workflow de GitHub**: eliminado `.github/workflows/build-and-upload.yml`; ya no se publica en el snapshot público ni se ejecuta en GitHub.
- **CHANGELOG**: las entradas anteriores ya no citan ficheros de documentación interna que no forman parte del código publicado.

## [1.2.4] - 2026-10-30

### Changed

- **Variables de CurseForge**: `docs/curseforge/project_vars.md` pasa a `docs/curseforge/project.env` (formato env: `PROJECT_ID`, `GAME_VERSIONS`, `RELEASE_TYPE`, `RELATIONS`). La documentación genérica se centraliza en `codex-docs/reference/CURSEFORGE.md` y los scripts de subida y consulta leen el nuevo fichero.
- **Sin cambios en el mod**: solo documentación y tooling. Versión **exenta de subida a CurseForge** (no hay JAR nuevo que publicar); se versiona por ser un cambio de proceso importante.

## [1.2.3] - 2026-10-29

### Changed

- **Tokens de CurseForge**: el token de subida y la clave Core ya no se guardan en `docs/curseforge/project_vars.md`; se leen de las variables de entorno de usuario `API_MOD_UPLOAD_CURSEFORGE` y `API_MOD_GET_CURSEFORGE`. Los scripts de subida y consulta se han adaptado y la documentación se ha actualizado.
- **Sin cambios en el mod**: solo documentación y tooling. Versión **exenta de subida a CurseForge** (no hay JAR nuevo que publicar); se versiona por ser un cambio de proceso importante.

## [1.2.2] - 2026-09-24

### Fixed

- **Crash del cliente al arrancar**: la 1.2.1 registraba el listener de recarga de recursos desde el constructor del mod, cuando `Minecraft.getInstance()` aún es `null` en NeoForge 26.2 (`NullPointerException` en `DataMinerClientNetworking.registerReloadListener`). Ahora se registra mediante `AddClientReloadListenersEvent` en una clase `@EventBusSubscriber(value = Dist.CLIENT)`.

## [1.2.1] - 2026-08-30

### Added

- **Client reload detection & server reporting**: `data_miner` now registers a `PreparableReloadListener` on the client `ReloadableResourceManager`. When a resource reload occurs (F3+T, language change, resource pack change, or any mod-triggered reload), the client captures the stack trace and sends a `ClientReloadPayload` to the server. Server logs show: `[DataMiner] Client reload from <player> (UUID=...) at <timestamp>` with full stack trace — allowing diagnosis of "random" reloads in multiplayer.

### Change

- Updated `DataMiner.java` to register payload handlers (`RegisterPayloadHandlersEvent`) and activate reload listener client-side via `FMLEnvironment.getDist().isClient()`.

## [1.2.0] - 2026-08-23

### Added

- **Lootr missing loot table detection**: new issue detector captures "Error con mod [X], no con Lootr! The loot table for this container [ResourceKey[minecraft:loot_table / X:path]] does not exist" messages.
- **Dedicated log file**: detected issues saved to `startup/logs/lootr_missing_tables.jsonl` with `mod_id`, `loot_table`, and `timestamp` for automated datapack stub generation.
- **INFO-level capture for System logger**: captures Lootr chat messages (logged at INFO level) in addition to WARN/ERROR.

### Change

- Updated `IssueRegistry.java` with new `lootr_missing_loot_table` detector pattern.
- Updated `CapturedAppender.java` to capture Level.INFO from "System" logger.

## [1.1.0] - 2026-08-19

### Change

- **Actualización de NeoForge**: actualizado de 26.2.0.45-beta a 26.2.0.57.
- **Nombre de JAR con versión del cargador**: el artefacto ahora se compila como `data_miner-26.2-neoforge-26.2.0.57-1.1.0.jar`.
- **Documentación del workflow**: actualizada la documentación interna para reflejar la nueva rama de trabajo.

## [1.0.3] - 2026-08-17

### Change

- **Actualización de NeoForge**: actualizado de 26.2.0.37-beta a 26.2.0.45-beta.
- **Nombre de JAR con versión del cargador**: el artefacto ahora se compila como `data_miner-26.2-neoforge-26.2.0.45-beta-1.0.3.jar`.
- **Documentación del workflow**: actualizada la documentación interna para reflejar la nueva rama de trabajo.

---

## [1.0.2] - 2026-08-12

### Change

- **Nombre de JAR con versión del cargador**: el artefacto ahora se compila como `data_miner-26.2-neoforge-26.2.0.37-beta-1.0.2.jar` (se añade la versión de cargador/NeoForge al nombre del archivo). Empaquetado y documentación; sin cambios de funcionalidad.

## [1.0.1] - 2026-08-05

### Change

- **Recompilado contra NeoForge `26.2.0.37-beta`**: bump de `neo_version` en `gradle.properties` (`26.2.0.32-beta` -> `26.2.0.37-beta`). Verificado con `runServer` (arranque sin errores).

## [1.0.0] - 2026-07-27

### Cambiado

- Release estable 1.0.0. Versión probada y verificada en Minecraft 26.2.
- Actualizado WORKFLOW a v1.4.0 (convenciones de nomenclatura, organización workspace, CI/CD, Graphify).
- Estructura de proyecto reorganizada a `data_miner/26.2/`.

## [0.0.0-beta.1] - 2026-07-26

### Añadido

- Port completo desde DataMiner 26.1.2 v1.0.1 a Minecraft 26.2 / NeoForge 26.2.0.32-beta.
- Volcado de registros a JSON (blocks, items, entities, sound events, potions, etc.).
- Monitorización de rendimiento (FPS min/max/avg, MSPT min/max/avg).
- Trazado de eventos en vivo (movimiento, bloques, daño, muerte, chunks).
- Medición de latencia (comer, romper bloques, ticks lentos >50ms).
- Análisis de impacto de mods por namespace.
- Análisis completo de mods con dependencias y detección de problemas.
- Redirección de logs WARN/ERROR de terceros a `logs/captured.log`.
- Detección inteligente de incidencias (refmaps, ATs, texturas, pack.meta).
- Ejecución asíncrona de I/O para no bloquear el juego.
- 9 comandos: `/data_miner dump`, `perf start/stop`, `events start/stop`, `latency start/stop/stats`, `mods analyze`.
