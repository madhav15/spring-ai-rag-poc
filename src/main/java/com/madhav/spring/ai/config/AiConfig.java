package com.madhav.spring.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AiConfig {

    private final AppProperties appProperties;

    @Bean
    public ChatLanguageModel chatModel() {

        return OllamaChatModel.builder()
                .baseUrl(appProperties.getOllama().getBaseUrl())
                .modelName(appProperties.getChatModel().getName())
                .timeout(Duration.ofMinutes(appProperties.getOllama().getTimeoutMinutes()))
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {

        return OllamaEmbeddingModel.builder()
                .baseUrl(appProperties.getOllama().getBaseUrl())
                .modelName(appProperties.getEmbeddingModel().getName())
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {

        return QdrantEmbeddingStore.builder()
                .host(appProperties.getQdrant().getHost())
                .port(appProperties.getQdrant().getPort())
                .collectionName(appProperties.getQdrant().getCollectionName())
                .build();
    }
}
