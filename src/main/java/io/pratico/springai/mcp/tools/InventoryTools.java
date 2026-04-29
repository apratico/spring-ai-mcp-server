package io.pratico.springai.mcp.tools;

import io.pratico.springai.mcp.domain.InventoryItem;
import io.pratico.springai.mcp.mockdata.WarehouseRegistry;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class InventoryTools {

    private final WarehouseRegistry warehouseRegistry;

    public InventoryTools(WarehouseRegistry warehouseRegistry) {
        this.warehouseRegistry = warehouseRegistry;
    }

    @McpTool(
            name = "get_inventory_level",
            description = """
                    Look up the current warehouse stock for a single material or spare-part code.
                    Returns quantity on hand, reorder point, unit of measure and bin location.
                    Use this when the caller asks whether a spare part or raw material is available,
                    whether a replacement is in stock, or whether re-ordering is needed.
                    """)
    public InventoryItem getInventoryLevel(
            @McpToolParam(
                    required = true,
                    description = "Material code as used in the WMS, e.g. SPR-HYD-PUMP-630 or RAW-ALU-6061.")
            String materialCode) {
        return warehouseRegistry.findByCode(materialCode)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No inventory item with code '%s'".formatted(materialCode)));
    }
}
