package io.pratico.springai.mcp.domain;

public record ManualSnippet(
        String manualId,
        String title,
        double similarityScore,
        String excerpt
) {
}
