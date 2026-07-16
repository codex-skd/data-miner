<h1 align="center">🔍 Data Miner</h1>

<p align="center"><strong>Comprehensive diagnostic and profiling mod for Minecraft — no game-thread blocking.</strong></p>

<br>

---

<br>

<h2>✨ Overview</h2>

<p>Data Miner dumps every game registry to enriched JSON, monitors FPS and MSPT, traces live game events, measures action latency (eating, block breaking, slow ticks), profiles mod impact with per-mod analysis, redirects noisy third-party logs, and detects known mod issues — all without blocking the game thread.</p>

<br>

<h2>🎯 Features</h2>

<h3>📋 Log Redirect</h3>
<p>WARN/ERROR messages from other mods are automatically captured and redirected from <code>latest.log</code> to Data Miner's own <code>logs/captured.log</code>, keeping the main log clean.</p>

<h3>🧠 Smart Issue Detection</h3>
<p>Automatically detects known patterns — missing refmaps, access transformers, missing textures, pack.meta errors — and saves structured data as JSON Lines to <code>startup/logs/issues.jsonl</code>.</p>

<h3>🗂️ Registry Dumps</h3>
<p>All BuiltInRegistries exported to JSON with detailed properties: hardness, food values, sounds, dimensions, and more.</p>

<h3>📊 Performance Monitor</h3>
<p>Track FPS (min/max/avg) and MSPT via <code>/dataminer perf start/stop</code>. Each slow tick includes an entities_by_mod breakdown to identify which mod causes lag.</p>

<h3>⏱️ Latency Analyzer</h3>
<p>Automatically tracks eating time, block breaking time, and slow ticks (&gt;50ms MSPT). Runs by default — no command needed. View live stats with <code>/dataminer latency stats</code>.</p>

<h3>📦 Full Mod Analysis</h3>
<p><code>startup/mod_analysis.json</code> with per-mod ID, version, registry counts, dependencies, and potential issues (missing deps, high entity counts).</p>

<h3>🔴 Event Tracer</h3>
<p>Live game events: player movement, block placement, damage, entity spawns/deaths, chunk loads.</p>

<h3>⚠️ Error Collection</h3>
<p>All uncaught exceptions logged with full stack traces to <code>startup/errors/</code> and <code>events/errors/</code>.</p>

<h3>⚡ Async I/O</h3>
<p>All file operations run on background threads — zero game freezes.</p>

<br>

<h2>📋 Requirements</h2>

<table>
<tr><td><strong>Minecraft</strong></td><td>26.1.2</td></tr>
<tr><td><strong>NeoForge</strong></td><td>26.1.2.76+</td></tr>
<tr><td><strong>Java</strong></td><td>25</td></tr>
</table>

<br>

<h2>🎮 Commands</h2>

<ul>
<li><code>/dataminer dump</code> — Export all registries to JSON (async)</li>
<li><code>/dataminer mods analyze</code> — Regenerate full mod analysis</li>
<li><code>/dataminer perf start/stop</code> — Start/stop FPS + MSPT monitoring</li>
<li><code>/dataminer events start/stop</code> — Start/stop live event tracing</li>
<li><code>/dataminer latency start/stop</code> — Start/stop latency measurement</li>
<li><code>/dataminer latency stats</code> — Show live latency stats</li>
</ul>

<br>

---

<br>

<h2>🙏 Credits</h2>

<p>Developed by <strong>Stalking Dragons</strong>.</p>

<br>
<br>

<p align="center">
  <a href="https://codex.skdragons.com/" target="_blank">
    <img src="https://node-files.skdragons.com/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="200">
  </a>
  <br>
  <a href="https://codex.skdragons.com/">https://codex.skdragons.com/</a>
  <br>
  <em>Codex Stalking Dragons — Minecraft Modding</em>
</p>
