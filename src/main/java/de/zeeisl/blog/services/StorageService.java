package de.zeeisl.blog.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    public static class StorageException extends RuntimeException {

        public StorageException(String message) {
            super(message);
        }

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    String store(MultipartFile file);

}