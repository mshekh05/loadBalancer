# 🔍 SNITCH — Query Analyzer Agent Prompt

> **Target Model:** Claude Opus 4.6  
> **Output:** Single-file HTML (all CSS + JS inline, no frameworks)  
> **Design Reference:** Kodezi code editor UI — dark IDE aesthetic with split-pane analysis  
> **Feature:** SQL/NoSQL Query Analyzer with AI-powered suggestions panel  
> **Fits within:** SNITCH DB Analysis Platform (same design system as Dashboard)

---

## 🎯 MISSION

Build a **production-grade Query Analyzer** page for SNITCH. The layout is a full-viewport IDE-style interface: a file tree on the left, a tabbed query editor in the center, a minimap preview, and a suggestions/analysis panel on the right. The user pastes or writes a database query, hits **Analyze**, and the right panel populates with ranked issues, optimizations, and security flags — each with an inline fix preview.

This must feel like a real developer tool — not a form. Syntax-highlighted query editor, scrollable file tree, collapsible suggestion cards, apply/dismiss actions.

---

## 🎨 DESIGN SYSTEM

> Same palette as the SNITCH Dashboard. Replicated here for completeness.

```css
:root {
  /* Backgrounds */
  --bg-root:        #0d0e14;
  --bg-sidebar:     #111218;
  --bg-editor:      #0f1019;
  --bg-card:        #161720;
  --bg-card-hover:  #1c1d29;
  --bg-tab-active:  #0f1019;
  --bg-tab-idle:    #0d0e14;
  --bg-suggestion:  #14151f;
  --bg-inlay:       #1a1b27;  /* code block inside suggestion card */

  /* Borders */
  --border:         #1f2030;
  --border-subtle:  #191a28;
  --border-focus:   rgba(124, 92, 252, 0.5);

  /* Brand */
  --brand:          #7c5cfc;
  --brand-dim:      #5b3fd4;
  --brand-glow:     rgba(124, 92, 252, 0.15);
  --brand-text:     #a78bfa;

  /* Status / Severity */
  --sev-opt:        #7c5cfc;   /* Optimization — brand violet */
  --sev-opt-bg:     rgba(124, 92, 252, 0.1);
  --sev-sec:        #f87171;   /* Security — red */
  --sev-sec-bg:     rgba(248, 113, 113, 0.1);
  --sev-read:       #34d399;   /* Readability — green */
  --sev-read-bg:    rgba(52, 211, 153, 0.1);
  --sev-warn:       #fbbf24;   /* Warning — amber */
  --sev-warn-bg:    rgba(251, 191, 36, 0.1);
  --sev-perf:       #60a5fa;   /* Performance — blue */
  --sev-perf-bg:    rgba(96, 165, 250, 0.1);

  /* Syntax Highlighting */
  --syn-keyword:    #c792ea;   /* SELECT, FROM, WHERE, JOIN — purple */
  --syn-function:   #82aaff;   /* COUNT(), MAX(), AVG() — blue */
  --syn-string:     #c3e88d;   /* 'string values' — green */
  --syn-number:     #f78c6c;   /* numeric literals — orange */
  --syn-operator:   #89ddff;   /* =, >, <, AND, OR — cyan */
  --syn-comment:    #546e7a;   /* -- comments — muted */
  --syn-table:      #ffcb6b;   /* table names — amber */
  --syn-column:     #e8e9f0;   /* column names — primary text */
  --syn-type:       #f07178;   /* data types — coral */
  --syn-line-num:   #3a3c50;   /* gutter line numbers */
  --syn-line-active:#2a2c40;   /* current line highlight */

  /* Typography */
  --text-primary:   #e8e9f0;
  --text-secondary: #8b8fa8;
  --text-muted:     #4b4e6a;

  /* Fonts */
  --font-ui:    'Inter', sans-serif;
  --font-mono:  'JetBrains Mono', monospace;
  --font-brand: 'Syne', sans-serif;
}
```

---

