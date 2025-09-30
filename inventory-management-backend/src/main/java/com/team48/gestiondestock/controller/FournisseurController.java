package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.FournisseurApi;
import com.team48.gestiondestock.dto.FournisseurRequestDto;
import com.team48.gestiondestock.dto.FournisseurResponseDto;
import com.team48.gestiondestock.dto.AdresseRequestDto;
import com.team48.gestiondestock.service.FournisseurService;
import com.team48.gestiondestock.service.stockage.MinioFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENTREPRISE')")
@RestController
public class FournisseurController implements FournisseurApi {

    private final FournisseurService fournisseurService;
    private final MinioFileStorageService minioFileStorageService;

    @Autowired
    public FournisseurController(FournisseurService fournisseurService, MinioFileStorageService minioFileStorageService) {
        this.fournisseurService = fournisseurService;
        this.minioFileStorageService = minioFileStorageService;
    }

    @Override
    public ResponseEntity<FournisseurResponseDto> save(
        @org.springframework.web.bind.annotation.RequestParam("nom") String nom,
        @org.springframework.web.bind.annotation.RequestParam("prenom") String prenom,
        @org.springframework.web.bind.annotation.RequestParam("email") String email,
        @org.springframework.web.bind.annotation.RequestParam("adresse1") String adresse1,
        @org.springframework.web.bind.annotation.RequestParam(value = "adresse2", required = false) String adresse2,
        @org.springframework.web.bind.annotation.RequestParam("ville") String ville,
        @org.springframework.web.bind.annotation.RequestParam("codePostal") String codePostal,
        @org.springframework.web.bind.annotation.RequestParam("pays") String pays,
        @org.springframework.web.bind.annotation.RequestParam("numTel") String numTel,
        @org.springframework.web.bind.annotation.RequestParam("entrepriseId") Integer entrepriseId,
        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        AdresseRequestDto adresse = new AdresseRequestDto();
        adresse.setAdresse1(adresse1);
        adresse.setAdresse2(adresse2);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        FournisseurRequestDto dto = new FournisseurRequestDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setAdresse(adresse);
        dto.setNumTel(numTel);
        dto.setEntrepriseId(entrepriseId);
        if (image != null && !image.isEmpty()) {
            String objectName = minioFileStorageService.uploadImage(image, "fournisseurs");
            dto.setPhoto(objectName);
        }
        FournisseurResponseDto savedFournisseur = fournisseurService.save(dto);
        return ResponseEntity.ok(savedFournisseur);
    }

    @Override
    public FournisseurResponseDto findById(@PathVariable("idFournisseur") Integer id) {
        return fournisseurService.findById(id);
    }

    @Override
    public List<FournisseurResponseDto> findAll() {
        return fournisseurService.findAll();
    }

    @Override
    public void delete(@org.springframework.web.bind.annotation.PathVariable("idFournisseur") Integer id) {
        FournisseurResponseDto fournisseurToDelete = fournisseurService.findById(id);
        if (fournisseurToDelete != null && fournisseurToDelete.getPhoto() != null && !fournisseurToDelete.getPhoto().isEmpty()) {
            minioFileStorageService.delete(fournisseurToDelete.getPhoto());
        }
        fournisseurService.delete(id);
    }

    @Override
    public ResponseEntity<FournisseurResponseDto> update(
        @org.springframework.web.bind.annotation.PathVariable("idFournisseur") Integer idFournisseur,
        @org.springframework.web.bind.annotation.RequestParam("nom") String nom,
        @org.springframework.web.bind.annotation.RequestParam("prenom") String prenom,
        @org.springframework.web.bind.annotation.RequestParam("email") String email,
        @org.springframework.web.bind.annotation.RequestParam("adresse1") String adresse1,
        @org.springframework.web.bind.annotation.RequestParam(value = "adresse2", required = false) String adresse2,
        @org.springframework.web.bind.annotation.RequestParam("ville") String ville,
        @org.springframework.web.bind.annotation.RequestParam("codePostal") String codePostal,
        @org.springframework.web.bind.annotation.RequestParam("pays") String pays,
        @org.springframework.web.bind.annotation.RequestParam("numTel") String numTel,
        @org.springframework.web.bind.annotation.RequestParam("entrepriseId") Integer entrepriseId,
        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        AdresseRequestDto adresse = new AdresseRequestDto();
        adresse.setAdresse1(adresse1);
        adresse.setAdresse2(adresse2);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        FournisseurRequestDto dto = new FournisseurRequestDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setAdresse(adresse);
        dto.setNumTel(numTel);
        dto.setEntrepriseId(entrepriseId);
        // Récupérer le fournisseur existant pour avoir l'ancienne image
        FournisseurResponseDto existing = fournisseurService.findById(idFournisseur);
        String oldPhoto = existing != null ? existing.getPhoto() : null;
        // Si nouvelle image, supprimer l'ancienne dans MinIO
        if (image != null && !image.isEmpty()) {
            if (oldPhoto != null && !oldPhoto.isEmpty()) {
                minioFileStorageService.delete(oldPhoto);
            }
            String objectName = minioFileStorageService.uploadImage(image, "fournisseurs");
            dto.setPhoto(objectName);
        } else {
            // Si pas de nouvelle image, garder l'ancienne
            dto.setPhoto(oldPhoto);
        }
        FournisseurResponseDto updatedFournisseur = fournisseurService.update(idFournisseur, dto);
        return ResponseEntity.ok(updatedFournisseur);
    }
} 