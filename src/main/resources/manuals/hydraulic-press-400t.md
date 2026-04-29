# 400-ton Hydraulic Press — Operations and Maintenance

Applicable to asset family **PRESS-02** (400-ton frame).

## Daily checks (every shift)

- Reservoir oil level between MIN and MAX. Use ISO VG 46 hydraulic oil only.
- Idle system pressure: nominal **150 bar ± 4 bar**.
- Inspect ram and bolster for foreign objects before first cycle.
- Confirm two-hand-control palm buttons trigger within 0.5 s of each other; otherwise the safety relay locks the press.

## Weekly checks

- Replace breather filter every 250 operating hours (track via PLC cycle-count meter).
- Inspect E-stop circuits and light curtain alignment.
- Re-grease pitman bearings with EP-2 lithium grease through the four side fittings — two strokes each.

## Common faults

| Code  | Symptom                                          | Likely cause                                                  | First action                                                                                      |
|-------|--------------------------------------------------|---------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| E-202 | Pressure climb slower than 1.5 s to 150 bar       | Worn pump, internal valve bypass                              | Verify pump pressure under load; replace **SPR-HYD-PUMP-400** if degraded.                         |
| E-205 | Excessive ram drift (> 1 mm) at top of stroke     | Counter-balance valve leak                                    | Inspect and replace counter-balance valve cartridge.                                               |
| E-209 | Cycle count rising but ram does not extend        | Stuck directional valve, electrical fault on solenoid Y1       | Power-cycle valve solenoid; if persisting, trace the 24 V line and replace solenoid coil.          |

## Spare-part cross-reference

- Pump assembly: **SPR-HYD-PUMP-400**
- Counter-balance valve: **SPR-VLV-CBAL-400**
- Pitman bearing set: **SPR-BRG-PITMAN-2**