## 🏗️ LAYOUT ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────┐
│  TOPBAR (full width, 44px tall)                                         │
│  [Ko SNITCH logo]  [tabs row]                     [Avatar] [→ Run Query]│
├────────────────┬────────────────────────────────┬────────────────────────┤
│                │  EDITOR TOOLBAR (36px)          │                        │
│  FILE TREE     │  [filename] [lines] [dialect]   │  ANALYSIS PANEL        │
│  (200px fixed) │  [copy][wrap][expand] [actions] │  (320px fixed)         │
│                ├────────────────────────┬────────┤                        │
│  Project root  │                        │ MINI-  │  [Suggestions header]  │
│  └ Schemas     │   QUERY EDITOR         │ MAP    │  [Issue count badge]   │
│    └ tables    │   (main pane)          │ (80px) │                        │
│  └ Saved       │   Syntax highlighted   │        │  [Card 1 — expanded]   │
│    └ queries   │   SQL with line nums   │        │  [Card 2 — collapsed]  │
│  └ History     │   + gutter             │        │  [Card 3 — collapsed]  │
│                │                        │        │  [Card 4 — collapsed]  │
│  [Credits]     │                        │        │                        │
│  [Upgrade]     ├────────────────────────┴────────┤  [Fix All] [Dismiss]   │
│                │  STATUS BAR (22px)              │                        │
└────────────────┴─────────────────────────────────┴────────────────────────┘
```

---

## 📦 COMPONENT SPECIFICATIONS

---

### 1. TOPBAR (44px, full width)

```
background: #111218
border-bottom: 1px solid #1f2030
display: flex; align-items: center; padding: 0 16px; gap: 0
```

**Left — Logo:**
- Inline SNITCH SVG icon (24×24) + "SNITCH" in Syne 800, 15px, #e8e9f0, letter-spacing 0.08em
- Separator: `1px solid #1f2030; height: 20px; margin: 0 14px`

**Center — Tab Bar (flex: 1):**
- Horizontal scrollable tab strip, `overflow-x: auto; display: flex; gap: 0`
- Each tab: `padding: 0 14px; height: 44px; display: flex; align-items: center; gap: 8px; font-size: 12.5px; cursor: pointer; border-right: 1px solid #1f2030; white-space: nowrap`
- **Active tab:** `background: #0f1019; color: #e8e9f0; border-bottom: 2px solid #7c5cfc`
- **Idle tab:** `background: #0d0e14; color: #8b8fa8`
- Each tab has: colored file-type dot (4px circle) + filename + × close button
- File type dot colors:
  - `.sql` → `#7c5cfc` (brand violet)
  - `.js` → `#f7df1e` (yellow)
  - `.php` → `#777bb4` (php purple)
  - `.html` → `#e34c26` (orange)

**Render these tabs:**
1. `users_query.sql` ← active (violet dot)
2. `orders_join.sql` (violet dot)
3. `slow_index.sql` (violet dot)
4. `app_schema.sql` (violet dot)
5. `analytics.sql` (violet dot)

**Right:**
- Avatar circle: 28px, gradient `#7c5cfc → #a78bfa`, initials "IK", 11px white
- Name: "Ishraq Khan" 12px `var(--text-primary)`, email "ishraq@snitch.io" 10px `var(--text-muted)` below (stacked)
- Separator
- **Run Query button:** `background: #7c5cfc; color: white; border: none; border-radius: 6px; padding: 6px 14px; font-size: 12.5px; font-weight: 600; font-family: var(--font-ui); cursor: pointer; display: flex; align-items: center; gap: 6px`
  - Left icon: ▶ play triangle (SVG, 10×10, white fill)
  - Text: "Run Query"
  - Hover: `background: #5b3fd4`

---

### 2. FILE TREE SIDEBAR (200px fixed)

```
background: #111218
border-right: 1px solid #1f2030
display: flex; flex-direction: column
overflow: hidden
```

**Search bar (top):**
- `margin: 8px; background: #0d0e14; border: 1px solid #1f2030; border-radius: 6px; padding: 6px 10px`
- Magnifier SVG icon (12px, muted) + placeholder "Search..." in 12px `var(--text-muted)`
- Right: `×` clear button (appears when focused)

**Tree structure** (scrollable, `flex: 1; overflow-y: auto; padding: 4px 0`):

Each row: `height: 26px; display: flex; align-items: center; padding-left: Npx; cursor: pointer; font-size: 12.5px`
- Folder row hover: `background: #1c1d29`
- File row hover: `background: rgba(124,92,252,0.06)`
- **Active file:** `background: rgba(124,92,252,0.12); color: #e8e9f0`
- Chevron: `▶` (collapsed) or `▼` (expanded), 10px, `var(--text-muted)`

