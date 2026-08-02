# CLAUDE.md — data_miner (Fabric 26.2)

Este repositorio es un mod **Fabric** del grupo `stalking-dragons/minecraft` para Minecraft 26.2.

## Workflow del mod

1. **Trabaja con `docs/WORKFLOW_DATA_MINER_26-2.md`** — es el workflow operativo de este mod, autocontenido. Léelo y síguelo. No hay que actualizarlo por sesión.
2. Reglas generales (idioma, no asumir información, no borrar sin permiso, delegación en OpenCode, prioridad de instrucciones): `../../codex-docs/reference/CLAUDE.md`. Ese archivo manda salvo que el usuario indique lo contrario.
3. On-demand (solo si la tarea lo necesita): `../../codex-docs/reference/CURSEFORGE.md` (publicar), `../../codex-docs/reference/GRAPHIFY.md` (backend LLM), `../../codex-docs/reference/REPO_SETUP.md` (crear repo). No leerlos de forma rutinaria.

## Prioridad de instrucciones

1. Petición del usuario en esta sesión.
2. Este archivo + `codex-docs/reference/CLAUDE.md`.
3. Workflow del proyecto (`docs/WORKFLOW_DATA_MINER_26-2.md`).
4. Convenciones existentes del proyecto.

## Notas del port Fabric

- Framework: Fabric (no NeoForge). Loader 0.19.3, Fabric API 0.156.0+26.2.
- Entrypoints: `DataMiner` (main) y `DataMinerClient` (client, `src/client/java`).
- Los eventos de NeoForge se sustituyen por callbacks de Fabric API (`ServerTickEvents`, `ServerEntityEvents`, `ServerChunkEvents`, `ServerLivingEntityEvents`, `ItemEvents`, `UseBlockCallback`, `AttackBlockCallback`, `CommandRegistrationCallback`).
- Configuración en `config/data_miner.json` (clase `DataMinerConfig`), no TOML.
