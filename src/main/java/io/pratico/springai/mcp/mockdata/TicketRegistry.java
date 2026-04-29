package io.pratico.springai.mcp.mockdata;

import io.pratico.springai.mcp.domain.MaintenanceTicket;
import io.pratico.springai.mcp.domain.TicketPriority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory stand-in for a CMMS / ticketing system.
 * Accepts new maintenance tickets created by MCP clients.
 */
@Component
public class TicketRegistry {

    private final Map<String, MaintenanceTicket> tickets = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1000);

    public MaintenanceTicket create(String machineId, String issueSummary, TicketPriority priority, String createdBy) {
        String ticketId = "MT-" + sequence.incrementAndGet();
        MaintenanceTicket ticket = new MaintenanceTicket(
                ticketId,
                machineId,
                issueSummary,
                priority,
                Instant.now(),
                createdBy
        );
        tickets.put(ticketId, ticket);
        return ticket;
    }

    public List<MaintenanceTicket> findAll() {
        return tickets.values().stream()
                .sorted((a, b) -> b.createdAt().compareTo(a.createdAt()))
                .toList();
    }
}
