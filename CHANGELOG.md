# Changelog

Todos los cambios notables de DataMiner se documentan en este archivo.

El formato sigue [Keep a Changelog](https://keepachangelog.com/en/1.1.0/)
y el proyecto adhiere a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.0-beta.1] - 2026-07-14

### Añadido

- **Redirección de logs**: WARN/ERROR de mods de terceros se capturan mediante un appender personalizado de Log4j2 y se redirigen de `latest.log` a `logs/captured.log`. El appender también añade un filtro DENY al RollingFile appender para que esos mensajes no sigan apareciendo en el log principal.
- **Detección inteligente de incidencias**: `IssueRegistry` detecta automáticamente patrones conocidos (refmaps faltantes, access transformers rotos, texturas faltantes, errores de pack.meta) y guarda JSON Lines estructurados en `startup/logs/issues.jsonl`. Extensible mediante la interfaz `IssueDetector`.
- **Estadísticas de latencia en vivo**: Comando `/dataminer latency stats` que muestra estadísticas en tiempo real de comida/rotura de bloques/ticks lentos sin necesidad de detener el trazador.

### Corregido

- **Explosión de archivos de incidencias**: Cambiado de un JSON por evento a un único `issues.jsonl` que se va añadiendo durante la sesión.

### Cambiado

- Versionado alineado al esquema `0.0.0-beta.X`.
- Documentación reestructurada según `WORKFLOW.md`: `docs/curseforge/project_description.md`, `docs/curseforge/versions/`, `CHANGELOG.md`.

## [0.7.4] - 2026-06-29

### Corregido

- **Caída del servidor** (ClassCastException: JsonArray no puede convertirse a JsonObject) en PerfEventHandlers.onServerTickPost al detectar ticks lentos. Se eliminó la llamada incorrecta `.getAsJsonObject("entities_by_namespace")`.

## [0.7.3] - 2026-06-29

### Eliminado

- Módulo Vision Analysis (ScreenCapture.java, VisionAnalyzer.java) eliminado por cumplimiento de la política de privacidad de CurseForge.
- Todos los comandos relacionados con visión, opciones de configuración y directorios de salida eliminados.

## [0.7.2] - 2026-06-25

### Añadido

- **Contexto de ticks lentos por mod**: cada tick lento incluye ahora `entities_by_mod` mostrando qué mods tienen entidades cargadas, permitiendo identificar el mod que causa el lag.
- **Análisis completo de mods** (`startup/mod_analysis.json`): informe por mod con ID, versión, recuentos de registros, dependencias y **posibles problemas** (deps faltantes, recuentos altos de entidades/bloques/items).
- Comando `/dataminer mods analyze` para regenerar el análisis de mods bajo demanda.

### Cambiado

- `LatencyTracer.recordSlowTick()` acepta parámetro de contexto de mod.
- `PerfEventHandlers` integra `ModAnalyzer.snapshotWorldContext()` durante ticks lentos.

## [0.7.1] - 2026-06-25

### Añadido

- **Seguimiento de latencia de rotura de bloques** — mediante `LeftClickBlock` + sondeo en `PlayerTickEvent` (transición bloque → aire). Ya no depende de `BlockEvent.BreakEvent`.
- **Config `latencyAlwaysOn`** — el trazador de latencia se ejecuta automáticamente por defecto. Se puede desactivar mediante configuración o usar `/dataminer latency start/stop`.

### Cambiado

- Se añadió `LatencyTracer.ensureRunning()` para auto-arranque.

## [0.7.0] - 2026-06-25

### Añadido

- **Analizador de Latencia** — `/dataminer latency start/stop` mide retardos reales de acciones.
  - Latencia al comer: delta `LivingEntityUseItemEvent.Start` → `Stop`.
  - Detección de ticks lentos: MSPT > 50ms registrado con recuentos de entidades/chunks.
  - Salida: `performance/latency/*_latency.json` con min/max/avg por categoría.
  - `LatencyTracer.java` y `LatencyEventHandlers.java`.
- **Analizador de Impacto de Mods** — `ModAnalyzer.java` genera `startup/mod_impact.json`.
  - Bloques, items, tipos de entidad por namespace, ordenados por total de entradas.
  - `ModAnalyzer.snapshotWorldContext()` para recuentos de entidades/chunks por namespace de mod en tiempo de ejecución.
- Seguimiento de FPS movido a `ClientPerfHandlers` (`@EventBusSubscriber(Dist.CLIENT)`) para evitar caídas del servidor.

## [0.6.3] - 2026-06-25

### Corregido

- **Caída del servidor**: `/dataminer vision` ya no lanza `NoClassDefFoundError` en servidores dedicados. Se añadió guarda `VisionAnalyzer.isClientReady()`.
- Clave API por defecto cambiada a `CHANGE_ME` (era una clave real hardcodeada).

## [0.6.2] - 2026-06-24

### Corregido

- **Congelación del juego corregida**: todas las operaciones pesadas de I/O (volcados de registros, guardado de informes) ahora se ejecutan en hilos en segundo plano mediante `DataMinerExecutor`, sin bloquear el hilo del juego.
- `DUMP_ON_STARTUP` cambiado a `false` por defecto para evitar congelación al arrancar.
- Los comandos responden inmediatamente y procesan el trabajo de forma asíncrona; el progreso se registra en la consola.
- `Initializer.init()` y `RegistryDumper.dumpAll()` movidos fuera del hilo principal al arrancar.
- Todos los comandos `/dataminer * stop` devuelven respuesta inmediata, el guardado ocurre en segundo plano.
- `/dataminer dump` devuelve respuesta inmediata, el volcado ocurre en segundo plano.

