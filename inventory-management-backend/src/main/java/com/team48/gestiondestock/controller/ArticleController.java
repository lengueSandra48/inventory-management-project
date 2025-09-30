package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.ArticleApi;
import com.team48.gestiondestock.dto.ArticleRequestDto;
import com.team48.gestiondestock.dto.ArticleResponseDto;
import com.team48.gestiondestock.service.ArticleService;
import com.team48.gestiondestock.service.stockage.MinioFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENTREPRISE')")
public class ArticleController implements ArticleApi {

    private final ArticleService articleService;
    private final MinioFileStorageService minioFileStorageService;

    @Autowired
    public ArticleController(ArticleService articleService, MinioFileStorageService minioFileStorageService) {
        this.articleService = articleService;
        this.minioFileStorageService = minioFileStorageService;
    }

    @Override
    public ResponseEntity<ArticleResponseDto> save(
        @org.springframework.web.bind.annotation.RequestParam("codeArticle") String codeArticle,
        @org.springframework.web.bind.annotation.RequestParam("designation") String designation,
        @org.springframework.web.bind.annotation.RequestParam("categorieId") Integer categorieId,
        @org.springframework.web.bind.annotation.RequestParam("entrepriseId") Integer entrepriseId,
        @org.springframework.web.bind.annotation.RequestParam("prixUnitaire") java.math.BigDecimal prixUnitaire,
        @org.springframework.web.bind.annotation.RequestParam("tauxTva") java.math.BigDecimal tauxTva,
        @org.springframework.web.bind.annotation.RequestParam("prixUnitaireTtc") java.math.BigDecimal prixUnitaireTtc,
        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        ArticleRequestDto dto = new ArticleRequestDto();
        dto.setCodeArticle(codeArticle);
        dto.setDesignation(designation);
        dto.setCategorieId(categorieId);
        dto.setEntrepriseId(entrepriseId);
        dto.setPrixUnitaire(prixUnitaire);
        dto.setTauxTva(tauxTva);
        dto.setPrixUnitaireTtc(prixUnitaireTtc);
        if (image != null && !image.isEmpty()) {
            String objectName = minioFileStorageService.uploadImage(image, "articles");
            dto.setPhoto(objectName);
        }
        ArticleResponseDto savedArticle = articleService.save(dto);
        return ResponseEntity.ok(savedArticle);
    }

    @Override
    public ArticleResponseDto findById(@org.springframework.web.bind.annotation.PathVariable("idArticle") Integer id) {
        return articleService.findById(id);
    }

    @Override
    public ArticleResponseDto findByCodeArticle(@org.springframework.web.bind.annotation.PathVariable("codeArticle") String codeArticle) {
        return articleService.findByCodeArticle(codeArticle);
    }

    @Override
    public List<ArticleResponseDto> findAll() {
        return articleService.findAll();
    }

    @Override
    public void delete(Integer id) {
        ArticleResponseDto articleToDelete = articleService.findById(id);
        if (articleToDelete != null && StringUtils.hasText(articleToDelete.getPhoto())) {
            minioFileStorageService.delete(articleToDelete.getPhoto());
        }
        articleService.delete(id);
    }

    @Override
    public ResponseEntity<ArticleResponseDto> update(
        @org.springframework.web.bind.annotation.PathVariable("idArticle") Integer idArticle,
        @org.springframework.web.bind.annotation.RequestParam("codeArticle") String codeArticle,
        @org.springframework.web.bind.annotation.RequestParam("designation") String designation,
        @org.springframework.web.bind.annotation.RequestParam("categorieId") Integer categorieId,
        @org.springframework.web.bind.annotation.RequestParam("entrepriseId") Integer entrepriseId,
        @org.springframework.web.bind.annotation.RequestParam("prixUnitaire") java.math.BigDecimal prixUnitaire,
        @org.springframework.web.bind.annotation.RequestParam("tauxTva") java.math.BigDecimal tauxTva,
        @org.springframework.web.bind.annotation.RequestParam("prixUnitaireTtc") java.math.BigDecimal prixUnitaireTtc,
        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        ArticleRequestDto dto = new ArticleRequestDto();
        dto.setCodeArticle(codeArticle);
        dto.setDesignation(designation);
        dto.setCategorieId(categorieId);
        dto.setEntrepriseId(entrepriseId);
        dto.setPrixUnitaire(prixUnitaire);
        dto.setTauxTva(tauxTva);
        dto.setPrixUnitaireTtc(prixUnitaireTtc);
        // Récupérer l'article existant pour avoir l'ancienne image
        ArticleResponseDto existing = articleService.findById(idArticle);
        String oldPhoto = existing != null ? existing.getPhoto() : null;
        // Si nouvelle image, supprimer l'ancienne dans MinIO
        if (image != null && !image.isEmpty()) {
            if (oldPhoto != null && !oldPhoto.isEmpty()) {
                minioFileStorageService.delete(oldPhoto);
            }
            String objectName = minioFileStorageService.uploadImage(image, "articles");
            dto.setPhoto(objectName);
        } else {
            // Si pas de nouvelle image, garder l'ancienne
            dto.setPhoto(oldPhoto);
        }
        ArticleResponseDto updatedArticle = articleService.update(idArticle, dto);
        return ResponseEntity.ok(updatedArticle);
    }
}