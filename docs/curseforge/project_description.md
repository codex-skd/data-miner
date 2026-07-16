<h1 align="center">📊 DataMiner</h1>

<p align="center"><strong>Diagnostic and profiling mod — registry dumps, performance monitoring, latency analysis, and mod issue detection.</strong></p>

<br>

---

<br>

<h2>✨ Overview</h2>

<p>DataMiner is a comprehensive diagnostic tool that dumps every game registry to enriched JSON, monitors FPS and MSPT in real time, traces live game events, measures action latency (eating, block breaking, slow ticks), and profiles mod impact — all without blocking the game thread.</p>

<p>It also captures WARN/ERROR messages from third-party mods and redirects them from <code>latest.log</code> to its own log files, keeping the main log clean. Known issue patterns (missing refmaps, broken access transformers, missing textures, invalid pack metadata) are automatically detected and saved as structured data for easy analysis.</p>

<br>

<h2>🎯 Features</h2>

<h3>📋 Registry Dumps</h3>
<p>Every <code>BuiltInRegistry</code> exported to enriched JSON — blocks (hardness, blast resistance, light emission, sound), items (stack size, max damage, rarity, food), entities (width, height, category, fire immunity, tracking range), status effects, sound events, creative tabs, potions, villager professions, and attributes.</p>

<h3>📈 Performance Monitor</h3>
<p>Real-time FPS (min/max/avg) and MSPT (min/max/avg) tracking via <code>/dataminer perf start/stop</code>. Identify performance bottlenecks across sessions.</p>

<h3>⏱️ Latency Analyzer</h3>
<p>Automatically measures eating time, block breaking time, and slow ticks (>50ms MSPT). Runs by default (<code>latencyAlwaysOn: true</code>) — no command needed. View live stats with <code>/dataminer latency stats</code> without stopping the tracer.</p>

<h3>🔍 Mod-Aware Slow Ticks</h3>
<p>Each slow tick includes an <code>entities_by_mod</code> breakdown, showing exactly which mod's entities are loaded. Pinpoint the mod causing lag at a glance.</p>

<h3>📦 Full Mod Analysis</h3>
<p>Per-mod report in <code>startup/mod_analysis.json</code> with ID, version, registry counts (blocks, items, entities), dependencies, and potential issues (missing deps, high entity/block counts).</p>

<h3>🔴 Log Redirect</h3>
<p>WARN/ERROR messages from other mods are automatically captured and redirected from <code>latest.log</code> to DataMiner's own <code>logs/captured.log</code>. Your main log stays clean and readable.</p>

<h3>🕵️ Smart Issue Detection</h3>
<p>Known problem patterns — missing refmaps, broken access transformers, missing textures, invalid pack metadata — are auto-detected and saved as structured JSON Lines in <code>startup/logs/issues.jsonl</code>.</p>

<h3>🎬 Live Event Tracer</h3>
<p>Capture game events in real time: player movement, block placement, entity interaction, damage, entity spawns/deaths, chunk loads. Output to timestamped JSON with full context.</p>

<h3>⚰️ Error Collection</h3>
<p>All uncaught exceptions are logged with full stack traces and causal chains to <code>startup/errors/</code> and <code>events/errors/</code>.</p>

<h3>⚡ Async I/O</h3>
<p>All file operations run on background threads via <code>DataMinerExecutor</code>. Zero game-thread blocking, zero freezes.</p>

<h3>🧩 Extensible Detection</h3>
<p>New issue patterns can be added via the <code>IssueDetector</code> interface. No rewrites needed.</p>

<br>

<h2>📋 Requirements</h2>

<table>
<tr><td><strong>Minecraft</strong></td><td>26.1.2</td></tr>
<tr><td><strong>NeoForge</strong></td><td>26.1.2.76+</td></tr>
<tr><td><strong>Java</strong></td><td>25</td></tr>
</table>

<br>

<h2>🎮 Commands</h2>

<table>
<tr><td><strong>Command</strong></td><td><strong>Description</strong></td></tr>
<tr><td><code>/dataminer dump</code></td><td>Export all registries to JSON</td></tr>
<tr><td><code>/dataminer mods analyze</code></td><td>Regenerate full mod analysis</td></tr>
<tr><td><code>/dataminer perf start/stop</code></td><td>Start/stop FPS + MSPT monitoring</td></tr>
<tr><td><code>/dataminer events start/stop</code></td><td>Start/stop live event tracing</td></tr>
<tr><td><code>/dataminer latency start</code></td><td>Start latency measurement</td></tr>
<tr><td><code>/dataminer latency stop</code></td><td>Stop and save latency report</td></tr>
<tr><td><code>/dataminer latency stats</code></td><td>Show live latency stats</td></tr>
</table>

<br>

<h2>🎮 How to Use</h2>

<ol>
<li>Drop <code>dataminer-*.jar</code> in <code>mods/</code> and launch the game.</li>
<li>DataMiner creates its folder structure on startup — no configuration needed.</li>
<li>Use <code>/dataminer latency stats</code> to view real-time latency data.</li>
<li>Run <code>/dataminer dump</code> to export all registries at any time.</li>
<li>Check <code>startup/logs/issues.jsonl</code> for detected mod issues.</li>
<li>Review <code>startup/mod_analysis.json</code> for per-mod impact profiling.</li>
</ol>

<br>

<h2>⚙️ Configuration</h2>

<p>Config file: <code>config/dataminer-common.toml</code></p>

<table>
<tr><td><strong>Key</strong></td><td><strong>Default</strong></td><td><strong>Description</strong></td></tr>
<tr><td><code>latencyAlwaysOn</code></td><td><code>true</code></td><td>Run latency tracer automatically</td></tr>
<tr><td><code>dumpOnStartup</code></td><td><code>false</code></td><td>Auto-dump registries on game start</td></tr>
</table>

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
