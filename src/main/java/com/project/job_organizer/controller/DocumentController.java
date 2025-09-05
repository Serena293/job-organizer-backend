package com.project.job_organizer.controller;

import com.project.job_organizer.model.DocumentEntity;
import com.project.job_organizer.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // CREATE → Caricare un documento
    @PostMapping
    public ResponseEntity<DocumentEntity> uploadDocument(@RequestBody DocumentEntity document) {
        DocumentEntity saved = documentService.uploadDocument(document);
        return ResponseEntity.ok(saved);
    }

    // READ → Recuperare i documenti dell'utente loggato
    @GetMapping
    public ResponseEntity<List<DocumentEntity>> getMyDocuments() {
        List<DocumentEntity> docs = documentService.getMyDocuments();
        return ResponseEntity.ok(docs);
    }

    // UPDATE → Modificare un documento
    @PutMapping("/{id}")
    public ResponseEntity<DocumentEntity> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentEntity updatedDoc) {
        DocumentEntity saved = documentService.updateDocument(id, updatedDoc);
        return ResponseEntity.ok(saved);
    }

    // DELETE → Eliminare un documento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
