package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.EntrepriseApi;
import com.team48.gestiondestock.dto.EntrepriseRequestDto;
import com.team48.gestiondestock.dto.EntrepriseResponseDto;
import com.team48.gestiondestock.dto.AdresseRequestDto;
import com.team48.gestiondestock.service.EntrepriseService;
import com.team48.gestiondestock.service.stockage.MinioFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
public class EntrepriseController implements EntrepriseApi {

    private final EntrepriseService entrepriseService;
    private final MinioFileStorageService minioFileStorageService;

    @Autowired
    public EntrepriseController(EntrepriseService entrepriseService, MinioFileStorageService minioFileStorageService) {
        this.entrepriseService = entrepriseService;
        this.minioFileStorageService = minioFileStorageService;
    }

    @Override
    public ResponseEntity<EntrepriseResponseDto> save(String nomEntreprise, String description, String email, String adresse1, String adresse2, String ville, String codePostal, String pays, String codeFiscal, String numTel, String steWeb, MultipartFile photo) {
        // Construction de l'adresse
        AdresseRequestDto adresse = new AdresseRequestDto();
        adresse.setAdresse1(adresse1);
        adresse.setAdresse2(adresse2);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);

        // Construction du DTO Entreprise
        EntrepriseRequestDto dto = new EntrepriseRequestDto();
        dto.setNomEntreprise(nomEntreprise);
        dto.setDescription(description);
        dto.setEmail(email);
        dto.setAdresse(adresse);
        dto.setCodeFiscal(codeFiscal);
        dto.setNumTel(numTel);
        dto.setSteWeb(steWeb);

        if (photo != null && !photo.isEmpty()) {
            String objectName = minioFileStorageService.uploadImage(photo, "entreprises");
            dto.setPhoto(objectName);
        }
        EntrepriseResponseDto savedEntreprise = entrepriseService.save(dto);
        return ResponseEntity.ok(savedEntreprise);
    }

    @Override
    public EntrepriseResponseDto findById(@PathVariable("idEntreprise") Integer id) {
        return entrepriseService.findById(id);
    }

    @Override
    public List<EntrepriseResponseDto> findAll() {
        return entrepriseService.findAll();
    }

    @Override
    public void delete(@PathVariable("idEntreprise") Integer id) {
        // Récupérer l'entreprise pour obtenir le chemin de l'image
        EntrepriseResponseDto existing = entrepriseService.findById(id);
        if (existing != null && existing.getPhoto() != null && !existing.getPhoto().isEmpty()) {
            minioFileStorageService.delete(existing.getPhoto());
        }
        entrepriseService.delete(id);
    }

    @Override
    public ResponseEntity<EntrepriseResponseDto> update(Integer idEntreprise, String nomEntreprise, String description, String email, String adresse1, String adresse2, String ville, String codePostal, String pays, String codeFiscal, String numTel, String steWeb, MultipartFile photo) {
        // Construction de l'adresse
        AdresseRequestDto adresse = new AdresseRequestDto();
        adresse.setAdresse1(adresse1);
        adresse.setAdresse2(adresse2);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);

        // Construction du DTO Entreprise
        EntrepriseRequestDto dto = new EntrepriseRequestDto();
        dto.setNomEntreprise(nomEntreprise);
        dto.setDescription(description);
        dto.setEmail(email);
        dto.setAdresse(adresse);
        dto.setCodeFiscal(codeFiscal);
        dto.setNumTel(numTel);
        dto.setSteWeb(steWeb);

        // Récupérer l'entreprise existante pour avoir l'ancienne image
        EntrepriseResponseDto existing = entrepriseService.findById(idEntreprise);
        String oldPhoto = existing != null ? existing.getPhoto() : null;

        // Si nouvelle image, supprimer l'ancienne dans MinIO
        if (photo != null && !photo.isEmpty()) {
            if (oldPhoto != null && !oldPhoto.isEmpty()) {
                minioFileStorageService.delete(oldPhoto);
            }
            String objectName = minioFileStorageService.uploadImage(photo, "entreprises");
            dto.setPhoto(objectName);
        } else {
            // Si pas de nouvelle image, garder l'ancienne
            dto.setPhoto(oldPhoto);
        }

        EntrepriseResponseDto updatedEntreprise = entrepriseService.update(idEntreprise, dto);
        return ResponseEntity.ok(updatedEntreprise);
    }
} 