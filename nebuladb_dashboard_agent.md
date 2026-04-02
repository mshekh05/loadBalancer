# 🛰️ NebulaDB — Database Monitoring Dashboard Agent Prompt

> **Target Model:** Claude Opus 4.6  
> **Output:** Single-file HTML/CSS/JS dashboard (no frameworks required)  
> **Design Reference:** Dark-mode, anime-skyline-backdrop database monitoring UI

---

## 🎯 MISSION

Build a **production-grade, fully interactive** database monitoring dashboard called **NebulaDB**. The dashboard must feel like a premium SaaS product — cinematic dark theme, glowing data visualizations, fluid micro-interactions, and real-time-feeling metrics. Every pixel should feel intentional.

---

## 🖼️ VISUAL IDENTITY & AESTHETIC DIRECTION

### Theme
- **Tone:** Cinematic dark-mode. Deep space meets DevOps tooling.
- **Palette:**
  - Background base: `#0d0f14` (near-black with blue undertone)
  - Panel/card surface: `#13161e` with subtle `rgba(255,255,255,0.03)` borders
  - Accent cyan/teal: `#00e5ff` (interactive elements, active states)
  - Accent green: `#00e676` (healthy status)
  - Accent orange/amber: `#ff9100` (warnings, degraded)
  - Accent red: `#ff1744` (critical alerts)
  - Accent purple: `#7c4dff` (CPU gauge ring)
  - Muted text: `#6b7280`
  - Primary text: `#e2e8f0`
- **Typography:** Use Google Fonts — `'Syne'` for headings/sidebar, `'JetBrains Mono'` for metrics & numbers, `'Inter'` for body text (this context warrants Inter for legibility in dense data UI)
- **Background FX:** Atmospheric gradient backdrop — simulate a twilight cityscape using CSS layered radial gradients in deep indigo, crimson, and amber tones. Subtle animated `@keyframes` drift on the gradient to evoke a living sky.
- **Depth:** Cards use `backdrop-filter: blur(12px)` + semi-transparent backgrounds. Inner glows on active elements using `box-shadow` with color-matched values.

---

## 🏗️ LAYOUT ARCHITECTURE

### Structure
```
┌─────────────────────────────────────────────────────────┐
│  SIDEBAR (220px fixed)  │  MAIN CONTENT AREA (flex-1)   │
│                         │                               │
│  Logo + Nav + Pinned    │  Topbar                       │
│  Clusters + User        │  ├── KPI Cards (4-col grid)   │
│                         │  ├── Charts Row (2-col)       │
│                         │  │   ├── Line Chart           │
│                         │  │   └── Gauge Charts         │
│                         │  └── Region Table             │
└─────────────────────────────────────────────────────────┘
```

---

## 📦 COMPONENT SPECIFICATIONS

### 1. SIDEBAR
- **Width:** 220px, full-height, `background: #0f1117`, right border `1px solid rgba(255,255,255,0.06)`
- **Logo:** 🌌 icon + "NebulaDB" in `'Syne'` bold, white
- **Search bar:** Full-width input with magnifier icon, `background: rgba(255,255,255,0.04)`, rounded-lg, placeholder "Search anything"
- **Nav items** (with icons — use Unicode or SVG inline):
  - 📊 Overview *(active — highlight with left border `3px solid #00e5ff` + subtle bg glow)*
  - ⚡ Performance
  - 🔍 Query Analyzer
  - 🏗️ Infrastructure
  - 🔔 Alerts
  - 👥 Team
  - 💳 Billing
  - 🔗 Integrations
- **Pinned Clusters section** (collapsible chevron label):
  - 🗄️ Production-DB-1 ⭐
  - 🗄️ Analytics-Replica ⭐
  - 🗄️ Staging-Cluster ⭐
- **Bottom:** Help center + Settings icons, then user avatar card (Joe Smith, DevOps Engineer, small avatar circle + expand chevron)
- Active nav item gets `background: rgba(0, 229, 255, 0.08)` + left accent border

---

### 2. TOPBAR
- Title: **"Monitoring Overview"** in `'Syne'` 22px + "Production" badge (rounded, muted border)
- Right side: 
  - 🟢 **System Healthy** dot + "99.98%" in green
  - 🔔 Bell icon (notification)
  - **Export** button — cyan background `#00e5ff`, dark text, rounded, with ↑ arrow icon

