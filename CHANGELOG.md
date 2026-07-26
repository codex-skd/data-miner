# Changelog

Todos los cambios notables de DataMiner se documentan en este archivo.

El formato sigue [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
y el proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
