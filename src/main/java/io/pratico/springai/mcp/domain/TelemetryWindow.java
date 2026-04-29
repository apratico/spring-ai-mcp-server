package io.pratico.springai.mcp.domain;

import java.time.Duration;

public enum TelemetryWindow {
    LAST_5_MIN(Duration.ofMinutes(5),  Duration.ofSeconds(15)),
    LAST_HOUR(Duration.ofHours(1),     Duration.ofMinutes(2)),
    LAST_SHIFT(Duration.ofHours(8),    Duration.ofMinutes(15));

    private final Duration totalDuration;
    private final Duration sampleInterval;

    TelemetryWindow(Duration totalDuration, Duration sampleInterval) {
        this.totalDuration = totalDuration;
        this.sampleInterval = sampleInterval;
    }

    public Duration totalDuration() {
        return totalDuration;
    }

    public Duration sampleInterval() {
        return sampleInterval;
    }
}