---

### 3. KPI CARDS (4 across)
Each card: `background: rgba(255,255,255,0.03)`, border `1px solid rgba(255,255,255,0.08)`, border-radius 12px, padding 20px, hover lifts with `translateY(-2px)` + brighter border.

| Card | Icon | Value | Delta | Color |
|------|------|-------|-------|-------|
| Avg Latency | 📶 | **42ms** | -12% vs last 24h | Red (down = good, show green) |
| Error Rate | ⚠️ | **0.04%** | +0.01% vs last 24h | Red (up = bad) |
| Throughput | ↗️ | **12.4k req/s** | -12.3% vs last 24h | Red |
| Active Connections | 📡 | **3,405** | +5% vs last 24h | Green |

- Values use `'JetBrains Mono'` font, 28px, white
- Delta badges: small pill with icon arrow + percentage

---

### 4. CHARTS ROW (2-column, 60/40 split)

#### A. Latency & Error Rate Line Chart (left, 60%)
- **Canvas-based or pure SVG** line chart
- X-axis: 00:00 → 20:00 (hourly ticks, muted labels)
- Y-axis: 0ms → 200ms
- **Two lines:**
  - Latency: `#00e5ff` (cyan), smooth bezier curve with area gradient fill fading to transparent
  - Error Rate: `#ff1744` (red), thinner line
- **Tooltip on hover:** Dark card popup showing "11:00 — Latency: 105ms" with a dot marker on the chart
- **Time range dropdown:** "Last 24 hour ▾" — top-right of the card
- Animate line drawing on load using SVG `stroke-dashoffset` animation

#### B. Resource Usage Gauges (right, 40%)
- **3 donut/arc gauges** in a row:
  - **CPU Usage:** 68% — ring color `#7c4dff` (purple) on dark track
  - **Memory Usage:** 82% — ring color `#00e676` (green) on dark track  
  - **Disk Usage:** 54% — ring color `#ff9100` (amber) on dark track
- Each gauge: SVG circle with `stroke-dasharray` calculated from percentage, center % label in `'JetBrains Mono'` bold
- Animate gauges filling on load (CSS transition on stroke-dashoffset)
- **System Insight box** below gauges: amber left border, italic insight text ("Memory usage on eu-west-1 cluster is nearing 85%. Consider scaling up to prevent spikes.")
- **Legend row** at bottom: CPU • Mem • Disk with colored dots

---

### 5. REGION & CLUSTER TABLE

- Section header: "Region & Cluster Breakdown"
- **Toolbar:** Search input + Status dropdown + Environment dropdown + Table/Grid toggle + Show All Regions dropdown
- **Table columns:** Region | Health | Avg Latency | Error Rate | Throughput (QPS) | Action
- **Rows** (zebra striping subtle, hover highlight):

| Region | Health | Avg Latency | Error Rate | QPS | Action |
|--------|--------|-------------|------------|-----|--------|
| us-west-2 | 🔴 Critical | 240ms | 2.45% | 5.1k | [Investigate] |
| us-east-1 | 🟢 Healthy | 35ms | 0.01% | 4.2k | View Logs |
| eu-west-1 | 🟡 Degraded | 125ms | 0.80% | 3.8k | [Investigate] |
| ap-southeast-1 | 🟢 Healthy | 45ms | 0.00% | 2.1k | View Logs |

- **Health badges:** Pill-shaped colored badges — Critical (red bg), Healthy (green bg), Degraded (amber bg)
- **[Investigate] buttons:** Cyan solid pill buttons
- **View Logs:** Plain text link style
- Latency colored: red if >100ms, green if <50ms, amber otherwise

---

## ✨ INTERACTIONS & ANIMATIONS

### On Load
1. Sidebar slides in from left (0.3s ease-out)
2. KPI cards stagger-fade in (0.1s delay each, translateY from +20px)
3. Line chart draws itself (SVG path animation, 1.5s)
4. Gauge rings fill from 0 to value (1s ease-out, staggered 0.2s apart)
5. Table rows fade in sequentially

