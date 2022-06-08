package de.zeeisl.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    public static class StorageException extends RuntimeException {

        public StorageException(String message) {
            super(message);
        }

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

}