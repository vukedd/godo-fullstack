package com.app.godo.services.files;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;

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

    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            // Extract filename using string manipulation
            String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);

            Path filePath = this.uploadDir.resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file: " + fileUrl, ex);
        }
    }
}
