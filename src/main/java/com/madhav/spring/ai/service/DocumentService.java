package com.madhav.spring.ai.service;

import com.madhav.spring.ai.util.TextChunker;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    @Value("${app.ai.chunking.chunk-size}")
    private int chunkSize;

    public void ingest(String text) {

        TextSegment segment = TextSegment.from(text);

        var embedding = embeddingModel.embed(text).content();

        embeddingStore.add(embedding, segment);
    }


    public void ingestPdfText(String text) {

        List<String> chunks = TextChunker.chunk(text, chunkSize);

        for (String chunk : chunks) {

            var embedding = embeddingModel.embed(chunk).content();

            Map<String, Object> payload = new HashMap<>();
            payload.put("text", chunk);

            embeddingStore.add(embedding, TextSegment.from(chunk));
        }
    }
}
