package com.project.job_organizer.service;

import com.project.job_organizer.model.DocumentEntity;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.DocumentRepository;
import com.project.job_organizer.repository.UserRepository;
import com.project.job_organizer.controller.UserPrincipal; // la tua classe custom
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DocumentService(DocumentRepository documentRepository,
                           UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public DocumentEntity uploadDocument(DocumentEntity document) {
        UserEntity user = getLoggedUser();
        document.setUser(user);
        return documentRepository.save(document);
    }

    // READ
    public List<DocumentEntity> getMyDocuments() {
        UserEntity user = getLoggedUser();
        return documentRepository.findAllByUser(user);
    }

    // UPDATE
    public DocumentEntity updateDocument(Long documentId, DocumentEntity updatedDoc) {
        UserEntity user = getLoggedUser();

        DocumentEntity existing = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this document");
        }

        existing.setDocumentName(updatedDoc.getDocumentName());
        existing.setDocumentPath(updatedDoc.getDocumentPath());
        existing.setDocumentDescription(updatedDoc.getDocumentDescription());


        return documentRepository.save(existing);
    }

    // DELETE
    public void deleteDocument(Long documentId) {
        UserEntity user = getLoggedUser();

        DocumentEntity existing = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this document");
        }

        documentRepository.delete(existing);
    }


    private UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