**Tree data to render:**
```
▼ 📁 Project 01                        [indent: 0]
  ▼ 📁 Schemas                         [indent: 12]
    ▼ 📁 Tables                         [indent: 24]
      ◉ users_query.sql  ← ACTIVE       [indent: 36]
      ◦ orders_join.sql                  [indent: 36]
      ◦ products.sql                     [indent: 36]
      ◦ app_schema.sql                   [indent: 36]
    ▼ 📁 Views                          [indent: 24]
      ◦ user_stats.sql                   [indent: 36]
      ◦ order_summary.sql                [indent: 36]
  ▼ 📁 Saved Queries                   [indent: 12]
    ▼ 📁 Analytics                      [indent: 24]
      ◦ monthly_revenue.sql              [indent: 36]
      ◦ user_retention.sql               [indent: 36]
    ◦ slow_index.sql                     [indent: 24]
  ▼ 📁 History                         [indent: 12]
    ◦ query_2024_01_15.sql               [indent: 24]
    ◦ query_2024_01_14.sql               [indent: 24]
  ▼ 📁 Debugger                        [indent: 0]
  ▼ 📁 Generator                       [indent: 0]
  ▼ 📁 Docs                            [indent: 0]
```

File icons: `.sql` files get a small violet `SQL` badge (5×5 colored rect), folders get `📁` emoji rendered as SVG rect with fold.

**Bottom section (fixed, no scroll):**
```
border-top: 1px solid #1f2030; padding: 10px 12px
```
- Help row: `? Help & Support` — 12px, `var(--text-muted)`, cursor pointer
- Credits card:
  ```
  background: rgba(124,92,252,0.08)
  border: 1px solid rgba(124,92,252,0.2)
  border-radius: 8px
  padding: 10px 12px
  margin-top: 8px
  ```
  - "41 Credits left" — 13px, weight 600, `var(--text-primary)`
  - "Free plan" — 11px, `var(--text-muted)`
  - **Upgrade Now button:** full-width, `background: #7c5cfc; color: white; border: none; border-radius: 6px; height: 30px; font-size: 12px; font-weight: 600; cursor: pointer; margin-top: 8px`
  - Hover: `background: #5b3fd4`

---

### 3. EDITOR TOOLBAR (36px strip above editor)

```
background: #111218
border-bottom: 1px solid #1f2030
display: flex; align-items: center; padding: 0 14px; gap: 12px
```

**Left cluster:**
- Filename: `users_query.sql` — 12.5px, `var(--text-primary)`, weight 500
- Separator dot `·`
- Line count: `30 lines` — 12px, `var(--text-muted)`, mono
- Separator dot `·`
- Dialect badge: `SQL` — `background: rgba(124,92,252,0.12); color: var(--brand-text); border: 1px solid rgba(124,92,252,0.2); border-radius: 4px; padding: 1px 7px; font-size: 11px; font-weight: 600; font-family: var(--font-mono)`

**Right cluster (pushed with `margin-left: auto`):**
- Icon buttons (each 26×26, `background: transparent; border: none; border-radius: 5px; cursor: pointer; color: var(--text-secondary)`):
  - Copy icon (two overlapping rects SVG)
  - Word-wrap icon (arrow wrapping line SVG)
  - Expand icon (4-corner arrows SVG)
- Separator
- Action buttons row:
  - **Analyze** (active/primary): `background: #7c5cfc; color: white; border-radius: 6px; padding: 4px 12px; font-size: 12px; font-weight: 600`
  - **Debug**: `background: transparent; border: 1px solid #1f2030; color: var(--text-secondary); border-radius: 6px; padding: 4px 12px; font-size: 12px`
  - **AI Assistant**: `background: transparent; border: 1px solid #1f2030; color: var(--text-secondary); border-radius: 6px; padding: 4px 12px; font-size: 12px` — with sparkle icon ✨ 10px
  - **Generate**: `background: transparent; border: 1px solid #1f2030; color: var(--text-secondary); border-radius: 6px; padding: 4px 12px; font-size: 12px`
  - **All Tools ▾**: dropdown trigger, same style, with chevron

---

### 4. QUERY EDITOR (main pane)

```
background: #0f1019
flex: 1
overflow: hidden
display: flex
```

**Structure:** `[Gutter 44px] [Code area flex:1] [Minimap 80px]`

