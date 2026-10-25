package com.gcare.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "gif", "webp");

    @Value("${app.upload-dir}")
    private String uploadDir;

    public String save(MultipartFile file) throws IOException {
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        int dot = original.lastIndexOf('.');
        String ext = dot >= 0 ? original.substring(dot + 1).toLowerCase() : "";

        if (!ALLOWED.contains(ext)) {
            throw new IllegalArgumentException("Only JPG, PNG, GIF or WEBP images are allowed");
        }

        String name = System.currentTimeMillis() + "." + ext;      // safe unique name
        Path target = Paths.get(uploadDir).toAbsolutePath().resolve(name);
        Files.createDirectories(target.getParent());
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return name;
    }

    public void delete(String name) {
        if (name == null || name.isBlank()) return;
        try {
            Files.deleteIfExists(Paths.get(uploadDir).toAbsolutePath().resolve(name));
        } catch (IOException ignored) {
            // a failed delete should not stop the request
        }
    }
}