<h1 align="center">&#128269; Data Miner</h1>

<p align="center"><strong>Comprehensive diagnostic and profiling mod &mdash; no game-thread blocking.</strong></p>

<p align="center">
<img src="https://img.shields.io/badge/loader-NeoForge-orange?style=plastic&logo=curseforge" alt="NeoForge">
<img src="https://img.shields.io/badge/minecraft-26.2%20%7C%201.21.1-blue?style=plastic" alt="Minecraft 26.2 and 1.21.1">
<img src="https://img.shields.io/badge/side-client%20%2B%20server-brightgreen?style=plastic" alt="Client and Server">
<img src="https://img.shields.io/badge/tool-diagnostic-lightgrey?style=plastic" alt="Diagnostic tool">
</p>

<br>

---

<br>

<h2>&#10024; Overview</h2>

<table>
<tr>
<td width="65%">
<p>Data Miner dumps every game registry to enriched JSON, monitors FPS and MSPT, traces live game events, measures action latency (eating, block breaking, slow ticks), profiles mod impact with per-mod analysis, redirects noisy third-party logs, and detects known mod issues &mdash; all on background threads, without blocking the game thread.</p>

<p>An original mod by <strong>Stalking Dragons</strong>. It is a developer / pack-maker diagnostic tool &mdash; it adds no gameplay content.</p>
</td>
<td width="35%" align="center">
<a href="https://codex.skdragons.com/" target="_blank"><img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="160"></a>
</td>
</tr>
</table>

<br>

<h2>&#127919; Features</h2>

<h3>&#128203; Log Redirect</h3>
<p>WARN/ERROR messages from other mods are captured and moved from <code>latest.log</code> to Data Miner's own <code>logs/captured.log</code>, keeping the main log clean.</p>

<h3>&#129504; Smart Issue Detection</h3>
<p>Detects known patterns &mdash; missing refmaps, access transformers, missing textures, pack.meta errors &mdash; and saves structured data as JSON Lines to <code>startup/logs/issues.jsonl</code>.</p>

<h3>&#128449;&#65039; Registry Dumps</h3>
<p>All built-in registries exported to JSON with detailed properties: hardness, food values, sounds, dimensions, and more.</p>

<h3>&#128202; Performance Monitor</h3>
<p>Track FPS (min/max/avg) and MSPT via <code>/data_miner perf start/stop</code>. Each slow tick includes an entities-by-mod breakdown to identify which mod causes lag.</p>

<h3>&#9201;&#65039; Latency Analyzer</h3>
<p>Automatically tracks eating time, block-breaking time and slow ticks (&gt;50 ms MSPT). View live stats with <code>/data_miner latency stats</code>.</p>

<h3>&#128230; Full Mod Analysis</h3>
<p><code>startup/mod_analysis.json</code> with per-mod ID, version, registry counts, dependencies and potential issues (missing deps, high entity counts).</p>

<h3>&#128308; Event Tracer</h3>
<p>Live game events: player movement, block placement, damage, entity spawns/deaths, chunk loads.</p>

<h3>&#9888;&#65039; Error Collection</h3>
<p>All uncaught exceptions logged with full stack traces to <code>startup/errors/</code> and <code>events/errors/</code>.</p>

<h3>&#9889; Async I/O</h3>
<p>All file operations run on background threads &mdash; zero game freezes.</p>

<br>

<h2>&#129521; Mod Structure</h2>

<table>
<tr><th align="left">Area</th><th align="left">What it provides</th></tr>
<tr><td><code>dumper</code></td><td>The registry-to-JSON exporter.</td></tr>
<tr><td><code>perf</code> / <code>latency</code></td><td>FPS/MSPT monitoring and the action-latency tracer.</td></tr>
<tr><td><code>event</code></td><td>The live event tracer.</td></tr>
<tr><td><code>logs</code></td><td>Log capture/redirect and the known-issue detector.</td></tr>
<tr><td><code>modimpact</code></td><td>Per-mod registry-impact and full-analysis reports.</td></tr>
<tr><td><code>command</code> / networking</td><td>The <code>/data_miner</code> command tree and the client&#8594;server reload probe.</td></tr>
</table>

<br>

<h2>&#128203; Requirements</h2>

<table>
<tr><td><strong>Minecraft / NeoForge / Java</strong></td><td>see <em>Available Versions</em> below</td></tr>
<tr><td><strong>Dependencies</strong></td><td>None</td></tr>
<tr><td><strong>Side</strong></td><td>Client and Server</td></tr>
</table>

<br>

<h2>&#128230; Available Versions</h2>

<table>
<tr><th align="left">Minecraft</th><th align="left">NeoForge</th><th align="left">Java</th><th align="left">Latest build</th><th align="left">Status</th></tr>
<tr><td>26.2</td><td>26.2.0.57+</td><td>25</td><td><code>1.2.1</code></td><td>Stable</td></tr>
<tr><td>1.21.1</td><td>21.1.249+</td><td>21</td><td><code>1.0.0</code></td><td>Stable</td></tr>
</table>

<p><em>Both versions share this CurseForge project. Pick the file that matches your Minecraft version.</em></p>

<br>

<h2>&#127918; Commands</h2>

<ul>
<li><code>/data_miner dump</code> &mdash; export all registries to JSON (async)</li>
<li><code>/data_miner mods analyze</code> &mdash; regenerate the full mod analysis</li>
<li><code>/data_miner perf start/stop</code> &mdash; FPS + MSPT monitoring</li>
<li><code>/data_miner events start/stop</code> &mdash; live event tracing</li>
<li><code>/data_miner latency start/stop</code> &mdash; latency measurement</li>
<li><code>/data_miner latency stats</code> &mdash; show live latency stats</li>
</ul>

<br>

---

<br>

<h2>&#128591; Credits</h2>

<p>Developed by <strong>Stalking Dragons</strong>.</p>

<br>
<br>

<p align="center">
  <a href="https://codex.skdragons.com/" target="_blank">
    <img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="200">
  </a>
  <br>
  <a href="https://codex.skdragons.com/">https://codex.skdragons.com/</a>
  <br>
  <em>Codex Stalking Dragons &mdash; Minecraft Modding</em>
</p>