#### 4A. GUTTER (line numbers)
- `background: #0f1019; width: 44px; padding: 10px 0; border-right: 1px solid #191a28; flex-shrink: 0`
- Line numbers: `font-family: var(--font-mono); font-size: 12px; color: var(--syn-line-num); text-align: right; padding-right: 12px; line-height: 21px; user-select: none`
- **Active line number** (line 12 highlighted): `color: #e8e9f0`
- **Highlighted lines** (where issues are detected — lines 3, 12, 22): `color: var(--brand-text)`

#### 4B. CODE AREA
- `flex: 1; overflow-y: auto; overflow-x: auto; padding: 10px 0 10px 14px`
- Each line: `height: 21px; line-height: 21px; white-space: pre; font-family: var(--font-mono); font-size: 12.5px`
- **Active line (12):** `background: var(--syn-line-active)` — full-width highlight stripe
- **Issue gutter dots:** On lines with detected issues, render a 4px colored dot on the far left edge of the gutter (before line number) — violet for optimization, red for security

**Query to render (users_query.sql — pre-populated SQL with intentional issues):**

```sql
-- Users query with analytics join
-- Last modified: 2024-01-15

SELECT u.id, u.email, u.created_at,
       COUNT(o.id) AS total_orders,
       SUM(o.total_amount) AS revenue,
       AVG(o.total_amount) AS avg_order,
       MAX(o.created_at) AS last_order_date,
       (SELECT COUNT(*) 
        FROM user_sessions us 
        WHERE us.user_id = u.id) AS session_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
LEFT JOIN user_profiles up ON u.id = up.user_id
WHERE u.created_at >= '2023-01-01'
  AND u.status = 'active'
  AND u.deleted_at IS NULL
  AND o.status != 'cancelled'
ORDER BY revenue DESC
LIMIT 100
OFFSET 0;
```

**Syntax highlight mapping — apply `<span>` tags with inline `color` styles:**

| Token type | Color | Examples from query |
|------------|-------|---------------------|
| Comments `--` | `var(--syn-comment)` | Both `-- comment` lines |
| Keywords | `var(--syn-keyword)` | SELECT, FROM, WHERE, LEFT JOIN, ORDER BY, LIMIT, OFFSET, AS, ON, AND, IS NULL, NOT |
| Functions | `var(--syn-function)` | COUNT(), SUM(), AVG(), MAX() |
| String literals | `var(--syn-string)` | `'2023-01-01'`, `'active'`, `'cancelled'` |
| Numbers | `var(--syn-number)` | `100`, `0` |
| Table names | `var(--syn-table)` | `users`, `orders`, `user_sessions`, `user_profiles` |
| Column/aliases | `var(--syn-column)` | `u.id`, `o.total_amount`, etc. |
| Operators | `var(--syn-operator)` | `>=`, `!=`, `=`, `*` |
| Subquery parens | slightly dimmer | The correlated subquery block |

**Issue underlines:** Apply `border-bottom: 1px dashed` to problematic tokens:
- Correlated subquery (line 9–11): `border-bottom: 1px dashed var(--sev-opt)` (violet — optimization)
- `!=` operator (line 18): `border-bottom: 1px dashed var(--sev-sec)` (red — security — use `<>` instead)
- `SELECT *` if present, or nested subquery: `border-bottom: 1px dashed var(--sev-warn)`

#### 4C. MINIMAP (80px wide)
- `width: 80px; background: #0d0e14; border-left: 1px solid #191a28; overflow: hidden; position: relative; flex-shrink: 0`
- Render a scaled-down visual representation of the query — tiny SVG lines representing code density
- Each "line" in minimap: a thin horizontal rect, colored to match the general syntax of that line
- Comments: `var(--syn-comment)` lines
- Keywords: `var(--syn-keyword)` bursts
- The minimap has a **viewport indicator** — a semi-transparent rect showing the current scroll position: `background: rgba(124,92,252,0.12); border: 1px solid rgba(124,92,252,0.3)`
- This is purely decorative but essential to the IDE feel

---

### 5. STATUS BAR (22px, bottom of editor pane)

```
background: #0a0b10
border-top: 1px solid #1f2030
display: flex; align-items: center; padding: 0 14px; gap: 16px
font-family: var(--font-mono); font-size: 11px; color: var(--text-muted)
```

Items (left to right):
- `●` green dot + `Connected · snitch_prod_db`
- Separator `|`
- `UTF-8`
- Separator `|`
- `SQL · PostgreSQL 15`
- Separator `|`
- `Ln 12, Col 8`
- Push right: `4 issues detected` in amber

