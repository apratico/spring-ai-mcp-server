# Alarm E-204 — Hydraulic Pressure Below Threshold

Triggered on hydraulic-press assets when the system pressure transducer reads below 80 % of the configured nominal value for more than 2 seconds during a build-up phase.

## Severity

**HIGH** — production-stopping. The press is automatically held in a safe state and cannot start a new cycle until the alarm is cleared.

## Decision tree

1. **Is the reservoir oil level within MIN/MAX?**
   - Below MIN → top up with ISO VG 46. Re-arm the press. If alarm re-triggers within 5 cycles, proceed to step 2.
   - In range → proceed to step 2.

2. **Is the pump producing nominal pressure with all directional valves closed?**
   - Yes (pressure climbs to nominal within 1.2 s) → look downstream: check counter-balance valve and main relief valve for internal leakage.
   - No → the pump is the suspect. Inspect for cavitation noise; replace pump assembly with the matching spare:
     - PRESS-02 → **SPR-HYD-PUMP-400**
     - PRESS-03 → **SPR-HYD-PUMP-630**

3. **Hose and fitting inspection**
   - Walk the entire pressure line. Any external weeping or surface cracks → replace the hose before re-arming.

## When to open a maintenance ticket

If steps 1 and 2 do not restore nominal pressure within one cycle, open a CMMS ticket with **HIGH** priority. Do not run the press in degraded mode — the failure pattern at low pressure includes uncontrolled ram drop on de-energization, which is a personnel hazard.

## Related manuals

- `hydraulic-press-630t.md`
- `hydraulic-press-400t.md`
- `safety-loto-procedure.md`
