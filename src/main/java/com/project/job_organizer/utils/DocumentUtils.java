package com.project.job_organizer.utils;

import com.project.job_organizer.model.FileType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DocumentUtils {

    // MIME type
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg",
            "image/png"
    ));

    // Maz size
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * Map mimeType to FileType
     */
    public static FileType mapMimeTypeToFileType(String mimeType) {
        if (mimeType == null) {
            return FileType.OTHER;
        }
        switch (mimeType) {
            case "application/pdf":
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return FileType.DOCUMENT;

            case "image/jpeg":
            case "image/png":
                return FileType.IMAGE;

            default:
                return FileType.OTHER;
        }
    }

    /**
     * Check if mimeType is allowed
     */
    public static boolean isMimeTypeAllowed(String mimeType) {
        return ALLOWED_MIME_TYPES.contains(mimeType);
    }

    /**
     * Check if file size is allowed
     */
    public static boolean isFileSizeAllowed(long size) {
        return size > 0 && size <= MAX_FILE_SIZE;
    }
}
