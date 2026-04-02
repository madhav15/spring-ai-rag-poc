package com.madhav.spring.ai.controller;


import com.madhav.spring.ai.service.DocumentService;
import com.madhav.spring.ai.service.PdfParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PdfController {

    private final PdfParserService pdfParserService;
    private final DocumentService documentService;

    @PostMapping("/upload-pdf")
    public String uploadPdf(@RequestParam MultipartFile file) {

        String text = pdfParserService.extractText(file);

        documentService.ingestPdfText(text);

        return "PDF processed and stored in vector DB";
    }
}
