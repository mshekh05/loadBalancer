# Dashboard Theme Specification — Light SaaS Style

> **Instructions for Claude Opus:** Apply this theme to the existing dashboard by replacing all color, typography, spacing, and component styling to match the design system below. Do not change the data, logic, or layout structure — only the visual presentation. Output complete, production-ready code with all styles applied inline, in `<style>` tags, or via CSS variables as appropriate for the framework in use.

---

## 1. Design Philosophy

**Tone:** Clean, professional, trust-building SaaS dashboard. Light mode only. High information density without feeling cluttered. Every element is softly rounded and subtly shadowed — nothing harsh, nothing flat. The aesthetic is "polished B2B product" — calm blue primary, crisp white surfaces, generous white space, and restrained use of color (only for data signals).

---

## 2. Color Palette

Define all colors as CSS custom properties on `:root`.

```css
:root {
  /* Backgrounds */
  --bg-app:        #F4F6F9;   /* overall app background — very light blue-gray */
  --bg-surface:    #FFFFFF;   /* cards, panels, sidebar */
  --bg-hover:      #F0F4FF;   /* interactive element hover state */
  --bg-active:     #EEF2FF;   /* active/selected element background */

  /* Brand / Primary */
  --primary:       #3B6FE8;   /* primary blue — buttons, active states, links */
  --primary-light: #EEF2FF;   /* primary tint — badges, highlights */
  --primary-dark:  #2A56C6;   /* primary hover */

  /* Semantic / Signal Colors */
  --success:       #22C55E;   /* positive delta, up trend */
  --success-bg:    #DCFCE7;   /* success pill background */
  --danger:        #EF4444;   /* negative delta, down trend, errors */
  --danger-bg:     #FEE2E2;   /* danger pill background */
  --warning:       #F97316;   /* warnings, third category accent */
  --warning-bg:    #FFEDD5;   /* warning pill background */
  --info:          #3B82F6;   /* informational, first category accent */
  --info-bg:       #DBEAFE;   /* info pill background */

  /* Text */
  --text-primary:  #111827;
  --text-secondary:#6B7280;
  --text-muted:    #9CA3AF;
  --text-link:     #3B6FE8;

  /* Borders */
  --border:        #E5E7EB;
  --border-strong: #D1D5DB;

  /* Chart Colors */
  --chart-1:       #3B6FE8;
  --chart-2:       #22C55E;
  --chart-3:       #F97316;
  --chart-4:       #A855F7;
  --chart-fill:    rgba(59, 111, 232, 0.08);

  /* Sidebar CTA gradient */
  --cta-gradient-from: #3B6FE8;
  --cta-gradient-to:   #6366F1;
}
```

---

## 3. Typography

Use **`DM Sans`** from Google Fonts.

```html
<link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
```

```css
:root {
  --font-base: 'DM Sans', sans-serif;

  --text-xs:   11px;
  --text-sm:   12px;
  --text-base: 13px;
  --text-md:   14px;
  --text-lg:   16px;
  --text-xl:   20px;
  --text-2xl:  24px;
  --text-3xl:  28px;

  --font-normal:   400;
  --font-medium:   500;
  --font-semibold: 600;
  --font-bold:     700;

  --leading-tight: 1.2;
  --leading-base:  1.5;
}

body {
  font-family: var(--font-base);
  font-size: var(--text-base);
  font-weight: var(--font-normal);
  color: var(--text-primary);
  background-color: var(--bg-app);
  line-height: var(--leading-base);
  -webkit-font-smoothing: antialiased;
}
```

### Type Roles

| Role | Size | Weight | Color |
|------|------|--------|-------|
| Page title | `28px` | 700 | `--text-primary` |
| Section heading | `16px` | 600 | `--text-primary` |
| Large metric / KPI number | `28–36px` | 700 | `--text-primary` |
| Card label | `12px` | 500 | `--text-secondary` |
| Delta / change badge | `11px` | 600 | contextual |
| Table header | `11px` | 600 uppercase | `--text-muted` |
| Table body | `13px` | 400 | `--text-primary` |
| Nav label | `13px` | 500 | `--text-secondary` (active: `--primary`) |
| Sidebar section label | `10px` | 600 uppercase | `--text-muted` |

