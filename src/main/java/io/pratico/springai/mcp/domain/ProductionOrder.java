package io.pratico.springai.mcp.domain;

import java.time.LocalDate;

public record ProductionOrder(
        String orderId,
        String productCode,
        int quantity,
        ProductionOrderStatus status,
        String assignedMachineId,
        LocalDate plannedStart,
        LocalDate plannedEnd
) {
}