## [0.6.1] - 2026-06-24

### Añadido

- **Vision Analysis** — captura de pantalla + inspección visual mediante IA.
  - `/dataminer vision analyze` captura la pantalla y la envía a una API de IA.
  - `/dataminer vision start/stop` para captura y análisis periódico automatizado.
  - `ScreenCapture.java` usando `Screenshot.grab` de vanilla para captura del framebuffer.
  - `VisionAnalyzer.java` con soporte de API dual: OpenAI-compatible y Google Gemini.
  - Capturas guardadas en `vision/screenshots/`, análisis en `vision/analyses/`.
  - Contexto del jugador incluido en cada análisis (posición, dimensión, salud, comida, FPS).
  - Intervalo de captura, endpoint de API, modelo, system prompt y clave API configurables.
- Opción de configuración `visionApiType` para cambiar entre formatos de API `"openai"` y `"gemini"`.
- Directorios `vision/` creados automáticamente al arrancar por `Initializer.java`.

## [0.4.1] - 2026-06-22

### Cambiado

- Primera beta. Todas las funcionalidades probadas y operativas.
- Tipo de lanzamiento promocionado de Alpha a Beta.

## [0.4.0] - 2026-06-22

### Añadido

- Comandos `/dataminer events start` y `/dataminer events stop` para trazado de eventos del juego en vivo.
- `EventTracer.java` recolectando eventos en memoria y exportándolos a JSON con timestamp en `events/`.
- `EventHandlers.java` con hooks para:
  - `PlayerTickEvent` — posición del jugador, sprinting, sneaking, salud, comida, bioma, dimensión (muestreado cada 1s).
  - `BlockEvent.EntityPlaceEvent` — bloque colocado con posición y dimensión.
  - `PlayerInteractEvent.RightClickBlock` — interacción con bloque con posición y mano.
  - `LivingDamageEvent.Pre` — daño a jugadores con cantidad y entidad fuente.
  - `LivingDeathEvent` — muertes de entidades con posición, tipo, dimensión.
  - `EntityJoinLevelEvent` — entidades entrando al mundo.
  - `ChunkEvent.Load` — carga de chunks con coordenadas y dimensión.
- Errores de eventos auto-capturados en `events/errors/` con stack traces.
- El JSON del informe de eventos incluye `total_events`, `total_errors` y todos los datos de eventos capturados.

## [0.3.0] - 2026-06-22

### Añadido

- Inicialización de estructura de carpetas al arrancar (`Initializer.java`).
- `info_client_data_miner/` o `info_server_data_miner/` con subdirectorios organizados.
- `startup/mods.json` listando todos los mods cargados con IDs, versiones y dependencias.
- `startup/info.json` con versión de MC, Java, SO, RAM, locale e información de lado.
- `startup/registries/` para todos los volcados de registros (movido de `dataminer_dumps`).
- `startup/errors/` para errores capturados durante el arranque.
- `events/errors/` placeholder para registro de errores de eventos en juego.
- `ErrorCollector.java` capturando excepciones no capturadas mediante `Thread.setDefaultUncaughtExceptionHandler`.
- Informes de rendimiento ahora guardados con nombres de archivo con timestamp en carpeta `performance/`.

### Cambiado

- Volcados de registros movidos a subdirectorio `startup/registries/`.
- Informes de rendimiento movidos a subdirectorio `performance/` con nombres con timestamp.

## [0.2.0] - 2026-06-22

### Añadido

- Comando `/dataminer dump` para activar volcado de registros manualmente.
- Comandos `/dataminer perf start` y `/dataminer perf stop` para monitorización de rendimiento.
- `PerformanceMonitor.java` monitorizando FPS (min/max/avg) y MSPT (min/max/avg) durante las sesiones.
- `PerfEventHandlers.java` con `ClientTickEvent` para FPS y `ServerTickEvent` para MSPT.
- `blocks.json` enriquecido con `hardness`, `blast_resistance`, `light_emission`, `has_block_entity` y `sound_type` (volume, pitch, break/step/place/hit/fall sounds).
- `items.json` enriquecido con `max_stack_size`, `max_damage`, `rarity` y `food_properties` (nutrition, saturation, can_always_eat).
- `entities.json` enriquecido con `width`, `height`, `category`, `fire_immune`, `can_summon`, `client_tracking_range`, `update_interval`, `description_id`.
- Informe de rendimiento guardado en `dataminer_dumps/performance.json`.

### Eliminado

- Biomas, encantamientos y tipos de dimensión de la lista de volcado (no accesibles mediante `BuiltInRegistries` en esta versión de Minecraft; se readicionarán mediante acceso dinámico a registros en una versión futura).

## [0.1.0] - 2026-06-22

### Añadido

- Configuración inicial del proyecto con NeoForge MDK para Minecraft 26.1.2.
- `DataMiner.java` clase principal del mod con anotación `@Mod` y listener `FMLCommonSetupEvent`.
- `DataMinerConfig.java` con opciones de activación por registro y opción de volcado automático.
- `RegistryDumper.java` que itera `BuiltInRegistries` y exporta cada registro a un archivo JSON.
- Volcado de salida al directorio `dataminer_dumps/` (configurable).
- Registros soportados: blocks, items, entity types, mob effects, sound events, creative mode tabs, potions, villager professions, attributes.
- Archivo de idioma `en_us.json` con futuras cadenas de comandos.
- Placeholder de configuración Mixin para futuros hooks de mixin.
