package com.app.godo.services.files;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final Path uploadDir = Paths.get("uploads");

    public String storeFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        try {
            Path targetLocation = this.uploadDir.resolve(uniqueFilename);

            // Copy the file's input stream to the target location.
            // This is the line that actually saves the file.
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            // Return the unique filename, as this will be part of the URL.
            return uniqueFilename;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + uniqueFilename, ex);
        }
    }
}
