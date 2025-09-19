package com.project.job_organizer.controller;

import com.project.job_organizer.model.DocumentEntity;
import com.project.job_organizer.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // CREATE
    @PostMapping("/upload")
    public ResponseEntity<List<DocumentEntity>> uploadDocuments(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("documentName") String documentName,
            @RequestParam("documentDescription") String documentDescription) throws IOException {
        List<DocumentEntity> savedDocuments = documentService.uploadDocuments(files, documentName, documentDescription);
        return ResponseEntity.ok(savedDocuments);
    }


    @GetMapping
    public ResponseEntity<List<DocumentEntity>> getMyDocuments() {
        List<DocumentEntity> docs = documentService.getMyDocuments();
        return ResponseEntity.ok(docs);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DocumentEntity> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentEntity updatedDoc) {
        DocumentEntity saved = documentService.updateDocument(id, updatedDoc);
        return ResponseEntity.ok(saved);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