---

### 6. ANALYSIS / SUGGESTIONS PANEL (320px fixed right)

```
background: #111218
border-left: 1px solid #1f2030
display: flex; flex-direction: column
width: 320px; flex-shrink: 0
```

#### 6A. Panel Header
```
padding: 12px 16px
border-bottom: 1px solid #1f2030
display: flex; align-items: center; justify-content: space-between
```

**Left:**
- "Suggestions" — `font-family: 'Syne'; font-size: 14px; font-weight: 700; color: var(--text-primary)`
- `·` separator
- "Issues" label + badge: `background: rgba(248,113,113,0.15); color: #f87171; border: 1px solid rgba(248,113,113,0.25); border-radius: 10px; padding: 1px 7px; font-size: 11px; font-weight: 600; font-family: var(--font-mono)` — shows `4`

**Right:**
- Code score pill: `90 Code score` — `background: rgba(52,211,153,0.1); color: #34d399; border: 1px solid rgba(52,211,153,0.2); border-radius: 10px; padding: 3px 10px; font-size: 12px; font-weight: 600; font-family: var(--font-mono)`
- Refresh/re-analyze icon button: circular arrows SVG, 16×16, `var(--text-muted)`, hover `var(--brand-text)`

#### 6B. Suggestion Cards (scrollable list)

Container: `flex: 1; overflow-y: auto; padding: 12px`

**Each card:**
```css
.suggestion-card {
  background: var(--bg-suggestion);
  border: 1px solid var(--border);
  border-radius: 8px;
  margin-bottom: 8px;
  overflow: hidden;
  transition: border-color 0.15s;
}
.suggestion-card:hover {
  border-color: rgba(124,92,252,0.3);
}
.suggestion-card.expanded {
  border-color: rgba(124,92,252,0.4);
}
```

**Card header row** (always visible, clickable to expand/collapse):
```css
.card-header {
  padding: 10px 14px;
  display: flex; align-items: center; gap: 8px; cursor: pointer;
}
```

- **Severity icon:** 14×14 SVG circle with severity-colored fill (see below)
- **Title:** 13px, `var(--text-primary)`, weight 500, `flex: 1`
- **Category badge:** pill with severity color bg/text (see severity system below)
- **Chevron:** `∨` (expanded) or `›` (collapsed), 12px, `var(--text-muted)`

**Severity icon system:**
| Type | Icon | Color | Badge text |
|------|------|-------|------------|
| Optimization | ⚡ lightning bolt SVG | `var(--sev-opt)` violet | `Optimization` |
| Security | ⚠ triangle SVG | `var(--sev-sec)` red | `Security` |
| Readability | ○ circle SVG | `var(--sev-read)` green | `Readability` |
| Performance | ⧖ clock SVG | `var(--sev-perf)` blue | `Performance` |

Badge style: `background: var(--sev-X-bg); color: var(--sev-X); border-radius: 4px; padding: 1px 7px; font-size: 10px; font-weight: 600; letter-spacing: 0.04em`

**Expanded card body:**
```css
.card-body {
  padding: 0 14px 14px;
  border-top: 1px solid var(--border-subtle);
}
```

- **Description text:** 12px, `var(--text-secondary)`, line-height 1.6, `margin: 8px 0`
  - Use code inline spans for filenames/functions: `<code style="color: var(--brand-text); background: rgba(124,92,252,0.1); padding: 1px 5px; border-radius: 3px; font-family: var(--font-mono); font-size: 11px">`

- **"Suggestion:" label** + suggestion text in 12px italic `var(--text-secondary)`

- **Code fix block:**
  ```css
  .code-fix {
    background: var(--bg-inlay);
    border: 1px solid var(--border);
    border-radius: 6px;
    padding: 10px 12px;
    margin: 8px 0;
    font-family: var(--font-mono);
    font-size: 11.5px;
    line-height: 1.7;
    overflow-x: auto;
    position: relative;
  }
  ```
  - Copy icon button (top-right of code block): 12×12 SVG, `var(--text-muted)`, `position: absolute; top: 8px; right: 8px`
  - Code is syntax-highlighted with the same color system as the editor

