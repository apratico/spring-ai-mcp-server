# Lockout-Tagout (LOTO) Procedure

Mandatory before any non-running maintenance task that involves access to moving parts, stored energy (hydraulic, pneumatic, gravitational) or live electrical circuits.

## Steps

1. **Notify** affected operators and the shift supervisor that the asset is going into LOTO.
2. **Shutdown** the machine via the local HMI to a safe stop state.
3. **Isolate** energy sources:
   - Electrical: open the main disconnect switch.
   - Hydraulic: close the supply isolation valve and bleed residual pressure via the manual relief valve until the gauge reads **0 bar**.
   - Pneumatic: close the regulator and vent the downstream line through the local exhaust port.
   - Stored mechanical energy: lower or block raised tooling (press ram, lifting tables).
4. **Lock** each isolation point with a personal padlock. Each technician working on the asset applies their own lock; the last person off site is the last to remove their lock.
5. **Tag** each lock with the technician's name, date and reason for LOTO.
6. **Verify** zero energy:
   - Try to start the machine from the HMI. It must NOT respond.
   - Read pressure gauges; all must read 0.
   - Verify electrical isolation with a calibrated voltage tester at the load side of the disconnect.

## Removing LOTO

Reverse order: verify the work area is clear, remove tags, remove locks, restore energy sources, perform a controlled re-start.

## Documentation

Every LOTO event must be logged in the CMMS as part of the originating maintenance ticket, including the technician(s) involved and the energy sources isolated. The audit trail is reviewed quarterly by the EHS officer.
