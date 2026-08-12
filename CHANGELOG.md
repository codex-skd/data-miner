# Changelog

Todos los cambios notables de DataMiner se documentan en este archivo.

El formato sigue [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
y el proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
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
