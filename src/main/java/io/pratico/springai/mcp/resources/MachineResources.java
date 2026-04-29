package io.pratico.springai.mcp.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;
import io.pratico.springai.mcp.mockdata.PlcGateway;
import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Exposes the shop-floor machine roster as a navigable MCP resource at
 * <code>machines://list</code>. Returned as JSON so that an MCP-aware client
 * can render or parse it without an extra tool round-trip.
 */
@Component
public class MachineResources {

    private static final String URI = "machines://list";
    private static final String MIME_TYPE = "application/json";

    private final PlcGateway plcGateway;
    private final ObjectMapper objectMapper;

    public MachineResources(PlcGateway plcGateway, ObjectMapper objectMapper) {
        this.plcGateway = plcGateway;
        this.objectMapper = objectMapper;
    }

    @McpResource(
            uri = URI,
            name = "Shop-floor machines",
            description = "JSON list of every registered machine with current state, OEE and last alarm.")
    public ReadResourceResult listMachines() {
        return new ReadResourceResult(List.of(
                new TextResourceContents(URI, MIME_TYPE, serialize())
        ));
    }

    private String serialize() {
        try {
            return objectMapper.writeValueAsString(plcGateway.findAll());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize machine roster", e);
        }
    }
}