- **Action buttons row:**
  ```css
  display: flex; gap: 8px; margin-top: 10px; justify-content: flex-end
  ```
  - **Apply button:** `background: var(--brand); color: white; border: none; border-radius: 5px; padding: 5px 14px; font-size: 12px; font-weight: 600; cursor: pointer` — hover: `background: var(--brand-dim)`
  - **Dismiss button:** `background: transparent; border: 1px solid var(--border); color: var(--text-secondary); border-radius: 5px; padding: 5px 10px; font-size: 12px; cursor: pointer` — hover: `border-color: rgba(248,113,113,0.4); color: var(--sev-sec)`

---

### 6C. THE 4 SUGGESTION CARDS (content)

---

**CARD 1 — EXPANDED BY DEFAULT**

- Severity: Optimization (violet)
- Title: "Improve Loop Efficiency"
- Category badge: `Optimization`
- Description: "Detected correlated subquery in `optimizeCode.js` that can be reduced to O(n). Correlated subqueries execute once per row — on large datasets this causes N+1 query patterns."
- Suggestion: "Use `LEFT JOIN` with aggregation instead of a correlated subquery for session counts."
- Code fix:
```sql
LEFT JOIN (
  SELECT user_id,
         COUNT(*) AS session_count
  FROM user_sessions
  GROUP BY user_id
) us ON u.id = us.user_id
```
Apply + Dismiss buttons visible.

---

**CARD 2 — COLLAPSED**

- Severity: Security (red)
- Title: "Potential Vulnerability in API Call"
- Category badge: `Security`
*(collapsed — only header visible)*

---

**CARD 3 — COLLAPSED**

- Severity: Readability (green)
- Title: "Simplify Function Names"
- Category badge: `Readability`
*(collapsed — only header visible)*

---

**CARD 4 — COLLAPSED**

- Severity: Performance (blue)
- Title: "Missing Index on Filter Column"
- Category badge: `Performance`
*(collapsed — only header visible)*

---

#### 6D. Panel Footer (Fix All / Dismiss All)

```
border-top: 1px solid #1f2030
padding: 12px 16px
display: flex; gap: 8px
```

- **Fix All Issues** button: full-width (flex: 1), `background: var(--brand); color: white; border: none; border-radius: 6px; height: 36px; font-size: 13px; font-weight: 600; cursor: pointer`
  - Has `⚡` icon on left
  - Hover: `background: var(--brand-dim)`
  - On click: animate all collapsed cards to briefly flash, then show a toast "4 fixes applied"

- **Dismiss All** button: `background: transparent; border: 1px solid var(--border); color: var(--text-secondary); border-radius: 6px; height: 36px; padding: 0 14px; font-size: 12px; cursor: pointer`

---

## ✨ INTERACTIONS & BEHAVIORS

### Query Editor
- **Editable textarea** behind the syntax-highlighted display — user can type real SQL
- When user types and pauses 1.5s: trigger re-highlight pass
- **Line numbers** update dynamically as lines are added/removed
- **Current line tracking:** clicking in editor updates `Ln X, Col Y` in status bar
- **Analyze button:** on click — run a basic client-side SQL pattern detector:
  ```js
  // Detect these patterns and generate suggestion cards:
  patterns = [
    { regex: /SELECT\s+\*/, severity: 'opt', title: 'Avoid SELECT *', ... },
    { regex: /\bNOT IN\b/, severity: 'perf', title: 'NOT IN performance issue', ... },
    { regex: /correlated subquery/, severity: 'opt', title: 'Correlated subquery detected', ... },
    { regex: /!=/, severity: 'sec', title: 'Use <> for ANSI compliance', ... },
    { regex: /WHERE.*LIKE\s+'%/, severity: 'perf', title: 'Leading wildcard prevents index use', ... },
    { regex: /ORDER BY \d+/, severity: 'read', title: 'Use column names not ordinals', ... },
  ]
  ```
- Detected issues get underline markers added to the editor
- Issue count badge in analysis panel header updates
- Code score recalculates: `100 - (issues.length * 5)` — clamped 0–100

### Suggestion Cards
- Click card header → toggle expanded/collapsed with smooth `max-height` transition (0.25s ease)
- Only one card expanded at a time (accordion behavior)
- **Apply button:** removes that suggestion card with fade-out animation, decrements issue count, re-scores code, flashes the relevant editor lines green briefly
- **Dismiss button:** removes card with fade-out, decrements count but doesn't re-score

### File Tree
- Click folder → toggle open/closed (chevron rotates 90°)
- Click file → updates active file highlight, updates tab bar active tab, updates editor content to a different pre-loaded query snippet per file

