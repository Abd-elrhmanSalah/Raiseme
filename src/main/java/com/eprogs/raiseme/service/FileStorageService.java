package com.eprogs.raiseme.service;

import com.eprogs.raiseme.config.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    private final FileStorageProperties fileStorageProperties;

    // Validate image
    private void validate(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("Empty image not allowed");

        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new RuntimeException("Only JPG & PNG allowed");

        if (file.getSize() > MAX_FILE_SIZE)
            throw new RuntimeException("Image size cannot exceed 5MB");
    }

    // Upload multiple images & return relative paths
    public List<String> uploadItemImages(Long itemId, List<MultipartFile> files) {
        List<String> paths = new ArrayList<>();

        Path itemDir = Paths.get(fileStorageProperties.getUploadDir(), String.valueOf(itemId))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(itemDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create upload directory");
        }

        for (MultipartFile file : files) {

            validate(file);

            String ext = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf("."));

            String fileName = UUID.randomUUID() + ext;

            Path target = itemDir.resolve(fileName);

            try {
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Cannot save image");
            }

            // Save relative path
            paths.add("uploads/item/" + itemId + "/" + fileName);
        }
        return paths;
    }

    // Delete specific file
    public void deleteImage(String path) {
        Path file = Paths.get(path).toAbsolutePath();
        try {
            Files.deleteIfExists(file);
        } catch (IOException ignored) {
        }
    }

    // Delete all images for an item
    public void deleteAllItemImages(Long itemId) {
        Path itemDir = Paths.get(fileStorageProperties.getUploadDir(), String.valueOf(itemId))
                .toAbsolutePath();
        try {
            Files.walk(itemDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (Exception ignored) {
        }
    }

}
