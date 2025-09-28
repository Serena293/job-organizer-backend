package com.project.job_organizer.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import java.util.Map;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
//    private static final String UPLOAD_DIR = "C:/uploads/";
    private final Cloudinary cloudinary;


    public DocumentService(DocumentRepository documentRepository,
                           UserRepository userRepository, Cloudinary cloudinary) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }
    //Create
    public List<DocumentEntity> uploadDocuments(MultipartFile[] files, String documentName, String documentDescription) throws IOException {
        UserEntity user = getLoggedUser();
        List<DocumentEntity> savedDocuments = new ArrayList<>();

            for (MultipartFile file : files) {
            String mimeType = file.getContentType();
            long size = file.getSize();

            if (!DocumentUtils.isMimeTypeAllowed(mimeType)) {
                throw new RuntimeException("MIME TYPE NOT SUPPORTED: " + mimeType);
            }

            if (!DocumentUtils.isFileSizeAllowed(size)) {
                throw new RuntimeException("FILE SIZE NOT SUPPORTED: " + size);
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "job-organizer/user-" + user.getId(),
                                "resource_type", "auto"
                        ));

            String cloudinaryUrl = (String) uploadResult.get("secure_url");

            DocumentEntity document = new DocumentEntity();
            document.setDocumentName(documentName);
            document.setDocumentDescription(documentDescription);
            document.setMimeType(mimeType);
            document.setFileType(DocumentUtils.mapMimeTypeToFileType(mimeType));
            document.setCreatedAt(LocalDateTime.now());
            document.setUser(user);
            document.setDocumentPath(cloudinaryUrl);

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
        try {
            // Estrai public_id dall'URL Cloudinary
            String url = existing.getDocumentPath();
            if (url != null && url.contains("cloudinary.com")) {
                String publicId = extractPublicIdFromUrl(url);
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            // Log dell'errore ma procedi con cancellazione dal DB
            System.err.println("Error deleting document from Cloudinary: " + e.getMessage());


            documentRepository.delete(existing);
        }
    }
        private String extractPublicIdFromUrl(String url) {
            try {
               String[] parts = url.split("/upload/");
                if (parts.length > 1) {
                    String pathWithVersion = parts[1];
                    String pathWithoutVersion = pathWithVersion.replaceFirst("v\\d+/", "");
                    return pathWithoutVersion.substring(0, pathWithoutVersion.lastIndexOf('.'));
                }
            } catch (Exception e) {
                // Se non riesci a estrarre, usa un fallback
                System.err.println("Error extracting public_id: " + e.getMessage());
            }
            return "unknown";
        }
    private UserEntity getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
