package io.pratico.springai.mcp.mockdata;

import io.pratico.springai.mcp.domain.InventoryItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory stand-in for a WMS inventory registry.
 * Holds stock levels for spare parts and raw materials.
 */
@Component
public class WarehouseRegistry {

    private final Map<String, InventoryItem> items = new ConcurrentHashMap<>();

    @PostConstruct
    void seed() {
        put(new InventoryItem("SPR-HYD-PUMP-400", "Hydraulic pump assembly, 400t press", 2,    2,  "EA", "WH-1/A/03"));
        put(new InventoryItem("SPR-HYD-PUMP-630", "Hydraulic pump assembly, 630t press", 0,    1,  "EA", "WH-1/A/04"));
        put(new InventoryItem("SPR-MOTOR-2KW",    "Conveyor motor 2kW, 3-phase",         5,    2,  "EA", "WH-1/B/11"));
        put(new InventoryItem("SPR-BELT-LINE-A",  "Conveyor belt, line A spec",          1,    2,  "EA", "WH-1/B/12"));
        put(new InventoryItem("RAW-ALU-6061",     "Aluminum 6061-T6 bar, 180mm",         240,  50, "M",  "WH-2/C/01"));
        put(new InventoryItem("RAW-STL-S275",     "Structural steel S275JR, sheet 3mm",  180,  80, "M2", "WH-2/C/08"));
        put(new InventoryItem("PKG-CARTON-L",     "Cardboard carton, large",             1500, 500,"EA", "WH-3/D/02"));
    }

    private void put(InventoryItem item) {
        items.put(item.materialCode(), item);
    }

    public Optional<InventoryItem> findByCode(String materialCode) {
        return Optional.ofNullable(items.get(materialCode));
    }

    public List<InventoryItem> findAll() {
        return items.values().stream()
                .sorted((a, b) -> a.materialCode().compareTo(b.materialCode()))
                .toList();
    }
}
