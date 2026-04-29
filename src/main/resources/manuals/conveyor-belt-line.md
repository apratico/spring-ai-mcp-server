# Packaging Line Conveyor — Maintenance Reference

Applicable to the conveyor segment of **LINE-A** and **LINE-B** packaging lines.

## Daily checks

- Walk the belt length and look for tears, edge fraying, or product debris jammed against the side guides.
- Listen at the drive motor for bearing growl or fan rattle.
- Confirm the no-back ratchet on inclined sections engages when the belt is stopped manually with the e-stop.

## Weekly checks

- Measure motor body temperature after one hour of continuous run. A reading above **75 °C** ambient correlates strongly with alarm **E-118 (conveyor belt motor overheating)** within the next two shifts; preemptive replacement is cheaper than line stoppage.
- Inspect belt-tension take-up; tension should be set so that the belt sag between two idlers does not exceed 1 % of the centre-to-centre distance.

## Failure modes

| Code  | Symptom                                    | Likely cause                                                  | First action                                                                              |
|-------|--------------------------------------------|---------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| E-118 | Conveyor belt motor overheating             | Bearing wear, blocked fan, undersized motor for ramp-up duty  | Stop the line, allow motor to cool, replace **SPR-MOTOR-2KW** if bearing roughness present. |
| E-121 | Belt tracking off-centre (> 15 mm offset)   | Worn idler, asymmetric loading, frame distortion              | Re-align idlers; if frame is bent, schedule full take-up replacement.                      |
| E-124 | Belt slip on drive pulley                  | Loss of tension, glazed pulley lagging                        | Re-tension belt; replace **SPR-BELT-LINE-A** if elongation exceeds factory limit.          |

## Spare-part cross-reference

- Conveyor motor: **SPR-MOTOR-2KW**
- Belt (line-A spec): **SPR-BELT-LINE-A**
- Idler set: **SPR-IDLER-3PCS**
