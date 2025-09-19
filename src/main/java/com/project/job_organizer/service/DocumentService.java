package com.project.job_organizer.service;

import com.project.job_organizer.model.DocumentEntity;
import com.project.job_organizer.model.UserEntity;
import com.project.job_organizer.repository.DocumentRepository;
import com.project.job_organizer.repository.UserRepository;
import com.project.job_organizer.controller.UserPrincipal; // la tua classe custom
import com.project.job_organizer.utils.DocumentUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "C:/uploads/";


    public DocumentService(DocumentRepository documentRepository,
                           UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }
    //Create
    public List<DocumentEntity> uploadDocuments(MultipartFile[] files, String documentName, String documentDescription) throws IOException {
        UserEntity user = getLoggedUser();
        List<DocumentEntity> savedDocuments = new ArrayList<>();

        Path uploadRootPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadRootPath)) {
            Files.createDirectories(uploadRootPath);
        }

        for (MultipartFile file : files) {
            String mimeType = file.getContentType();
            long size = file.getSize();

            if (!DocumentUtils.isMimeTypeAllowed(mimeType)) {
                throw new RuntimeException("MIME TYPE NOT SUPPORTED: " + mimeType);
            }

            if (!DocumentUtils.isFileSizeAllowed(size)) {
                throw new RuntimeException("FILE SIZE NOT SUPPORTED: " + size);
            }

            DocumentEntity document = new DocumentEntity();
            document.setDocumentName(documentName);
            document.setDocumentDescription(documentDescription);
            document.setMimeType(mimeType);
            document.setFileType(DocumentUtils.mapMimeTypeToFileType(mimeType));
            document.setCreatedAt(LocalDateTime.now());
            document.setUser(user);

            Path uploadPath = Paths.get(UPLOAD_DIR + user.getId());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            file.transferTo(filePath.toFile());

            document.setDocumentPath(filePath.toString());

            savedDocuments.add(documentRepository.save(document));

        }
        return savedDocuments;

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
