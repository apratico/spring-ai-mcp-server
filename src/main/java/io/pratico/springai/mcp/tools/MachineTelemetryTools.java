package io.pratico.springai.mcp.tools;

import io.pratico.springai.mcp.domain.MachineTelemetry;
import io.pratico.springai.mcp.domain.TelemetryWindow;
import io.pratico.springai.mcp.mockdata.PlcGateway;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class MachineTelemetryTools {

    private final PlcGateway plcGateway;

    public MachineTelemetryTools(PlcGateway plcGateway) {
        this.plcGateway = plcGateway;
    }

    @McpTool(
            name = "get_machine_telemetry",
            description = """
                    Read PLC time-series telemetry for a machine over a configurable window.
                    Returns ordered samples (temperature °C, vibration mm/s, current A, cumulative cycle count)
                    plus aggregated stats (avg/max per channel, cycles produced in the window).
                    Use this for downtime root-cause analysis, performance degradation checks, or to confirm
                    that a machine is genuinely faulty before opening a maintenance ticket.
                    Use this BEFORE create_maintenance_ticket when the symptom is ambiguous.
                    """)
    public MachineTelemetry getMachineTelemetry(
            @McpToolParam(
                    required = true,
                    description = "Machine identifier as shown on the shop floor, e.g. CNC-07 or PRESS-03.")
            String machineId,
            @McpToolParam(
                    required = false,
                    description = "Time window. One of: LAST_5_MIN, LAST_HOUR, LAST_SHIFT. Defaults to LAST_HOUR if omitted.")
            TelemetryWindow window) {
        TelemetryWindow effective = window == null ? TelemetryWindow.LAST_HOUR : window;
        return plcGateway.getTelemetry(machineId, effective);
    }
}
