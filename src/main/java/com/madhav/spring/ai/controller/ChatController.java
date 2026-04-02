package com.madhav.spring.ai.controller;

import com.madhav.spring.ai.dto.AskQuestionRequest;
import com.madhav.spring.ai.service.DocumentService;
import com.madhav.spring.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final DocumentService documentService;
    private final RagService ragService;

    @PostMapping("/ingest")
    public String ingest(@RequestBody String text) {

        documentService.ingest(text);
        return "Document stored";
    }

    @PostMapping("/ask")
    public String ask(@RequestBody AskQuestionRequest request) {
        return ragService.ask(request.getQuestion());
    }
}