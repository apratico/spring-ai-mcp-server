package io.pratico.springai.mcp.domain;

import java.time.Instant;

public record MaintenanceTicket(
        String ticketId,
        String machineId,
        String issueSummary,
        TicketPriority priority,
        Instant createdAt,
        String createdBy
) {
}
