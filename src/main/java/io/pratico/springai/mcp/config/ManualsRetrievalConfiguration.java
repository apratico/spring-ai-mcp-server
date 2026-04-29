package io.pratico.springai.mcp.config;

import io.pratico.springai.mcp.mockdata.ManualsLibrary;
import io.pratico.springai.mcp.mockdata.ManualsLibrary.ManualDocument;
import io.pratico.springai.mcp.tools.ManualsTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Wires the RAG side of the MCP server.
 *
 * <p>Builds an in-memory {@link SimpleVectorStore} from
 * <code>classpath:/manuals/*.md</code> at startup using the configured embedding
 * model and exposes {@link ManualsTools}, the {@code search_manuals} MCP tool.
 *
 * <p>Whole subsystem is gated on the presence of an {@link EmbeddingModel} bean.
 * The OpenAI starter only creates one when {@code spring.ai.model.embedding=openai}
 * (and {@code OPENAI_API_KEY} is set), so the four non-RAG tools (production orders,
 * telemetry, inventory, maintenance tickets) keep working out of the box without
 * any OpenAI credentials.
 */
@Configuration
@ConditionalOnBean(EmbeddingModel.class)
public class ManualsRetrievalConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ManualsRetrievalConfiguration.class);

    @Bean
    public VectorStore manualsVectorStore(EmbeddingModel embeddingModel, ManualsLibrary library) {
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documents = library.all().stream()
                .map(ManualsRetrievalConfiguration::toDocument)
                .toList();
        if (documents.isEmpty()) {
            log.warn("No manuals found in classpath; search_manuals will return empty results.");
            return store;
        }
        store.add(documents);
        log.info("Indexed {} maintenance manual(s) into the in-memory vector store.", documents.size());
        return store;
    }

    @Bean
    public ManualsTools manualsTools(VectorStore manualsVectorStore,
                                     @Value("${mcp-server.manuals.top-k-default:4}") int topKDefault) {
        return new ManualsTools(manualsVectorStore, topKDefault);
    }

    private static Document toDocument(ManualDocument manual) {
        return new Document(
                manual.id(),
                manual.content(),
                Map.of(
                        "manualId", manual.id(),
                        "title",    manual.title()));
    }
}
