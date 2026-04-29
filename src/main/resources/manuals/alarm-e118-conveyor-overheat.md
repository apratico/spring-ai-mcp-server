# Alarm E-118 — Conveyor Belt Motor Overheating

Triggered on packaging-line conveyors when the motor PT100 sensor exceeds 95 °C, or when the thermal-overload relay trips.

## Severity

**HIGH** — the line stops automatically. Restarting under thermal lockout damages the motor windings and accelerates bearing wear; do not bypass.

## Decision tree

1. **Allow the motor to cool to under 50 °C before any inspection.**
   The PLC will not clear the alarm until this is true. Use the IR thermometer at the motor end-bell.

2. **Inspect the cooling fan and shroud.**
   - Blocked or fouled with cardboard/plastic debris (common on LINE-A) → clean and re-arm.
   - Fan blades cracked → replace fan kit before re-arming.

3. **Hand-rotate the motor shaft.**
   - Smooth, even drag → bearings probably healthy. Run the line at 50 % speed for 15 minutes and re-check the PT100. If temperature exceeds 70 °C at 50 % load, escalate.
   - Notchy or rough → bearings worn. Plan motor replacement with **SPR-MOTOR-2KW**.

4. **Inspect drive pulley and belt tension.**
   A slipping belt causes the motor to draw current beyond nameplate (over-current ⇒ over-heat). Re-tension and re-test.

## When to open a maintenance ticket

If the PT100 trips again within the same shift after a clean-and-restart, open a CMMS ticket with **HIGH** priority and request stock check on **SPR-MOTOR-2KW**.

## Related manuals

- `conveyor-belt-line.md`
- `safety-loto-procedure.md`
