package io.pratico.springai.mcp.domain;

import java.time.Instant;

public record Machine(
        String machineId,
        String description,
        MachineState state,
        double oeePercent,
        String lastAlarmCode,
        String lastAlarmDescription,
        Instant lastUpdate
) {
}
