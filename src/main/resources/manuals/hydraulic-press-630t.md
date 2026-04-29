# 630-ton Hydraulic Press — Operations and Maintenance

Applicable to asset family **PRESS-03** (630-ton frame) and any clone in the same generation.

## Daily checks (every shift)

- Verify oil level in main reservoir is between MIN and MAX marks. Top up only with ISO VG 46 hydraulic oil.
- Confirm system idle pressure on the gauge: nominal **180 bar ± 5 bar**. A reading outside this band is the most common precursor to a hydraulic pressure fault (alarm E-204).
- Listen for cavitation noise from the main pump during the first ram cycle. A pulsing, rattling sound indicates air ingestion and must be escalated.
- Wipe the columns and check for visible oil weeping at the cylinder rod seal.

## Weekly checks

- Inspect the high-pressure hose between the pump assembly and the directional valve block. Replace any hose with surface cracks, kinks, or weeping at the swage.
- Sample the oil and check for water content > 200 ppm or particle contamination above ISO 4406 19/17/14. Either condition warrants a full filter change.
- Re-torque the tie-rod nuts to **1850 Nm** in star pattern. Loose tie-rods skew the ram and amplify vibration on the next stamp.

## Common faults

| Code  | Symptom                                       | Likely cause                                                                 | First action                                                                 |
|-------|-----------------------------------------------|------------------------------------------------------------------------------|------------------------------------------------------------------------------|
| E-204 | Hydraulic pressure below threshold            | Worn pump (pressure drop), oil level too low, internal valve leak, ruptured hose | Stop the press. Check oil level, then pump pressure at gauge. Replace pump assembly **SPR-HYD-PUMP-630** if pressure cannot be sustained at 180 bar with valves closed. |
| E-208 | Ram return slower than 1.2 m/s                | Clogged return-line filter, sticky 4/3 directional valve                     | Replace return filter cartridge first; if symptom persists, clean the valve spool. |
| E-211 | Oil temperature above 65 °C                   | Cooler fan failure, blocked cooler matrix, low coolant flow                  | Inspect and clean cooler matrix; verify thermostat trips at 60 °C.           |

## Spare-part cross-reference

- Pump assembly: **SPR-HYD-PUMP-630**
- Return-line filter cartridge: **SPR-FLT-RET-25**
- Tie-rod nut: **SPR-NUT-M64-12.9**
- Cylinder rod seal kit: **SPR-SEAL-CYL-630**

## Lockout-tagout

Before any internal inspection, follow the LOTO procedure documented in `safety-loto-procedure.md`. Bleed residual pressure via the manual relief valve until the gauge reads 0 bar. Never trust a pressure switch alone.
