# CurseForge — Variables del proyecto

## Proyecto

| Variable | Valor |
|----------|-------|
| `curseforge_project_id` | `1584390` |
| `mod_id` | `data_miner` |
| `display_name` | `Data Miner` |

## Tokens

| API | Token | Uso |
|-----|-------|-----|
| Upload | `ee776b0a-ee95-4850-b554-06be02a8657f` | Subir archivos JAR |
| Core (GET) | `$2a$10$yGwryAfmRkS9ZJsJUDf5YOKZpOIsmHB8Fji2D8JVCKBSZEKYlwmaO` | Consultar datos del mod |

Autenticación Upload: cabecera `X-Api-Token`
Autenticación Core: cabecera `x-api-key`

## Versión actual

| Variable | Valor |
|----------|-------|
| `minecraft_version` | `26.2` |
| `framework` | `neoforge` |
| `java_version` | `25` |
| `environment` | `Client`, `Server` |

## Rama

```
minecraft/26.2/neoforge-26.2.0.32-beta/production
```

## Tag

Formato: `<mc-version>-<framework>-<version>`
Ejemplo: `26.2-neoforge-1.0.0`

## Parámetros del upload

| Campo | Valor | Notas |
|-------|-------|-------|
| `displayName` | `Data Miner (1.0.0)` | Nombre visible: `display_name (version)` |
| `changelog` | HTML (no Markdown) | Ver estructura abajo |
| `changelogType` | `html` | Obligatorio para que se vea bien |
| `releaseType` | `release` | Según el tipo de versión |
| `gameVersionNames` | `["Client", "Server", "26.2", "NeoForge"]` | Entorno + MC + modloader |

## Estructura del changelog (HTML)

```html
<h2>v0.0.0-beta.1 - Initial port to Minecraft 26.2</h2>

<h3>Added</h3>
<ul>
<li><strong>Full port</strong> of DataMiner from MC 26.1.2 to MC 26.2 / NeoForge 26.2.0.32-beta.</li>
<li>All features preserved: registry dumps, performance monitor, event tracer, latency analyzer, mod analysis, log redirect, issue detection.</li>
</ul>

<hr>

<p><strong>JAR</strong>: <code>data_miner-26.2-neoforge-0.0.0-beta.1.jar</code></p>
```

## Subir archivo (JAR) con Python

```python
import json, uuid, urllib.request

boundary = uuid.uuid4().hex
version = "1.0.0"

metadata = {
    "displayName": f"Data Miner ({version})",
    "changelog": "<h2>v1.0.0 - First Stable Release</h2>",
    "changelogType": "html",
    "gameVersionNames": ["Client", "Server", "26.2", "NeoForge"],
    "releaseType": "beta"
}

with open(f"build/libs/data_miner-26.2-neoforge-{version}.jar", "rb") as f:
    jar_data = f.read()

meta_bytes = json.dumps(metadata, ensure_ascii=False).encode("utf-8")

body = b""
body += f"--{boundary}\r\n".encode()
body += b'Content-Disposition: form-data; name="metadata"\r\n'
body += b"Content-Type: application/json\r\n\r\n"
body += meta_bytes + b"\r\n"
body += f"--{boundary}\r\n".encode()
body += b'Content-Disposition: form-data; name="file"; filename="data_miner-26.2-neoforge-{version}.jar"\r\n'
body += b"Content-Type: application/java-archive\r\n\r\n"
body += jar_data + b"\r\n"
body += f"--{boundary}--\r\n".encode()

req = urllib.request.Request(
    f"https://minecraft.curseforge.com/api/projects/1584390/upload-file",
    data=body,
    headers={
        "X-Api-Token": "ee776b0a-ee95-4850-b554-06be02a8657f",
        "Content-Type": f"multipart/form-data; boundary={boundary}"
    },
    method="POST"
)

resp = urllib.request.urlopen(req)
print(resp.read().decode())
```

## Verificar con GET

```bash
curl -s "https://api.curseforge.com/v1/mods/1584390/files/<FILE_ID>" \
  -H "x-api-key: $2a$10$yGwryAfmRkS9ZJsJUDf5YOKZpOIsmHB8Fji2D8JVCKBSZEKYlwmaO"
```

## Changelog

```bash
curl -s "https://api.curseforge.com/v1/mods/1584390/files/<FILE_ID>/changelog" \
  -H "x-api-key: $2a$10$yGwryAfmRkS9ZJsJUDf5YOKZpOIsmHB8Fji2D8JVCKBSZEKYlwmaO"
```

## Descripcion del proyecto

No hay endpoint API para actualizar la descripcion. Se edita manualmente desde la web de CurseForge pegando el HTML de `docs/curseforge/project_description.md`.

## Flujo completo

1. `./gradlew clean build`
2. Actualizar `docs/curseforge/versions/<version>.md` con HTML
3. Actualizar `CHANGELOG.md`
4. `git commit -m "fix: descripcion\n\nvX.Y.Z"` + `git push`
5. `git tag -a 26.2-neoforge-<version> -m "vX.Y.Z: descripcion"` + `git push origin <tag>`
6. Subir JAR a CurseForge con Python
7. Verificar con GET que el changelog se vea bien
8. Liberar manualmente desde la web si es necesario
