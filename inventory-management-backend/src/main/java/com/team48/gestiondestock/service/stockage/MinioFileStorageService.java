// com/loic/gestiondestock/service/stockage/MinioFileStorageService.java
package com.team48.gestiondestock.service.stockage;

import com.team48.gestiondestock.exception.InvalidImageFormatException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Service
public class MinioFileStorageService {
    private final MinioClient minioClient;
    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioFileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) return null;
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new InvalidImageFormatException("Nom de fichier invalide");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowed = List.of("jpg", "jpeg", "png", "svg");
        if (!allowed.contains(extension)) {
            throw new InvalidImageFormatException("Format de fichier non autorisé. Formats acceptés: " + String.join(", ", allowed));
        }
        String uniqueSuffix = UUID.randomUUID().toString();
        String objectName = folder + "/" + uniqueSuffix + "_" + originalFilename;
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception e) {
            throw new InvalidImageFormatException("Erreur lors de l'upload sur Minio: " + e.getMessage());
        }
        return objectName;
    }

    public void delete(String objectName) {
        try {
            minioClient.removeObject(
                io.minio.RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'image sur Minio: " + e.getMessage());
        }
    }
}