package io.pratico.springai.mcp.tools;

import io.pratico.springai.mcp.domain.ManualSnippet;
import org.springframework.ai.document.Document;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;

/**
 * Bridges the RAG primitive into the MCP protocol: search_manuals exposes the
 * same maintenance-manuals corpus over MCP that the companion RAG showcase
 * exposes over REST. The vector store is built once at startup from
 * classpath:/manuals/*.md (see {@code ManualsRetrievalConfiguration}).
 *
 * <p>Wired as a @Bean by {@code ManualsRetrievalConfiguration}, conditional on
 * {@code spring.ai.openai.api-key} being set, so the other four tools can run
 * without an OpenAI key.
 */
public class ManualsTools {

    private final VectorStore vectorStore;
    private final int topKDefault;

    public ManualsTools(VectorStore vectorStore, int topKDefault) {
        this.vectorStore = vectorStore;
        this.topKDefault = topKDefault;
    }

    @McpTool(
            name = "search_manuals",
            description = """
                    Semantic search across the on-board maintenance-manual corpus.
                    Returns the top-k snippets most relevant to the natural-language query,
                    each with the source manual id, title, similarity score and excerpt.
                    Use this for fault-diagnosis lookups, OEM procedure recall, spare-part
                    cross-reference, and answering "how do I fix / inspect X" style questions.
                    """,
            annotations = @McpTool.McpAnnotations(
                    title = "Search maintenance manuals",
                    readOnlyHint = true,
                    destructiveHint = false,
                    idempotentHint = true))
    public List<ManualSnippet> searchManuals(
            @McpToolParam(required = true,
                    description = "Free-text query in English or Italian, e.g. 'hydraulic pressure drop on press' or 'conveyor belt motor overheating'.")
            String query,
            @McpToolParam(required = false,
                    description = "Maximum number of snippets to return. Defaults to 4 if omitted.")
            Integer k) {
        int topK = (k == null || k <= 0) ? topKDefault : k;
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();

        List<Document> hits = vectorStore.similaritySearch(request);
        if (hits == null || hits.isEmpty()) {
            return List.of();
        }
        return hits.stream().map(ManualsTools::toSnippet).toList();
    }

    private static ManualSnippet toSnippet(Document document) {
        Object manualId = document.getMetadata().getOrDefault("manualId", "unknown");
        Object title    = document.getMetadata().getOrDefault("title",    manualId);
        Double score    = document.getScore();
        return new ManualSnippet(
                String.valueOf(manualId),
                String.valueOf(title),
                score == null ? 0.0 : score,
                document.getText());
    }
}
