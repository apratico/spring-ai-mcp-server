package io.pratico.springai.mcp.prompts;

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MCP prompt that walks an LLM through a structured shift-handover summary
 * for the outgoing supervisor. Designed to be combined by the client with
 * the get_production_orders, get_machine_telemetry and machines://list calls.
 */
@Component
public class ShiftHandoverPrompt {

    @McpPrompt(
            name = "shift-handover-summary",
            description = """
                    Produce a shift-handover briefing for the incoming supervisor:
                    open production orders, machines currently down, open maintenance tickets,
                    pending material reorders. Parametrized by shift label and outgoing supervisor name.
                    """)
    public GetPromptResult shiftHandoverSummary(
            @McpArg(name = "shift", description = "Outgoing shift label, e.g. MORNING, AFTERNOON, NIGHT.", required = true)
            String shift,
            @McpArg(name = "supervisor", description = "Name of the outgoing supervisor.", required = true)
            String supervisor) {

        String userMessage = """
                You are the shift-handover assistant for a discrete-manufacturing plant.
                Produce a compact briefing for the incoming supervisor based on the current state of
                the manufacturing-operations MCP server. Use these tools and resources, in this order:

                  1. Read the resource `machines://list` to obtain the full machine roster.
                  2. Call `get_production_orders` (no status filter) for the open work-order picture.
                  3. For every machine in DOWN state, call `get_machine_telemetry` with window=LAST_HOUR
                     to confirm the failure mode.
                  4. Cross-check spare-part availability for failed machines via `get_inventory_level`.

                Return a hand-over note with these sections:
                  - Header: shift = %s, outgoing supervisor = %s, incoming supervisor = (TBD).
                  - In-progress and on-hold orders, with assigned machine and target end date.
                  - Machines requiring attention: id, alarm code, recent telemetry trend, recommended action.
                  - Open maintenance tickets (if any) and stock coverage of the relevant spare parts.
                  - Top three follow-ups for the next shift.

                Keep each bullet on a single line. Do NOT invent values: if a tool returns no data, say so.
                """.formatted(shift, supervisor);

        return new GetPromptResult(
                "Shift handover summary",
                List.of(new PromptMessage(Role.USER, new TextContent(userMessage))));
    }
}
