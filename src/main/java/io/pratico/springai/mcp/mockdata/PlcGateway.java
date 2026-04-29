package io.pratico.springai.mcp.mockdata;

import io.pratico.springai.mcp.domain.Machine;
import io.pratico.springai.mcp.domain.MachineState;
import io.pratico.springai.mcp.domain.MachineTelemetry;
import io.pratico.springai.mcp.domain.MachineTelemetry.TelemetryStats;
import io.pratico.springai.mcp.domain.TelemetrySample;
import io.pratico.springai.mcp.domain.TelemetryWindow;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory stand-in for a PLC / shop-floor data gateway.
 *
 * <p>Holds current machine state plus a synthetic time-series generator that
 * produces deterministic telemetry samples per (machineId, window). The signal
 * shape follows the machine state — running machines look healthy, machines in
 * a DOWN state produce out-of-band readings — so downstream consumers see a
 * believable, story-consistent dataset without needing a real OPC UA backend.
 */
@Component
public class PlcGateway {

    private final Map<String, Machine> machines = new ConcurrentHashMap<>();

    @PostConstruct
    void seed() {
        Instant now = Instant.now();
        put(new Machine("CNC-07",   "5-axis CNC milling center",   MachineState.RUNNING,     87.4, null,    null,                                now));
        put(new Machine("CNC-08",   "3-axis CNC milling center",   MachineState.SETUP,       0.0,  null,    null,                                now));
        put(new Machine("PRESS-02", "400-ton hydraulic press",     MachineState.RUNNING,     72.1, null,    null,                                now));
        put(new Machine("PRESS-03", "630-ton hydraulic press",     MachineState.DOWN,        0.0,  "E-204", "Hydraulic pressure below threshold", now));
        put(new Machine("LINE-A",   "Packaging line A",            MachineState.DOWN,        0.0,  "E-118", "Conveyor belt motor overheating",    now));
        put(new Machine("LINE-B",   "Packaging line B",            MachineState.MAINTENANCE, 0.0,  null,    "Scheduled preventive maintenance",   now));
    }

    private void put(Machine machine) {
        machines.put(machine.machineId(), machine);
    }

    public Optional<Machine> findById(String machineId) {
        return Optional.ofNullable(machines.get(machineId));
    }

    public List<Machine> findAll() {
        return machines.values().stream()
                .sorted((a, b) -> a.machineId().compareTo(b.machineId()))
                .toList();
    }

    public List<Machine> findByState(MachineState state) {
        return machines.values().stream()
                .filter(m -> m.state() == state)
                .sorted((a, b) -> a.machineId().compareTo(b.machineId()))
                .toList();
    }

    public MachineTelemetry getTelemetry(String machineId, TelemetryWindow window) {
        Machine machine = findById(machineId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No machine found with id '%s'".formatted(machineId)));

        Instant to = Instant.now();
        Instant from = to.minus(window.totalDuration());

        long sampleCount = window.totalDuration().toSeconds() / window.sampleInterval().toSeconds();
        // Deterministic per (machineId, window) so successive calls in tests/demos line up.
        Random rng = new Random((long) machineId.hashCode() * 31 + window.ordinal());

        List<TelemetrySample> samples = new ArrayList<>((int) sampleCount);
        long cycle = baseCycleCount(machine);
        for (int i = 0; i < sampleCount; i++) {
            Instant ts = from.plus(window.sampleInterval().multipliedBy(i));
            samples.add(syntheticSample(machine, ts, rng, cycle));
            cycle += incrementCycles(machine, rng);
        }
        return new MachineTelemetry(machineId, window, from, to, samples, computeStats(samples));
    }

    private static long baseCycleCount(Machine machine) {
        return switch (machine.state()) {
            case RUNNING     -> 12_400L;
            case IDLE        -> 12_390L;
            case SETUP       -> 12_390L;
            case MAINTENANCE -> 12_390L;
            case DOWN        -> 12_390L;
        };
    }

    private static long incrementCycles(Machine machine, Random rng) {
        return machine.state() == MachineState.RUNNING ? 1L + rng.nextInt(2) : 0L;
    }

    private static TelemetrySample syntheticSample(Machine machine, Instant ts, Random rng, long cycleCount) {
        return switch (machine.state()) {
            case RUNNING -> new TelemetrySample(
                    ts,
                    62.0 + rng.nextGaussian() * 1.5,
                    1.8  + rng.nextGaussian() * 0.2,
                    18.0 + rng.nextGaussian() * 0.6,
                    cycleCount);
            case IDLE -> new TelemetrySample(
                    ts,
                    34.0 + rng.nextGaussian() * 0.8,
                    0.2  + Math.abs(rng.nextGaussian() * 0.05),
                    1.5  + rng.nextGaussian() * 0.1,
                    cycleCount);
            case SETUP -> new TelemetrySample(
                    ts,
                    38.0 + rng.nextGaussian() * 0.6,
                    0.4  + Math.abs(rng.nextGaussian() * 0.1),
                    3.5  + rng.nextGaussian() * 0.4,
                    cycleCount);
            case MAINTENANCE -> new TelemetrySample(
                    ts,
                    24.0 + rng.nextGaussian() * 0.4,
                    0.0,
                    0.0,
                    cycleCount);
            // DOWN: clearly anomalous — high temp, vibration spike, current at zero.
            case DOWN -> new TelemetrySample(
                    ts,
                    91.0 + rng.nextGaussian() * 2.2,
                    7.4  + Math.abs(rng.nextGaussian() * 0.6),
                    0.0,
                    cycleCount);
        };
    }

    private static TelemetryStats computeStats(List<TelemetrySample> samples) {
        if (samples.isEmpty()) {
            return new TelemetryStats(0, 0, 0, 0, 0, 0, 0);
        }
        double sumTemp = 0, sumVib = 0, sumCur = 0;
        double maxTemp = Double.NEGATIVE_INFINITY;
        double maxVib  = Double.NEGATIVE_INFINITY;
        double maxCur  = Double.NEGATIVE_INFINITY;
        long firstCycles = samples.get(0).cycleCount();
        long lastCycles  = samples.get(samples.size() - 1).cycleCount();

        for (TelemetrySample s : samples) {
            sumTemp += s.temperatureCelsius();
            sumVib  += s.vibrationMmPerSec();
            sumCur  += s.currentAmperes();
            if (s.temperatureCelsius() > maxTemp) maxTemp = s.temperatureCelsius();
            if (s.vibrationMmPerSec()  > maxVib)  maxVib  = s.vibrationMmPerSec();
            if (s.currentAmperes()     > maxCur)  maxCur  = s.currentAmperes();
        }
        int n = samples.size();
        return new TelemetryStats(
                sumTemp / n, maxTemp,
                sumVib  / n, maxVib,
                sumCur  / n, maxCur,
                Math.max(0, lastCycles - firstCycles));
    }
}
