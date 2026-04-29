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
 * MCP prompt that drives a structured downtime root-cause analysis on a
 * specific machine, weaving together telemetry, manuals (RAG) and CMMS write.
 */
@Component
public class DowntimeRcaPrompt {

    @McpPrompt(
            name = "downtime-rca-investigation",
            description = """
                    Walk an LLM through a five-step downtime root-cause analysis on a given machine:
                    confirm the alarm, inspect telemetry, retrieve relevant manuals, propose hypotheses,
                    and (only with operator confirmation) open a maintenance ticket.
                    """)
    public GetPromptResult downtimeRcaInvestigation(
            @McpArg(name = "machine_id", description = "Target machine id, e.g. PRESS-03.", required = true)
            String machineId,
            @McpArg(name = "observed_alarm", description = "Alarm code or symptom observed by the operator (free text).", required = false)
            String observedAlarm) {

        String alarmHint = (observedAlarm == null || observedAlarm.isBlank())
                ? "(no alarm code provided — start from telemetry and the machines:// resource)"
                : observedAlarm;

        String userMessage = """
                Conduct a root-cause analysis for machine %s. Reported symptom: %s.

                Follow these five steps strictly, calling exactly one MCP capability per step:

                  Step 1 — Confirm the asset state.
                    Read resource `machines://list` and locate %s; quote its current state, OEE and last
                    alarm code/description verbatim.

                  Step 2 — Inspect recent behaviour.
                    Call `get_machine_telemetry` with machineId=%s and window=LAST_HOUR.
                    Highlight any out-of-band reading (temperature, vibration, current) and the cycle
                    count delta vs. a healthy baseline.

                  Step 3 — Pull the manuals.
                    Call `search_manuals` with a query that combines the machine description and the
                    failure pattern from step 2. Cite the top-2 snippets (manualId + 1-line excerpt).

                  Step 4 — Propose hypotheses.
                    List 2 to 3 plausible root causes ranked by likelihood, each backed by the evidence
                    gathered in steps 1-3. Distinguish between the immediate cause and the latent cause.

                  Step 5 — Action.
                    Recommend the single next action. Only call `create_maintenance_ticket` if the
                    operator-facing summary explicitly confirms the issue is real and unresolved;
                    otherwise stop and ask for confirmation.

                Output format: numbered sections matching the five steps above. Be concise.
                """.formatted(machineId, alarmHint, machineId, machineId);

        return new GetPromptResult(
                "Downtime root-cause investigation",
                List.of(new PromptMessage(Role.USER, new TextContent(userMessage))));
    }
}
