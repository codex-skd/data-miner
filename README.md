# DataMiner

Mod de diagnóstico y profiling para Minecraft. Exporta todos los registros del juego a JSON, monitoriza FPS/MSPT, rastrea eventos en vivo, mide latencia de acciones, analiza el impacto de mods, redirige logs ruidosos de terceros y detecta problemas conocidos — todo sin bloquear el hilo del juego.

## Requisitos

- Minecraft **26.1.2**
- NeoForge **26.1.2.76**+
- Java **25**

## Inicio rápido

1. Coloca `dataminer-*.jar` en `mods/`
2. Inicia el juego
3. DataMiner crea su estructura de carpetas al arrancar (async, sin congelaciones)
4. El trazador de latencia se activa automáticamente por defecto (`latencyAlwaysOn: true`)

## Comandos

| Comando | Descripción |
|---|---|
| `/dataminer dump` | Exporta todos los registros a JSON (segundo plano) |
| `/dataminer mods analyze` | Regenera el análisis completo de mods |
| `/dataminer perf start` | Inicia monitorización de FPS + MSPT |
| `/dataminer perf stop` | Detiene y guarda el informe de rendimiento |
| `/dataminer events start` | Traza acciones de jugador, entidades, chunks |
| `/dataminer events stop` | Detiene y guarda el informe de eventos |
| `/dataminer latency start` | Inicia medición de latencia (comer/romper/tick) |
| `/dataminer latency stop` | Detiene y guarda el informe de latencia |
| `/dataminer latency stats` | Muestra estadísticas de latencia en vivo sin detener |

## Estructura de salida

```
info_client_data_miner/  (o info_server_data_miner/)
├── startup/
│   ├── registries/          # Todos los registros del juego en JSON
│   ├── logs/
│   │   ├── captured.log     # WARN/ERROR redirigidos de otros mods
│   │   └── issues.jsonl     # Problemas detectados como JSON Lines
│   ├── mods.json            # Mods cargados con versiones/deps
│   ├── mod_impact.json      # Blocks/items/entities por namespace
│   ├── mod_analysis.json    # Perfil por mod con posibles problemas
│   ├── info.json            # Versión MC, Java, SO, RAM, locale
│   └── errors/              # Excepciones de arranque
├── performance/
│   ├── *.json               # Informes de sesión FPS/MSPT
│   └── latency/
│       └── *.json           # Informes de latencia (comer/romper/tick)
├── events/
│   ├── *.json               # Trazas de eventos en vivo
│   └── errors/              # Excepciones de manejadores de eventos
```

## Configuración

Archivo de configuración: `config/dataminer-common.toml`

| Clave | Por defecto | Descripción |
|---|---|---|
| `latencyAlwaysOn` | `true` | Ejecuta el trazador de latencia automáticamente |
| `dumpOnStartup` | `false` | Exporta registros al iniciar el juego |

## Estructura del proyecto

```
src/main/java/com/skd/dataminer/
├── DataMiner.java              # @Mod punto de entrada
├── DataMinerConfig.java        # Configuración NeoForge
├── DataMinerExecutor.java      # Pool de hilos para I/O en segundo plano
├── init/
│   └── Initializer.java        # Estructura de carpetas + JSONs de arranque
├── error/
│   └── ErrorCollector.java     # Manejador de excepciones no capturadas
├── command/
│   └── DataMinerCommands.java  # Todos los subcomandos /dataminer
├── dumper/
│   └── RegistryDumper.java     # Iteración de registros + JSON enriquecido
├── perf/
│   ├── PerformanceMonitor.java # Seguimiento de FPS + MSPT
│   └── PerfEventHandlers.java  # Hooks de ticks del servidor
├── event/
│   ├── EventTracer.java        # Colección de eventos en vivo
│   └── EventHandlers.java      # Hooks de eventos del juego
├── latency/
│   ├── LatencyTracer.java      # Trazador de latencia de acciones
│   ├── LatencyEventHandlers.java # Hooks de comer + romper bloques
│   └── ClientPerfHandlers.java # Captura de FPS del cliente
├── logs/
│   ├── CapturedAppender.java   # Appender Log4j2 para captura de logs
│   ├── LogRedirector.java      # Registro del appender + configuración de filtros
│   └── issues/
│       ├── IssueDetector.java  # Interfaz de detectores de patrones
│       └── IssueRegistry.java  # Registro de patrones + escritor JSONL
├── modimpact/
│   └── ModAnalyzer.java        # Impacto de registros + contexto del mundo
└── mixin/
```

## Licencia

All Rights Reserved.
