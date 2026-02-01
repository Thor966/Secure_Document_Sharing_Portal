package com.doc.util;

import java.util.List;

public final class FileTypeValidator {

    private FileTypeValidator() {}

    public static final List<String> ALLOWED_MIME_TYPES = List.of(
        "application/pdf",

        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",

        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",

        "application/vnd.ms-powerpoint",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",

        "text/plain",
        "text/csv",

        "image/jpeg",
        "image/png",
        "image/webp"
    );

    public static boolean isValid(String mimeType) {
        return ALLOWED_MIME_TYPES.contains(mimeType);
    }
}
