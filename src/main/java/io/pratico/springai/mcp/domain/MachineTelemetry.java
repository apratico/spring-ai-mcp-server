package io.pratico.springai.mcp.domain;

import java.time.Instant;
import java.util.List;

public record MachineTelemetry(
        String machineId,
        TelemetryWindow window,
        Instant from,
        Instant to,
        List<TelemetrySample> samples,
        TelemetryStats stats
) {

    public record TelemetryStats(
            double avgTemperatureCelsius,
            double maxTemperatureCelsius,
            double avgVibrationMmPerSec,
            double maxVibrationMmPerSec,
            double avgCurrentAmperes,
            double maxCurrentAmperes,
            long cyclesInWindow
    ) {
    }
}
