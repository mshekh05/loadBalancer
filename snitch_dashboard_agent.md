# 🕵️ SNITCH — DB Analysis Dashboard Agent Prompt

> **Target Model:** Claude Opus 4.6  
> **Output:** Single-file HTML (all CSS + JS inline, no frameworks)  
> **Design Reference:** Kodezi-style dark dashboard — code analysis aesthetic  
> **Product:** SNITCH — Intelligent Database Analysis Platform

---

## 🎯 MISSION

Build a **production-grade, pixel-perfect** single-page dashboard for **SNITCH**, a database analysis product. The UI must replicate the Kodezi aesthetic exactly: near-black backgrounds, electric indigo/violet brand accent, sparse typography, mini sparklines in stat cards, and clean tabular data. Every component should feel premium, dense with data but never cluttered.

Also generate a **SNITCH product icon/logo** rendered inline using pure SVG — a stylized magnifying glass merged with a database cylinder, in the brand violet palette.

---

## 🎨 EXACT VISUAL SPECIFICATION

### Color Palette (match precisely)
```css
:root {
  /* Backgrounds */
  --bg-root:    #0d0e14;   /* page background — deepest near-black */
  --bg-sidebar: #111218;   /* sidebar surface */
  --bg-card:    #161720;   /* stat cards, panel backgrounds */
  --bg-card-hover: #1c1d29; /* card hover state */
  --bg-row-alt: #13141e;   /* alternate table row */

  /* Borders */
  --border:     #1f2030;   /* standard border */
  --border-muted: #191a28; /* very subtle separator */

  /* Brand */
  --brand:      #7c5cfc;   /* SNITCH violet — primary accent */
  --brand-dim:  #5b3fd4;   /* darker brand for hover */
  --brand-glow: rgba(124, 92, 252, 0.15); /* subtle brand glow fill */

  /* Status */
  --green:      #34d399;   /* healthy / positive delta */
  --green-dim:  rgba(52, 211, 153, 0.12);
  --red:        #f87171;   /* error / negative delta */
  --red-dim:    rgba(248, 113, 113, 0.12);
  --amber:      #fbbf24;   /* warning */
  --amber-dim:  rgba(251, 191, 36, 0.12);
  --blue:       #60a5fa;   /* info */

  /* Typography */
  --text-primary:   #e8e9f0;
  --text-secondary: #8b8fa8;
  --text-muted:     #4b4e6a;
  --text-brand:     #a78bfa; /* brand purple for highlights, code */

  /* Fonts */
  --font-ui:   'Inter', sans-serif;       /* all UI labels, body */
  --font-mono: 'JetBrains Mono', monospace; /* numbers, metrics, code refs */
  --font-brand:'Syne', sans-serif;        /* SNITCH logo, section headers */
}
```

### Typography Rules
- **ALL stat/metric numbers:** `font-family: var(--font-mono)`, weight 600, letter-spacing: -0.02em
- **ALL nav labels, table cells, body copy:** `font-family: var(--font-ui)`, weight 400, 13–14px
- **SNITCH logo + section group labels:** `font-family: var(--font-brand)`, weight 700
- **Code filenames, DB references:** `font-family: var(--font-mono)`, color: `var(--text-brand)`, inside a `<code>` tag with `background: rgba(124,92,252,0.1); padding: 1px 5px; border-radius: 4px`
- **Delta badges:** always uppercase, 11px, weight 600, mono font

---

## 🔤 SNITCH PRODUCT ICON (SVG — inline, 48×48 viewBox)

Generate this as an inline SVG in the sidebar logo area. The icon is a **magnifying glass whose lens contains a tiny database cylinder symbol**:

```
Design intent:
- Outer circle: stroke only, #7c5cfc, stroke-width 3, circle cx=20 cy=20 r=13
- Handle: rounded line from (30,30) to (43,43), stroke #7c5cfc, stroke-width 4, linecap round
- Inside the lens: a small stylized DB — two stacked horizontal ellipses (top cap + body)
  - Top ellipse: cx=20 cy=17, rx=7 ry=3, fill none, stroke #a78bfa, stroke-width 1.5
  - Body arc: just the bottom half of a rect beneath — path from (13,17) down to (13,23) 
    curve to (27,23) up to (27,17), fill none, stroke #a78bfa, stroke-width 1.5
  - Middle line (table divider inside db): line x1=13 y1=20 x2=27 y2=20, 
    stroke #a78bfa, stroke-width 1, opacity 0.6
- Small dot highlight: circle cx=15 cy=15 r=2 fill #7c5cfc opacity 0.5
```

