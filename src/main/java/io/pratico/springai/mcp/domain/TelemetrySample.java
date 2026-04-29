package io.pratico.springai.mcp.domain;

import java.time.Instant;

public record TelemetrySample(
        Instant timestamp,
        double temperatureCelsius,
        double vibrationMmPerSec,
        double currentAmperes,
        long cycleCount
) {
}
