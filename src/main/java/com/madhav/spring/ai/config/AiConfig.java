package com.madhav.spring.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiConfig {

    @Value("${app.ai.ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${app.ai.ollama.timeout-minutes}")
    private int ollamaTimeoutMinutes;

    @Value("${app.ai.chat-model.name}")
    private String chatModelName;

    @Value("${app.ai.embedding-model.name}")
    private String embeddingModelName;

    @Value("${app.ai.qdrant.host}")
    private String qdrantHost;

    @Value("${app.ai.qdrant.port}")
    private int qdrantPort;

    @Value("${app.ai.qdrant.collection-name}")
    private String qdrantCollectionName;

    @Bean
    public ChatLanguageModel chatModel() {

        return OllamaChatModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(chatModelName)
                .timeout(Duration.ofMinutes(ollamaTimeoutMinutes))
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {

        return OllamaEmbeddingModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(embeddingModelName)
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {

        return QdrantEmbeddingStore.builder()
                .host(qdrantHost)
                .port(qdrantPort)
                .collectionName(qdrantCollectionName)
                .build();
    }
}
