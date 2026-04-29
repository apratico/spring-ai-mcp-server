package io.pratico.springai.mcp.tools;

import io.pratico.springai.mcp.domain.ProductionOrder;
import io.pratico.springai.mcp.domain.ProductionOrderStatus;
import io.pratico.springai.mcp.mockdata.MesRegistry;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductionOrderTools {

    private final MesRegistry mesRegistry;

    public ProductionOrderTools(MesRegistry mesRegistry) {
        this.mesRegistry = mesRegistry;
    }

    @McpTool(
            name = "get_production_orders",
            description = """
                    List production orders from the MES/ERP, optionally filtered by status.
                    Use this when the caller asks about work orders, production schedule, release state,
                    orders in progress, on hold, completed, planned, or cancelled. If status is null or
                    omitted, returns every order regardless of status.
                    """)
    public List<ProductionOrder> getProductionOrders(
            @McpToolParam(
                    required = false,
                    description = "Optional filter. One of: PLANNED, RELEASED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED.")
            ProductionOrderStatus status) {
        return status == null ? mesRegistry.findAll() : mesRegistry.findByStatus(status);
    }
}