Place next to "SNITCH" wordmark in sidebar. The wordmark uses `font-family: 'Syne'`, weight 800, 17px, color #e8e9f0, letter-spacing: 0.08em.

---

## 🏗️ LAYOUT

```
┌──────────────────────────────────────────────────────────────┐
│  SIDEBAR 220px fixed      │   MAIN AREA (flex-1, scrollable) │
│  bg: #111218              │   bg: #0d0e14                    │
│  border-right: 1px #1f2030│                                  │
│                           │  ┌─ TOP HEADER ──────────────┐  │
│  [Icon] SNITCH            │  │ "Dashboard"  System: Healthy│  │
│                           │  └────────────────────────────┘  │
│  [Search bar]  ⌘K         │                                  │
│                           │  ┌─ 3 KPI CARDS ──────────────┐  │
│  ── (no label) ──         │  │ Bug Healed│LOC Refactored│PRs│  │
│  🏠 Dashboard ← active    │  └────────────────────────────┘  │
│  📊 DORA Metrics          │                                  │
│  📁 Repositories          │  ┌─ ROW 2: 2-col (60/40) ─────┐  │
│  🧠 Memory Cortex         │  │ Code Stability│Flaky Tests  │  │
│                           │  └────────────────────────────┘  │
│  ── System Health ──      │                                  │
│  🔍 System Overview       │  ┌─ ROW 3: 3-col (equal) ─────┐  │
│  🗺️  Risk Map              │  │ Bug Fix│Recent Healing│Impact│  │
│  💊 Healing Activity      │  └────────────────────────────┘  │
│                           │                                  │
│  ── Automation Engines ── │                                  │
│  ⚙️  Evolution Engine      │                                  │
│  📄 Living Docs           │                                  │
│  🔄 Smart PR Engine       │                                  │
│                           │                                  │
│  ── Integrations ──       │                                  │
│  🔗 Connected Tools       │                                  │
│  ⚡ Automation Sequences   │                                  │
│                           │                                  │
│  [Avatar] Ishraq Khan     │                                  │
│           ishraq@snitch   │                                  │
└──────────────────────────────────────────────────────────────┘
```

---

## 📦 COMPONENT SPECIFICATIONS

### 1. SIDEBAR

**Structure:**
- Fixed left, 220px wide, full height, `background: #111218`, `border-right: 1px solid #1f2030`
- Top padding: 20px

**Logo row:**
- Inline SVG icon (48×48, as specified above) + "SNITCH" wordmark
- Below logo: small tagline `<p>DB Analysis Platform</p>` in 10px, `var(--text-muted)`

**Search bar:**
- Full width input, `background: #0d0e14`, border `1px solid #1f2030`, border-radius 8px
- Padding: 8px 12px, placeholder "Search", right-side `<kbd>⌘K</kbd>` badge
- `<kbd>` style: `background: #1f2030; border-radius: 4px; padding: 1px 5px; font-size: 10px; color: var(--text-muted)`

**Nav sections:**
Each section has a group label (`font-size: 10px; font-weight: 600; letter-spacing: 0.1em; color: var(--text-muted); text-transform: uppercase; margin: 20px 0 6px 12px`) except the first (Dashboard group has no label).

Nav item style:
```
padding: 7px 12px
border-radius: 7px
font-size: 13.5px
color: var(--text-secondary)
display: flex; align-items: center; gap: 9px
cursor: pointer
transition: background 0.15s, color 0.15s
```

Active state: `background: rgba(124,92,252,0.12); color: #e8e9f0; font-weight: 500`  
Active item also has `border-left: 2px solid #7c5cfc; padding-left: 10px`  
Hover (non-active): `background: #161720; color: #c4c6d8`

**Icons:** Use simple Unicode or tiny inline SVGs (16×16). Keep monochrome, matching text color.