---

## 4. Spacing System

8px base grid. All paddings, margins, and gaps must be multiples of 4px.

```css
:root {
  --space-1:  4px;
  --space-2:  8px;
  --space-3:  12px;
  --space-4:  16px;
  --space-5:  20px;
  --space-6:  24px;
  --space-8:  32px;
  --space-10: 40px;
}
```

| Context | Value |
|---------|-------|
| Card internal padding | `20px 24px` |
| Content area padding | `24px 28px` |
| Sidebar item padding | `9px 20px` |
| Table cell padding | `12px 16px` |
| Topbar height | `56px` |
| Sidebar width | `220px` |

---

## 5. Elevation & Shadows

```css
:root {
  --shadow-xs: 0 1px 2px rgba(0, 0, 0, 0.04);
  --shadow-sm: 0 1px 4px rgba(0, 0, 0, 0.06), 0 0 0 1px rgba(0, 0, 0, 0.03);
  --shadow-md: 0 4px 12px rgba(0, 0, 0, 0.07), 0 1px 3px rgba(0, 0, 0, 0.04);
  --shadow-lg: 0 8px 24px rgba(0, 0, 0, 0.09);
}
```

| Element | Shadow |
|---------|--------|
| Cards | `--shadow-sm` |
| Dropdowns | `--shadow-md` |
| Modals | `--shadow-lg` |
| Sidebar | `box-shadow: 1px 0 0 var(--border)` (right edge only) |

---

## 6. Border Radius

```css
:root {
  --radius-sm:   6px;
  --radius-md:   10px;
  --radius-lg:   14px;
  --radius-xl:   20px;
  --radius-full: 9999px;
}
```

| Element | Radius |
|---------|--------|
| Pills, badges, avatars | `--radius-full` |
| Inputs, small buttons | `--radius-md` |
| Cards, panels | `--radius-lg` |
| Modals, large cards | `--radius-xl` |

---

## 7. Layout Structure

```
┌─────────────────────────────────────────┐
│  Sidebar (220px)  │  Main Content Area  │
│                   │  ┌───────────────┐  │
│                   │  │    Topbar     │  │
│                   │  ├───────────────┤  │
│                   │  │    Content    │  │
│                   │  └───────────────┘  │
└─────────────────────────────────────────┘
```

```css
.app-shell {
  display: flex;
  height: 100vh;
  overflow: hidden;
  background: var(--bg-app);
}

.sidebar {
  width: 220px;
  flex-shrink: 0;
  background: var(--bg-surface);
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  height: 56px;
  background: var(--bg-surface);
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  padding: 0 var(--space-6);
  gap: var(--space-4);
  flex-shrink: 0;
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding: var(--space-6) var(--space-8);
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}
```

### Common Grid Layouts

```css
/* KPI stat cards row */
.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: var(--space-5);
}

/* Main content + right panel */
.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: var(--space-5);
}
```

---

## 8. Component Styles

### 8.1 Card

```css
.card {
  background: var(--bg-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
  padding: 20px 24px;
}
```

### 8.2 KPI / Stat Card

```css
.stat-card {
  background: var(--bg-surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
  box-shadow: var(--shadow-sm);
  padding: 18px 20px;
  position: relative;
}

.stat-card__label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--text-secondary);
}

.stat-card__value {
  font-size: 28px;
  font-weight: var(--font-bold);
  color: var(--text-primary);
  line-height: 1.1;
  margin: 6px 0 8px;
}

.stat-card__icon {
  position: absolute;
  top: 18px;
  right: 18px;
  width: 32px;
  height: 32px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-light);
  color: var(--primary);
}

.stat-card__compare {
  font-size: var(--text-xs);
  color: var(--text-muted);
}
```

### 8.3 Delta / Change Badge

```css
.delta {
  display: inline-flex;
  align-items: center;
  gap: 2px;
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
  padding: 2px 7px;
  border-radius: var(--radius-full);
}

.delta--up   { color: var(--success); background: var(--success-bg); }
.delta--down { color: var(--danger);  background: var(--danger-bg);  }
```

### 8.4 Sidebar Navigation