### Tab Bar
- Click tab → switches active file (same as clicking file in tree)
- Click × → closes tab (with a subtle scale-out animation)
- Middle-click equivalent → close tab

### Run Query button
- On click: status bar briefly shows `Executing...` in amber
- After 800ms: shows `42 rows returned · 127ms` in green
- A subtle ripple animation on the button itself

### Keyboard shortcuts (implement with keydown listener)
- `Ctrl+Enter` → Run Query
- `Ctrl+Shift+A` → Analyze
- `Ctrl+/` → Toggle comment on current line

---

## 🔧 TECHNICAL IMPLEMENTATION

### HTML Skeleton
```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>SNITCH — Query Analyzer</title>
  <link href="https://fonts.googleapis.com/css2?family=Syne:wght@700;800&family=JetBrains+Mono:wght@400;500;600&family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
  <style>/* ALL CSS */</style>
</head>
<body>
  <div id="app">
    <div id="topbar">...</div>
    <div id="workspace">
      <aside id="file-tree">...</aside>
      <div id="editor-pane">
        <div id="editor-toolbar">...</div>
        <div id="editor-body">
          <div id="gutter">...</div>
          <div id="code-area">...</div>
          <div id="minimap">...</div>
        </div>
        <div id="status-bar">...</div>
      </div>
      <aside id="analysis-panel">...</aside>
    </div>
  </div>
  <script>/* ALL JS */</script>
</body>
</html>
```

### Core CSS Rules
```css
* { box-sizing: border-box; margin: 0; padding: 0; }
html, body { height: 100%; overflow: hidden; }
#app { 
  display: flex; flex-direction: column; height: 100vh;
  background: var(--bg-root); color: var(--text-primary);
  font-family: var(--font-ui);
}
#workspace {
  display: flex; flex: 1; overflow: hidden;
}
#file-tree { width: 200px; flex-shrink: 0; /* ... */ }
#editor-pane { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
#editor-body { flex: 1; display: flex; overflow: hidden; }
#code-area { 
  flex: 1; overflow: auto; padding: 10px 0 10px 14px;
  /* Custom scrollbar */
}
#analysis-panel { width: 320px; flex-shrink: 0; display: flex; flex-direction: column; }
```

### Custom Scrollbar (apply to all scrollable areas)
```css
::-webkit-scrollbar { width: 6px; height: 6px; }
::-webkit-scrollbar-track { background: transparent; }
::-webkit-scrollbar-thumb { background: #1f2030; border-radius: 3px; }
::-webkit-scrollbar-thumb:hover { background: #2a2b3d; }
```

### JavaScript Architecture
```
// === CONFIG: QUERIES & SUGGESTIONS DATA ===
// (pre-defined SQL for each file, pre-built suggestion cards)

// === SQL SYNTAX HIGHLIGHTER ===
// tokenize(sql) → HTML string with <span> colored tags
// patterns: keywords[], functions[], operators[], etc.
// returns highlighted HTML preserving line structure

// === LINE NUMBER RENDERER ===
// syncGutter(lineCount) — keeps gutter in sync with code area scroll

// === MINIMAP RENDERER ===
// renderMinimap(lines) — draws scaled SVG representation

// === SUGGESTION ENGINE ===
// analyze(sql) → issues[] — pattern-based SQL linter
// renderSuggestions(issues) — builds suggestion card DOM
// calculateScore(issues) → 0-100

// === CARD ACCORDION ===
// toggleCard(id) — expand/collapse with CSS max-height transition

// === FILE TREE ===
// toggleFolder(id), selectFile(id) — tree state management

// === TAB MANAGEMENT ===
// openTab(file), closeTab(file), switchTab(file)

// === EDITOR EVENTS ===
// onInput, onKeydown, onMouseup — cursor tracking, shortcuts

// === QUERY RUNNER ===
// runQuery() — simulated async execution with status bar updates

// === ANIMATIONS ===
// applyFix(cardId), dismissCard(cardId), showToast(msg)

// === INIT ===
// boot sequence: render tree → load default query → analyze → animate in
```