**Bottom user row:**
- `margin-top: auto` (pushed to bottom)
- `border-top: 1px solid #1f2030; padding: 14px 12px`
- Avatar: 32px circle, `background: linear-gradient(135deg, #7c5cfc, #a78bfa)`, initials "IK" in 12px white
- Name: 13px `var(--text-primary)`, email: 11px `var(--text-muted)` below

---

### 2. TOP HEADER

```
padding: 24px 28px 16px
border-bottom: 1px solid #1f2030
display: flex; justify-content: space-between; align-items: flex-start
```

**Left:**
- `<h1>` "Dashboard" — `font-family: 'Syne'; font-size: 24px; font-weight: 700; color: #e8e9f0; margin: 0`
- Below: `<p>System Status: <span style="color: var(--green); font-weight: 500;">Healthy</span></p>` — 13px

**Right (no export button in this design — keep it minimal):**
- Just a small cluster status pill: `background: var(--green-dim); color: var(--green); border: 1px solid rgba(52,211,153,0.2); border-radius: 20px; padding: 4px 12px; font-size: 12px; font-weight: 500`
- "● All systems operational"

---

### 3. KPI STAT CARDS (3 across, equal width)

Each card:
```
background: var(--bg-card)
border: 1px solid var(--border)
border-radius: 10px
padding: 18px 20px
display: flex; justify-content: space-between; align-items: flex-start
```

**Left side of each card:**
- Label: `ALL CAPS, 11px, letter-spacing 0.08em, color var(--text-muted), font-weight 600`
- An ⓘ icon (inline SVG circle-i, 12px, muted) next to the label
- Value: `font-family: var(--font-mono); font-size: 28px; font-weight: 600; color: var(--text-primary); margin: 10px 0 8px`
- Delta badge: arrow icon + percentage + "than last month"
  - Badge format: `↗ 17.4% than last month` — green for up/good, red for down/bad
  - `font-family: var(--font-mono); font-size: 11px; font-weight: 600`
  - Arrow: `↗` or `↘` Unicode, colored matching delta direction

