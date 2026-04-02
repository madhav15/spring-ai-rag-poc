package com.madhav.spring.ai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.ai")
@Getter
@Setter
public class AppProperties {

    private OllamaProperties ollama = new OllamaProperties();
    private ChatModelProperties chatModel = new ChatModelProperties();
    private EmbeddingModelProperties embeddingModel = new EmbeddingModelProperties();
    private QdrantProperties qdrant = new QdrantProperties();
    private ChunkingProperties chunking = new ChunkingProperties();

    @Getter
    @Setter
    public static class OllamaProperties {
        private String baseUrl = "http://localhost:11434";
        private int timeoutMinutes = 2;
    }

    @Getter
    @Setter
    public static class ChatModelProperties {
        private String name = "phi3:mini";
    }

    @Getter
    @Setter
    public static class EmbeddingModelProperties {
        private String name = "nomic-embed-text";
    }

    @Getter
    @Setter
    public static class QdrantProperties {
        private String host = "localhost";
        private int port = 6334;
        private String collectionName = "documents";
        private int vectorSize = 768;
        private String distanceMetric = "Cosine";
    }

    @Getter
    @Setter
    public static class ChunkingProperties {
        private int chunkSize = 1000;
    }
}