### Syntax Highlighter Implementation
```js
function highlightSQL(sql) {
  const keywords = ['SELECT','FROM','WHERE','JOIN','LEFT','RIGHT','INNER',
    'OUTER','ON','AS','AND','OR','NOT','IN','IS','NULL','ORDER','BY',
    'GROUP','HAVING','LIMIT','OFFSET','INSERT','UPDATE','DELETE',
    'CREATE','ALTER','DROP','INDEX','TABLE','VIEW','WITH','DISTINCT',
    'UNION','ALL','EXISTS','BETWEEN','LIKE','CASE','WHEN','THEN',
    'ELSE','END','COUNT','SUM','AVG','MAX','MIN'];
  // Build regex, replace tokens with <span style="color: ...">
  // Process in order: comments first (greedy), then strings, then tokens
  // Preserve whitespace and newlines
}
```

### Load Animation Sequence
1. `0ms` — page renders, all panels already in place (no slide-in)
2. `0ms` — file tree items fade in top-to-bottom, 15ms stagger, 200ms each
3. `100ms` — editor content types in: simulate typing by revealing characters progressively, or just fade in the whole block at once (fade is cleaner)
4. `300ms` — analysis panel header fades in
5. `500ms` — Card 1 slides down open from height 0 to full height (0.4s ease-out)
6. `700ms` — Cards 2–4 fade in collapsed
7. `900ms` — Code score badge counts up from 0 to 90 using `requestAnimationFrame`
8. `1100ms` — Status bar items fade in left to right
9. Subtle: minimap viewport indicator slowly drifts down then back — indicating the editor is "live"

---

## 📐 EXACT DIMENSIONS

| Element | Dimension |
|---------|-----------|
| Topbar height | 44px |
| File tree width | 200px |
| Analysis panel width | 320px |
| Editor toolbar height | 36px |
| Status bar height | 22px |
| Gutter width | 44px |
| Minimap width | 80px |
| Tab height | 44px (full topbar height) |
| Card padding | 10px 14px |
| Font size — editor code | 12.5px |
| Line height — editor | 21px |
| Font size — UI labels | 12–13.5px |
| Font size — mono numbers | 12px |

---

## 🚫 DO NOT

- ❌ Use `contenteditable` for the editor — use a real `<textarea>` overlaid behind the highlight layer
- ❌ Import CodeMirror, Monaco, or any editor library — build it manually
- ❌ Use `<canvas>` — all rendering is DOM/SVG
- ❌ Use shadows or glows — only `border-color` changes for depth
- ❌ Leave the query editor empty — pre-populate it with the full SQL above
- ❌ Make the right panel fixed-content — it must be driven by the actual `analyze()` function
- ❌ Add a light mode — strictly dark only
- ❌ Use gradients on any surface except the avatar circle
- ❌ Round numbers without mono font — every number uses `var(--font-mono)`

---

## ✅ DEFINITION OF DONE

- [ ] Full viewport IDE layout renders correctly — no overflow, no scroll on outer body
- [ ] SNITCH logo with SVG icon renders in topbar
- [ ] 5 tabs render in tab bar, `users_query.sql` active with violet underline
- [ ] File tree renders with correct indentation, active file highlighted, folders toggleable
- [ ] Credits card + Upgrade Now button at bottom of sidebar
- [ ] Editor toolbar: filename, line count, SQL badge, all action buttons
- [ ] Query editor: 20+ lines of pre-populated SQL, fully syntax-highlighted
- [ ] Line gutter: numbered, active line tracked, issue dots on affected lines
- [ ] Minimap: visible with viewport indicator rect
- [ ] Status bar: connection, encoding, dialect, cursor position, issue count
- [ ] Analysis panel header: "Suggestions · Issues 4" + "90 Code score" badge
- [ ] Card 1 (Improve Loop Efficiency) expanded by default with code fix block
- [ ] Cards 2–4 collapsed, correct severity icons and category badges
- [ ] Apply button on Card 1 removes card + updates counts
- [ ] Accordion: clicking collapsed card expands it, collapses others
- [ ] Analyze button re-runs SQL linting, updates suggestions
- [ ] Run Query button shows simulated execution feedback in status bar
- [ ] Fix All Issues button applies all fixes with toast notification
- [ ] Code score badge animates counting up on load
- [ ] All load animations run in sequence
- [ ] Custom scrollbars on all overflow containers
- [ ] Keyboard shortcuts: Ctrl+Enter (run), Ctrl+Shift+A (analyze), Ctrl+/ (comment)

---

*Agent prompt for Claude Opus 4.6 | SNITCH Query Analyzer v1.0*  
*Part of the SNITCH DB Analysis Platform suite*