```css
.sidebar-logo {
  padding: 18px 20px 12px;
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--text-primary);
  border-bottom: 1px solid var(--border);
  margin-bottom: var(--space-3);
}

.nav-section-label {
  font-size: 10px;
  font-weight: var(--font-semibold);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--text-muted);
  padding: var(--space-3) 20px var(--space-1);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: 9px 20px;
  font-size: var(--text-base);
  font-weight: var(--font-medium);
  color: var(--text-secondary);
  text-decoration: none;
  cursor: pointer;
  position: relative;
  transition: background 150ms ease, color 150ms ease;
}

.nav-item:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.nav-item.active {
  background: var(--bg-active);
  color: var(--primary);
  font-weight: var(--font-semibold);
}

/* Left accent bar on active item */
.nav-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 4px;
  bottom: 4px;
  width: 3px;
  background: var(--primary);
  border-radius: 0 var(--radius-sm) var(--radius-sm) 0;
}

.nav-item__icon {
  width: 16px;
  height: 16px;
  opacity: 0.7;
  flex-shrink: 0;
  color: currentColor;
}

.nav-item.active .nav-item__icon { opacity: 1; }

.nav-badge {
  margin-left: auto;
  background: var(--primary);
  color: #fff;
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
  padding: 1px 7px;
  border-radius: var(--radius-full);
  min-width: 20px;
  text-align: center;
}
```

### 8.5 Collapsible Sub-menu

```css
.nav-submenu { padding-left: 38px; }

.nav-submenu .nav-item {
  font-size: var(--text-sm);
  padding: 6px 20px 6px 0;
  color: var(--text-muted);
}

.nav-submenu .nav-item.active {
  color: var(--primary);
  background: transparent;
}

.nav-submenu .nav-item.active::before { display: none; }
```

### 8.6 Sidebar Bottom CTA Card

```css
.sidebar-cta {
  margin: auto 12px 12px;
  padding: 16px;
  background: linear-gradient(135deg, var(--cta-gradient-from), var(--cta-gradient-to));
  border-radius: var(--radius-lg);
  color: #fff;
}

.sidebar-cta__title {
  font-size: var(--text-md);
  font-weight: var(--font-bold);
  margin-bottom: 4px;
}

.sidebar-cta__body {
  font-size: var(--text-xs);
  opacity: 0.85;
  line-height: 1.5;
  margin-bottom: var(--space-4);
}

.sidebar-cta__button {
  width: 100%;
  background: #fff;
  color: var(--primary);
  border: none;
  border-radius: var(--radius-md);
  padding: 8px 0;
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  cursor: pointer;
  text-align: center;
}
```

### 8.7 Topbar

```css
.search-input {
  flex: 1;
  max-width: 320px;
  display: flex;
  align-items: center;
  gap: 8px;
  background: var(--bg-app);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  padding: 7px 12px;
  font-size: var(--text-sm);
  color: var(--text-muted);
}

.topbar__actions {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}

.icon-btn {
  width: 34px;
  height: 34px;
  border-radius: var(--radius-full);
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  color: var(--text-secondary);
  cursor: pointer;
  transition: background 150ms;
}

.icon-btn:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  object-fit: cover;
  border: 2px solid var(--border);
}
```

### 8.8 Buttons

```css
.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--primary);
  color: #fff;
  border: none;
  border-radius: var(--radius-md);
  padding: 8px 16px;
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  cursor: pointer;
  transition: background 150ms;
}

.btn-primary:hover { background: var(--primary-dark); }

.btn-outline {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--bg-surface);
  color: var(--text-primary);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  padding: 7px 14px;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  cursor: pointer;
  transition: background 150ms;
}

.btn-outline:hover { background: var(--bg-hover); }
```

### 8.9 Filter Chip / Date Picker Trigger

```css
.filter-chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--bg-surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  padding: 6px 12px;
  font-size: var(--text-sm);
  color: var(--text-primary);
  cursor: pointer;
}
```

### 8.10 Data Table

```css
.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: var(--text-sm);
}

.data-table thead th {
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--text-muted);
  padding: 10px 12px;
  text-align: left;
  border-bottom: 1px solid var(--border);
  white-space: nowrap;
}

.data-table tbody td {
  padding: 12px;
  color: var(--text-primary);
  border-bottom: 1px solid var(--border);
  vertical-align: middle;
}

.data-table tbody tr:last-child td { border-bottom: none; }
.data-table tbody tr:hover td      { background: var(--bg-hover); }
```

