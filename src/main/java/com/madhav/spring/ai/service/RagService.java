package com.madhav.spring.ai.service;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public String ask(String question) {

        try {

            // generate query embedding
            var queryEmbedding = embeddingModel.embed(question).content();

            // call Qdrant REST search
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> body = new HashMap<>();
            body.put("vector", queryEmbedding.vector());
            body.put("limit", 3);
            body.put("with_payload", true);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://localhost:6333/collections/documents/points/search",
                    request,
                    Map.class
            );

            List<Map<String, Object>> results =
                    (List<Map<String, Object>>) response.getBody().get("result");

            StringBuilder contextBuilder = new StringBuilder();

            for (Map<String, Object> point : results) {

                Map payload = (Map) point.get("payload");

                if (payload != null && payload.get("text_segment") != null) {
                    contextBuilder.append(payload.get("text_segment")).append("\n");
                }
            }

            String context = contextBuilder.toString();

            String prompt = """
                Answer the question using the context below.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

            return chatModel.generate(prompt);

        } catch (Exception e) {
            log.error("Error in llm response ", e);
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}