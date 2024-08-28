package com.abcRestaurant.abcRestaurant.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Path getPath(String filename) {
        return Paths.get(uploadDir).resolve(filename);
    }

    public Resource loadAsResource(String filename) throws IOException {
        Path file = getPath(filename);
        return new UrlResource(file.toUri());
    }

    public void saveFile(byte[] fileBytes, String filename) throws IOException {
        Path targetLocation = getPath(filename);
        Files.write(targetLocation, fileBytes);
    }
}
