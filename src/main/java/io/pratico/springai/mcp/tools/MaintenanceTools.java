package io.pratico.springai.mcp.tools;

import io.pratico.springai.mcp.domain.MaintenanceTicket;
import io.pratico.springai.mcp.domain.TicketPriority;
import io.pratico.springai.mcp.mockdata.PlcGateway;
import io.pratico.springai.mcp.mockdata.TicketRegistry;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceTools {

    private static final String CREATED_BY = "mcp-client";

    private final TicketRegistry ticketRegistry;
    private final PlcGateway plcGateway;

    public MaintenanceTools(TicketRegistry ticketRegistry, PlcGateway plcGateway) {
        this.ticketRegistry = ticketRegistry;
        this.plcGateway = plcGateway;
    }

    @McpTool(
            name = "create_maintenance_ticket",
            description = """
                    Open a new maintenance ticket in the CMMS for a given machine.
                    Call this only after verifying the machine is in an abnormal state
                    (DOWN or alarmed) using the /machines resource or get_machine_telemetry.
                    Priority must reflect severity: CRITICAL for safety or production-stopping faults,
                    HIGH for unplanned downtime on a running-required asset, MEDIUM for degraded performance,
                    LOW for cosmetic or non-urgent issues.
                    """,
            annotations = @McpTool.McpAnnotations(
                    title = "Create CMMS maintenance ticket",
                    readOnlyHint = false,
                    destructiveHint = false,
                    idempotentHint = false))
    public MaintenanceTicket createMaintenanceTicket(
            @McpToolParam(required = true,
                    description = "Machine identifier that the ticket targets, e.g. PRESS-03.")
            String machineId,
            @McpToolParam(required = true,
                    description = "Short, specific issue description in plain English (1 sentence max).")
            String issueSummary,
            @McpToolParam(required = true,
                    description = "Priority: LOW, MEDIUM, HIGH or CRITICAL.")
            TicketPriority priority) {

        if (plcGateway.findById(machineId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot open ticket: machine '%s' is not registered".formatted(machineId));
        }
        return ticketRegistry.create(machineId, issueSummary, priority, CREATED_BY);
    }
}
