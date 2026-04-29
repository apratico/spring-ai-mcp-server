package io.pratico.springai.mcp.mockdata;

import io.pratico.springai.mcp.domain.ProductionOrder;
import io.pratico.springai.mcp.domain.ProductionOrderStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory stand-in for a MES/ERP production-order registry.
 * Seeds the same realistic dataset shipped in the companion agent showcase.
 */
@Component
public class MesRegistry {

    private final Map<String, ProductionOrder> orders = new ConcurrentHashMap<>();

    @PostConstruct
    void seed() {
        LocalDate today = LocalDate.now();
        put(new ProductionOrder("PO-2041", "ALU-FRAME-180",  500, ProductionOrderStatus.IN_PROGRESS, "CNC-07",   today.minusDays(1), today.plusDays(2)));
        put(new ProductionOrder("PO-2042", "ALU-FRAME-220",  300, ProductionOrderStatus.RELEASED,    "CNC-08",   today,              today.plusDays(3)));
        put(new ProductionOrder("PO-2043", "STL-BRKT-50",   1200, ProductionOrderStatus.IN_PROGRESS, "PRESS-02", today.minusDays(2), today.plusDays(1)));
        put(new ProductionOrder("PO-2044", "STL-BRKT-80",    800, ProductionOrderStatus.ON_HOLD,     "PRESS-03", today.minusDays(1), today.plusDays(2)));
        put(new ProductionOrder("PO-2045", "PKG-BOX-L",     4000, ProductionOrderStatus.PLANNED,     "LINE-A",   today.plusDays(1), today.plusDays(4)));
        put(new ProductionOrder("PO-2046", "PKG-BOX-M",     6000, ProductionOrderStatus.COMPLETED,   "LINE-A",   today.minusDays(5), today.minusDays(1)));
    }

    private void put(ProductionOrder order) {
        orders.put(order.orderId(), order);
    }

    public List<ProductionOrder> findByStatus(ProductionOrderStatus status) {
        return orders.values().stream()
                .filter(o -> o.status() == status)
                .sorted((a, b) -> a.orderId().compareTo(b.orderId()))
                .toList();
    }

    public List<ProductionOrder> findAll() {
        return orders.values().stream()
                .sorted((a, b) -> a.orderId().compareTo(b.orderId()))
                .toList();
    }

    public Optional<ProductionOrder> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }
}
