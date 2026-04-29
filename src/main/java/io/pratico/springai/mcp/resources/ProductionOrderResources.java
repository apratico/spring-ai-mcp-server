package io.pratico.springai.mcp.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;
import io.pratico.springai.mcp.domain.ProductionOrder;
import io.pratico.springai.mcp.mockdata.MesRegistry;
import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Exposes a single production order as a navigable MCP resource at
 * <code>production-order://{orderId}</code>. The path variable is bound by name
 * to the {@code orderId} method parameter.
 */
@Component
public class ProductionOrderResources {

    private static final String URI_TEMPLATE = "production-order://{orderId}";
    private static final String MIME_TYPE = "application/json";

    private final MesRegistry mesRegistry;
    private final ObjectMapper objectMapper;

    public ProductionOrderResources(MesRegistry mesRegistry, ObjectMapper objectMapper) {
        this.mesRegistry = mesRegistry;
        this.objectMapper = objectMapper;
    }

    @McpResource(
            uri = URI_TEMPLATE,
            name = "Production order",
            description = "JSON document describing a single MES production order by id (e.g. PO-2041).")
    public ReadResourceResult getProductionOrder(String orderId) {
        ProductionOrder order = mesRegistry.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No production order with id '%s'".formatted(orderId)));
        return new ReadResourceResult(List.of(
                new TextResourceContents("production-order://" + orderId, MIME_TYPE, serialize(order))
        ));
    }

    private String serialize(ProductionOrder order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize production order " + order.orderId(), e);
        }
    }
}
