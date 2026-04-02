package com.madhav.spring.ai.controller;


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

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}