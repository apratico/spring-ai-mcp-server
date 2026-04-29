package io.pratico.springai.mcp.mockdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads maintenance manuals from <code>classpath:/manuals/*.md</code> at startup.
 * The library is the source of truth for both the search_manuals tool (via the
 * vector store) and the {@code /manuals/{id}} resource handler.
 */
@Component
public class ManualsLibrary {

    private static final Logger log = LoggerFactory.getLogger(ManualsLibrary.class);

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private final Map<String, ManualDocument> manualsById = new ConcurrentHashMap<>();
    private final String classpathPattern;

    public ManualsLibrary(@Value("${mcp-server.manuals.classpath-pattern:classpath:/manuals/*.md}")
                          String classpathPattern) {
        this.classpathPattern = classpathPattern;
        loadAll();
    }

    private void loadAll() {
        try {
            Resource[] resources = resolver.getResources(classpathPattern);
            for (Resource res : resources) {
                String filename = res.getFilename();
                if (filename == null) continue;
                String id = filename.replaceFirst("\\.md$", "");
                String body = new String(res.getContentAsByteArray(), StandardCharsets.UTF_8);
                String title = extractTitle(body, id);
                manualsById.put(id, new ManualDocument(id, title, body));
            }
            log.info("Loaded {} maintenance manual(s) from {}", manualsById.size(), classpathPattern);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load manuals from " + classpathPattern, e);
        }
    }

    private static String extractTitle(String body, String fallback) {
        for (String line : body.split("\\R", 8)) {
            String stripped = line.strip();
            if (stripped.startsWith("# ")) {
                return stripped.substring(2).trim();
            }
        }
        return fallback;
    }

    public List<ManualDocument> all() {
        return new ArrayList<>(manualsById.values());
    }

    public Optional<ManualDocument> findById(String manualId) {
        return Optional.ofNullable(manualsById.get(manualId));
    }

    public record ManualDocument(String id, String title, String content) {
    }
}