**Right side — SPARKLINE:**
- A mini SVG line chart (80×36px) — no axes, no labels, just a smooth bezier line
- Line color: green (#34d399) for positive metric, red (#f87171) for negative
- Line stroke-width: 1.5px
- Subtle area fill beneath: `rgba(color, 0.06)`
- **Data to render as sparklines (fictional but realistic, last 6 points):**

| Card | Label | Value | Delta | Sparkline Direction | Line Color |
|------|-------|-------|-------|---------------------|------------|
| Bug Healed | BUG HEALED | 484 | ↘ 7.6% (bad) | mostly up then dip | red |
| Lines of Code Refactored | LINES OF CODE REFACTORED | 100K | ↗ 17.4% (good) | gentle rise | green |
| Auto-Generated PRs | AUTO-GENERATED PRS | 67 | ↗ 11.3% (good) | slight rise | green/teal |

---

### 4. CODE STABILITY OVERVIEW PANEL (left panel, row 2)

Card: `background: var(--bg-card); border: 1px solid var(--border); border-radius: 10px; padding: 20px`

**Header row:**
- Left: "CODE STABILITY OVERVIEW" label + ⓘ icon (same style as KPI labels)
- Right: dropdown "Last month ▾" — `background: #0d0e14; border: 1px solid #1f2030; border-radius: 6px; padding: 4px 10px; font-size: 12px; color: var(--text-secondary); cursor: pointer`

**Score:**
- `<span style="font-family: 'Syne'; font-size: 22px; font-weight: 700; color: var(--brand)">82</span><span style="color: var(--text-muted); font-size: 16px">/100</span>  <span style="color: var(--text-secondary); font-size: 14px; margin-left: 8px">Overall Health</span>`

**Segmented progress bar:**
- Full-width bar, height 10px, border-radius 5px, `overflow: hidden; display: flex`
- Segments laid out proportionally:
  - Critical (red): 14% width → `background: #ef4444`
  - High (orange): 23% width → `background: #f97316`
  - Medium (yellow): 1% width → `background: #eab308`
  - Remaining/Low (muted): 62% width → `background: #1f2030`

**Legend rows** (below bar, 4 rows):
```
display: grid; grid-template-columns: auto 1fr auto auto; gap: 6px 16px; margin-top: 14px
```
Each row: `[colored 10px square]  [label 13px]  [percentage 12px mono muted]  [count 12px mono right-aligned]`

| Color Square | Label | % | Count |
|---|---|---|---|
| #ef4444 | Critical | 14% | 931 |
| #f97316 | High | 23% | 14% |
| #eab308 | Medium | 1% | 23% |
| #d1d5db (muted) | Low | 61% | 1% |

*(Note: the "Count" column in the reference shows swapped % values — replicate as-is)*

**Footer stat:**
`↘ 3%` in red with arrow, then `<span style="color: var(--text-secondary)"> fewer critical issues than last month</span>` — 12px, italic-ish, below legend

---

### 5. MOST FLAKY TESTS PANEL (right panel, row 2)

Card: same card style

**Header:** "MOST FLAKY TESTS" + ⓘ + "Last month ▾" dropdown (right-aligned)

**Table:**
```css
table { width: 100%; border-collapse: collapse; font-size: 13px; }
th { color: var(--text-muted); font-weight: 600; font-size: 11px; letter-spacing: 0.06em; 
     text-transform: uppercase; padding: 0 0 10px; border-bottom: 1px solid var(--border); }
td { padding: 9px 0; border-bottom: 1px solid var(--border-muted); }
tr:last-child td { border-bottom: none; }
tr:hover td { background: var(--bg-card-hover); }
```

**Columns:** Tests | Passes | Fails | Avg Time

- **Tests column:** plain name, `color: var(--text-primary)` — 14px
- **Passes column:** green number with `+` prefix, `color: var(--green); font-family: var(--font-mono); font-weight: 600`
- **Fails column:** red number with `-` prefix, `color: var(--red); font-family: var(--font-mono); font-weight: 600`
- **Avg Time:** muted mono, `color: var(--text-secondary); font-family: var(--font-mono)`

| Test Name | Passes | Fails | Avg Time |
|-----------|--------|-------|----------|
| auth-flow | +15 | -6 | 12s |
| dashboard/load (dupe shown) | +42 | -12 | 9s |
| user-validation | +100 | -20 | 20s |
| stripe-hook | +11 | -1 | 5s |
| (unnamed/pending) | +21 | -0 | 8s |
| dashboard/load | +42 | -12 | 9s |

For the unnamed row, show `<span style="color: var(--text-muted); font-style: italic">unnamed</span>`

---

### 6. BUG FIX ACTIVITY (bottom-left card)

Card: same card style

**Header:** "BUG FIX ACTIVITY" + ⓘ + "Last month ▾" (right)

**Stat row:**
```
↗ 70.4%  Auto fix    ↗ 25.4%  Manual fix
```
- Each: `↗` in green, percentage in `var(--font-mono)` green weight-600, label in `var(--text-secondary)` 12px
- 16px gap between the two stats

**Area chart (full-width inside card, height ~100px):**
- Two smooth area lines on same chart — NO axes, NO labels, just lines + filled areas
- Line 1 (Auto fix): `#7c5cfc` (brand violet), area fill `rgba(124,92,252,0.15)`
- Line 2 (Manual fix): subtle line, color `rgba(124,92,252,0.4)`, dashed or thinner
- Both lines trend downward-then-up (bug activity pattern) across ~12 data points
- Use SVG with `preserveAspectRatio="none"` to fill the container width
- Smooth bezier curves (cubic), area fill via SVG `<path>` closing to baseline

---

### 7. RECENT HEALING (bottom-center card)

Card: same card style

**Header:** "RECENT HEALING" + ⓘ + "Last month ▾" (right)

**Activity list** (6 rows):
Each row: `display: flex; justify-content: space-between; align-items: center; padding: 7px 0; border-bottom: 1px solid var(--border-muted)`

- Left side: icon (🔧 or ⚙️ as appropriate, 13px) + action text, with code reference in brand code style
- Right side: PR count badge — `+1 PR`, `+3 PR`, etc.
  - Badge: `background: var(--brand-glow); color: var(--brand); border: 1px solid rgba(124,92,252,0.25); border-radius: 4px; padding: 1px 7px; font-size: 11px; font-family: var(--font-mono); font-weight: 600`

| Icon | Text | Code Ref | PR Badge |
|------|------|----------|----------|
| 🔧 | Patched flaky test | `load.test.ts` | +1 PR |
| ⚙️ | Modularized | `stripeService.js` | +3 PR |
| 🔧 | Fixed deployment | `deplo...` (truncated) | +1 PR |
| ⚙️ | Refactored API client | `api...` (truncated) | +2 PR |
| 🔧 | Fixed edge case | `formatDate.js` | +1 PR |

Code refs use the `<code>` style defined above. Long refs get truncated with `...` and `text-overflow: ellipsis`.

---

### 8. REFACTOR IMPACT (bottom-right card)

Card: same card style

**Header:** "REFACTOR IMPACT" + ⓘ + "Last month ▾" (right)

**Donut chart (SVG, ~120px diameter):**
- 3 segments — rendered as SVG arcs using `stroke-dasharray` technique on a circle
- Circle: `cx=60 cy=70 r=40`, stroke-width 22 (thick ring, no fill)
- **Segments:**
  - Legacy code removed: 40% — color `#7c5cfc` (brand violet)
  - Components modularized: 10% — color `#34d399` (green)
  - Stack upgraded: 50% — color `#f97316` (orange)
- Center of donut: label "100K" in 13px mono, or leave empty
- Circumference = 2π×40 ≈ 251.3px

**Legend (right of donut):**
Each legend item: `[10px colored circle]  [label 12px text-secondary]  [value 13px mono text-primary weight-600]`

| Color | Label | Value |
|-------|-------|-------|
| #7c5cfc | Legacy code removed | 50K Lines |
| #34d399 | Components modularized | 40K Lines |
| #f97316 | Stack upgraded | 10K Lines |

Layout: `display: flex; gap: 12px; align-items: center` — donut left, legend right

---

## ✨ ANIMATIONS & INTERACTIONS

### On Load (stagger, clean)
1. Sidebar items fade in with `opacity: 0 → 1; transform: translateX(-8px) → translateX(0)` — 0.25s each, 40ms stagger
2. KPI cards: `opacity: 0 → 1; transform: translateY(12px) → 0` — 0.3s, 80ms stagger each
3. Sparklines draw themselves: SVG `stroke-dashoffset` animation, 0.8s ease-out
4. Segmented progress bar: width animates from 0 to target, 0.6s ease-out, delayed 0.3s
5. Donut chart segments fill from 0 to value: `stroke-dashoffset` animation, 0.9s ease-out, 0.4s delay
6. Table rows: `opacity: 0 → 1` cascade, 30ms per row

### Micro-interactions
- KPI card hover: `background → var(--bg-card-hover); border-color → rgba(124,92,252,0.3); transform: translateY(-1px)` — 0.2s
- Nav item hover: instant (no animation, just color change)
- Table row hover: `background → var(--bg-card-hover)` — 0.1s
- PR badge in Recent Healing: hover scales to 1.05, 0.15s

### Simulated Live Data
- Every 4 seconds: randomly nudge the "Bug Healed" count by ±3, update the sparkline last point
- KPI number change: brief `color: var(--brand) → var(--text-primary)` flash over 0.4s using CSS transition

---

## 🔧 TECHNICAL IMPLEMENTATION

### HTML Structure
```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SNITCH — DB Analysis Dashboard</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Syne:wght@700;800&family=JetBrains+Mono:wght@400;600&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
  <style>/* ALL CSS HERE */</style>
</head>
<body>
  <div class="app">
    <aside class="sidebar">...</aside>
    <main class="main">
      <header class="topbar">...</header>
      <div class="content">
        <!-- Row 1: 3 KPI cards -->
        <div class="grid-3">...</div>
        <!-- Row 2: 60/40 split -->
        <div class="grid-60-40">...</div>
        <!-- Row 3: 3 equal -->
        <div class="grid-3">...</div>
      </div>
    </main>
  </div>
  <script>/* ALL JS HERE */</script>
</body>
</html>
```

### CSS Architecture
```css
* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: var(--font-ui); background: var(--bg-root); color: var(--text-primary); 
       height: 100vh; overflow: hidden; }
.app { display: flex; height: 100vh; }
.sidebar { width: 220px; flex-shrink: 0; /* ... */ }
.main { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.content { flex: 1; overflow-y: auto; padding: 20px 24px; 
           display: flex; flex-direction: column; gap: 16px; }
.grid-3 { display: grid; grid-template-columns: repeat(3, 1fr); gap: 14px; }
.grid-60-40 { display: grid; grid-template-columns: 3fr 2fr; gap: 14px; }
```

### JavaScript Modules
```
// === SNITCH ICON SVG GENERATOR ===
// === SPARKLINE RENDERER ===
// === DONUT CHART RENDERER ===
// === BUG FIX AREA CHART RENDERER ===
// === LOAD ANIMATIONS ===
// === LIVE DATA SIMULATION ===
// === UI INTERACTIONS (dropdowns, hover states) ===
```

### Sparkline Math
For each sparkline (80×36 viewBox):
```js
function renderSparkline(data, color, svgEl) {
  const w = 80, h = 36, pad = 4;
  const min = Math.min(...data), max = Math.max(...data);
  const points = data.map((v, i) => ({
    x: pad + (i / (data.length - 1)) * (w - pad * 2),
    y: h - pad - ((v - min) / (max - min || 1)) * (h - pad * 2)
  }));
  // Build smooth cubic bezier path
  // Build area path (close to bottom)
  // Set stroke-dasharray + animate to 0
}
```

### Donut Chart Math
```js
const R = 40, circumference = 2 * Math.PI * R; // ≈ 251.3
// For each segment, calculate stroke-dasharray and stroke-dashoffset
// Use stroke-dashoffset rotation via transform="rotate(-90 60 70)"
// Stack segments using cumulative offset
segments.forEach((seg, i) => {
  const dash = (seg.pct / 100) * circumference;
  const offset = circumference - dash;
  const rotation = -90 + (cumulative / 100) * 360;
  // Apply to SVG circle element
  cumulative += seg.pct;
});
```

---

## 📐 SPACING SYSTEM

| Token | Value |
|-------|-------|
| Content padding | 20px 24px |
| Card padding | 18px 20px |
| Card gap | 14px |
| Inner section gap | 12px |
| Row gap (table) | 0 (border-separated) |
| Nav item padding | 7px 12px |
| Nav group label margin-top | 20px |

---

## 🚫 DO NOT

- ❌ Use any color lighter than `#161720` for card backgrounds
- ❌ Use white text (`#ffffff`) — use `#e8e9f0` max
- ❌ Add shadows or glows — only the brand glow background color is allowed
- ❌ Use Bootstrap, Tailwind, or any external CSS framework
- ❌ Leave placeholder lorem ipsum text — use DB/code analysis terminology
- ❌ Make the layout responsive/mobile — design strictly for 1280px+ desktop
- ❌ Use `<canvas>` — all charts must be pure SVG
- ❌ Use `Chart.js` or other chart libraries — implement all charts manually
- ❌ Use emojis in production UI elements (only in this spec for reference)
- ❌ Use `box-shadow` for elevation — use border color changes instead

---

## ✅ DEFINITION OF DONE

- [ ] SNITCH SVG icon renders correctly in sidebar (magnifying glass + DB symbol)
- [ ] SNITCH wordmark uses Syne font, matches spec
- [ ] Sidebar has all nav sections with group labels, active state on Dashboard
- [ ] Search bar with ⌘K badge renders correctly
- [ ] User avatar row at bottom with gradient circle
- [ ] All 3 KPI cards render with sparklines, values, and delta badges
- [ ] Code Stability panel: score, segmented bar, legend, footer stat
- [ ] Flaky Tests table: correct columns, colored values, hover states
- [ ] Bug Fix Activity: area chart renders with 2 lines
- [ ] Recent Healing: list with code refs in brand-colored code tags + PR badges
- [ ] Refactor Impact: donut chart with 3 segments + legend
- [ ] All load animations run sequentially on page open
- [ ] Live data simulation updates Bug Healed every 4s
- [ ] Entire layout fits in viewport without outer scroll (inner content area scrolls)
- [ ] All numbers use JetBrains Mono font
- [ ] Color palette matches exactly — no generic blues/grays

---

*Agent prompt authored for Claude Opus 4.6 | SNITCH Dashboard v1.0*  
*Reference: Kodezi-style DB analysis dashboard aesthetic*