### 8.11 Section Header Pattern

```css
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-4);
}

.section-header__title {
  font-size: var(--text-md);
  font-weight: var(--font-semibold);
  color: var(--text-primary);
}

.overflow-btn {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
}

.overflow-btn:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}
```

### 8.12 Page Title Area

```css
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-6);
}

.page-title {
  font-size: 26px;
  font-weight: var(--font-bold);
  color: var(--text-primary);
  line-height: 1.2;
}

.page-actions {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}
```

### 8.13 Segmented Progress Bar

For multi-category breakdowns rendered as a horizontal bar.

```css
.segment-bar {
  display: flex;
  height: 6px;
  border-radius: var(--radius-full);
  overflow: hidden;
  gap: 2px;
  margin: 8px 0 12px;
}

.segment-bar__fill {
  border-radius: var(--radius-full);
  transition: width 0.5s ease;
}
/* Apply --chart-1 / --chart-2 / --chart-3 as background per segment */
```

### 8.14 Circular / Gauge Progress

SVG-based semi-circle or full donut indicator.

```
Track stroke:    var(--border),   stroke-width: 10
Progress stroke: var(--success),  stroke-width: 10, stroke-linecap: round
Value label:     28px, --font-bold, --text-primary, centered
Sub-label:       11px, --text-muted, centered
```

---

## 9. Chart Styling

| Property | Value |
|----------|-------|
| Primary line color | `var(--chart-1)` |
| Line width | `2px` |
| Area fill under line | `var(--chart-fill)` |
| Curve type | `monotone` |
| Grid line color | `var(--border)` |
| Grid line style | dashed, `strokeDasharray="4 4"` |
| Grid opacity | `0.6` |
| Axis label style | `11px`, `var(--text-muted)`, no tick marks |
| Axis lines | hidden |
| Tooltip background | `var(--bg-surface)` |
| Tooltip border | `1px solid var(--border)` |
| Tooltip shadow | `var(--shadow-md)` |
| Tooltip border-radius | `10px` |
| Hover dot | `5px radius`, fill `var(--chart-1)`, stroke `#fff 2px` |
| Bar fill | `var(--chart-1)`, `border-radius: 4px 4px 0 0` |
| Inactive bars | `var(--border)` |

---

## 10. Iconography

- Use **Lucide Icons** or **Heroicons** (outline variant)
- Default size: `16px` (stat card icons: `18px`)
- Always use `currentColor` — no hardcoded fill values
- Inactive nav icons: `opacity: 0.7`, active: `opacity: 1`

---

## 11. Transitions

```css
:root {
  --transition-fast: 150ms ease;
  --transition-base: 200ms ease;
}

button, a, .nav-item, input {
  transition: background var(--transition-fast),
              color var(--transition-fast),
              border-color var(--transition-fast),
              box-shadow var(--transition-fast);
}
```

- No hover transforms on cards — data-dense dashboards avoid lift/scale effects
- Charts animate on mount: `1s ease-in` for lines, `0.5s` for bars

---

## 12. Scrollbars

```css
::-webkit-scrollbar        { width: 5px; height: 5px; }
::-webkit-scrollbar-track  { background: transparent; }
::-webkit-scrollbar-thumb  { background: var(--border-strong); border-radius: var(--radius-full); }
::-webkit-scrollbar-thumb:hover { background: var(--text-muted); }
```

---

## 13. Quick Reference

| Token | Value |
|-------|-------|
| App background | `#F4F6F9` |
| Surface (cards) | `#FFFFFF` |
| Primary | `#3B6FE8` |
| Success | `#22C55E` |
| Danger | `#EF4444` |
| Warning | `#F97316` |
| Text primary | `#111827` |
| Text secondary | `#6B7280` |
| Border | `#E5E7EB` |
| Font family | DM Sans |
| Base font size | `13px` |
| Card radius | `14px` |
| Card shadow | `0 1px 4px rgba(0,0,0,.06)` |
| Sidebar width | `220px` |
| Topbar height | `56px` |
| Card padding | `20px 24px` |
| Grid gap | `20px` |