### Ongoing
- **Simulated live data:** Every 3 seconds, slightly randomize the latency line chart's last data point and update the KPI values by ±2–5% using `setInterval`. Animate the number change with a brief flash.
- **Hover states:** Cards lift, table rows highlight, nav items glow
- **Tooltip:** Follows mouse on chart with smooth `transform` transition
- **Active connections counter:** Slowly increments/decrements to simulate live traffic

### Micro-interactions
- Bell icon jiggles on page load (CSS animation)
- Export button has ripple effect on click
- Gauge percentages pulse subtly at high values (>80%)

---

## 🔧 TECHNICAL IMPLEMENTATION RULES

### File Structure
- **Single HTML file** — all CSS in `<style>`, all JS in `<script>`
- No external frameworks (no React, no Vue, no Tailwind)
- Google Fonts via CDN link tag only
- All charts: pure SVG or Canvas2D — no Chart.js dependency

### CSS Architecture
```css
:root {
  --bg-base: #0d0f14;
  --bg-panel: #13161e;
  --bg-card: rgba(255, 255, 255, 0.03);
  --border: rgba(255, 255, 255, 0.08);
  --accent-cyan: #00e5ff;
  --accent-green: #00e676;
  --accent-orange: #ff9100;
  --accent-red: #ff1744;
  --accent-purple: #7c4dff;
  --text-primary: #e2e8f0;
  --text-muted: #6b7280;
  --font-display: 'Syne', sans-serif;
  --font-mono: 'JetBrains Mono', monospace;
  --font-body: 'Inter', sans-serif;
  --radius-sm: 6px;
  --radius-md: 10px;
  --radius-lg: 14px;
}
```

### JavaScript Modules
Organize JS into clean sections with comments:
1. `// === CHART DATA & CONFIG ===`
2. `// === LINE CHART RENDERER ===`
3. `// === GAUGE RENDERER ===`
4. `// === LIVE DATA SIMULATION ===`
5. `// === UI INTERACTIONS ===`
6. `// === INIT ===`

### Chart Math
For the SVG line chart:
- Map time (0–24h) to x coordinates within chart width
- Map latency values to y coordinates (inverted — 0ms at bottom)
- Use cubic bezier control points for smooth curves: `C` command in SVG path
- Area fill: duplicate path with `y=chartBottom` endpoints, fill with `linearGradient` from `rgba(0,229,255,0.15)` to `transparent`

For SVG gauge rings:
```js
const circumference = 2 * Math.PI * radius; // radius ~40
const dashOffset = circumference * (1 - percentage/100);
// Set stroke-dasharray="circumference" stroke-dashoffset="dashOffset"
```

---

## 📐 SPACING & SIZING

- Sidebar: 220px wide
- Main content padding: 24px
- Card gap: 16px
- KPI grid: `display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px`
- Charts row: `display: grid; grid-template-columns: 3fr 2fr; gap: 16px`
- Border radius on cards: 12px
- All transitions: `0.2s ease` unless specified otherwise

---

## 🚫 WHAT NOT TO DO

- ❌ No light mode — strictly dark only
- ❌ No placeholder gray boxes — all charts must render actual data
- ❌ No lorem ipsum — use realistic DB/infra terminology
- ❌ No generic Bootstrap or Tailwind classes
- ❌ No flat/boring background — the atmospheric gradient is essential to the aesthetic
- ❌ Don't break layout on 1280px+ screens — design for desktop

---

## ✅ DEFINITION OF DONE

The output is complete when:
- [ ] Sidebar renders with all nav items, pinned clusters, and user footer
- [ ] All 4 KPI cards show with correct icons, values, and delta badges
- [ ] Line chart renders with animated draw, hover tooltip, and time axis labels
- [ ] All 3 gauge rings render with correct colors, percentages, and fill animation
- [ ] System Insight box appears below gauges
- [ ] Region table renders with all 4 rows, health badges, action buttons, and toolbar
- [ ] Live data simulation runs and updates values every 3 seconds
- [ ] Page load animations complete gracefully in sequence
- [ ] Entire page fits in viewport without scrolling (aside from table overflow)
- [ ] Visual quality is indistinguishable from a real SaaS product screenshot

---

*Agent authored for Claude Opus 4.6 | NebulaDB Dashboard v1.0*
