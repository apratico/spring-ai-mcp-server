# CNC Milling Center — Maintenance Reference

Covers the 5-axis (**CNC-07**) and 3-axis (**CNC-08**) milling centers in the machining cell.

## Daily checks

- Coolant tank level above LOW mark; top up with the workshop emulsion concentrate.
- Inspect the way wipers; replace any wiper that no longer sweeps cleanly within the same shift.
- Check air pressure at the regulator: 6 bar ± 0.3 bar.

## Weekly checks

- Verify spindle taper cleanliness with a clean rag (not compressed air — risks bearing damage).
- Test tool-change cycle in dry-run; the cycle time should remain within 3.6 ± 0.2 s on the 5-axis and 2.4 ± 0.2 s on the 3-axis machine.
- Calibrate touch probe against the master ring; deviation > 5 µm requires probe re-zero.

## Performance signals

- Spindle current rising slowly across a shift on the same NC program is a leading indicator of insert wear or coolant degradation.
- Vibration above **3.5 mm/s RMS** at the spindle housing during finishing passes points to bearing wear or improper tool clamping.
- A drop of more than 8 % in cycle-count rate vs. baseline at constant feed rate suggests an axis-drive issue or a coolant-pressure dip.

## Spare-part cross-reference

- Spindle bearing kit (5-axis): **SPR-BRG-SPNDL-5AX**
- Way wiper set (universal): **SPR-WIPE-CNC**
- Coolant pump: **SPR-PUMP-CLNT-3KW**
- Aluminum bar feedstock: **RAW-ALU-6061**
