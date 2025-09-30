package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.UtilisateurApi;
import com.team48.gestiondestock.dto.UtilisateurRequestDto;
import com.team48.gestiondestock.dto.UtilisateurResponseDto;
import com.team48.gestiondestock.dto.AdresseRequestDto;
import com.team48.gestiondestock.service.UtilisateurService;
import com.team48.gestiondestock.service.stockage.MinioFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'ENTREPRISE')")
@RestController
public class UtilisateurController implements UtilisateurApi {

    private final UtilisateurService utilisateurService;
    private final MinioFileStorageService minioFileStorageService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, MinioFileStorageService minioFileStorageService) {
        this.utilisateurService = utilisateurService;
        this.minioFileStorageService = minioFileStorageService;
    }

    @Override
    public ResponseEntity<UtilisateurResponseDto> save(
        @org.springframework.web.bind.annotation.RequestParam("nom") String nom,
        @org.springframework.web.bind.annotation.RequestParam("prenom") String prenom,
        @org.springframework.web.bind.annotation.RequestParam("email") String email,
        @org.springframework.web.bind.annotation.RequestParam("motDePasse") String motDePasse,
        @org.springframework.web.bind.annotation.RequestParam("dateDeNaissance") String dateDeNaissance,
        @org.springframework.web.bind.annotation.RequestParam("adresse1") String adresse1,
        @org.springframework.web.bind.annotation.RequestParam(value = "adresse2", required = false) String adresse2,
        @org.springframework.web.bind.annotation.RequestParam("ville") String ville,
        @org.springframework.web.bind.annotation.RequestParam("codePostal") String codePostal,
        @org.springframework.web.bind.annotation.RequestParam("pays") String pays,
        @org.springframework.web.bind.annotation.RequestParam("entrepriseId") Integer entrepriseId,
        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        AdresseRequestDto adresse = new AdresseRequestDto();
        adresse.setAdresse1(adresse1);
        adresse.setAdresse2(adresse2);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        UtilisateurRequestDto dto = new UtilisateurRequestDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setMotDePasse(motDePasse);
        dto.setAdresse(adresse);
        dto.setEntrepriseId(entrepriseId);
        if (dateDeNaissance != null && !dateDeNaissance.isEmpty()) {
            DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            };
            boolean parsed = false;
            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDate localDate = LocalDate.parse(dateDeNaissance, formatter);
                    dto.setDateDeNaissance(localDate);
                    parsed = true;
                    break;
                } catch (Exception ignored) {}
            }
            if (!parsed) {
                throw new IllegalArgumentException("Format de date de naissance invalide. Formats acceptés : dd-MM-yyyy, dd/MM/yyyy, yyyy/MM/dd, yyyy-MM-dd");
            }
        }
        if (image != null && !image.isEmpty()) {
            String objectName = minioFileStorageService.uploadImage(image, "utilisateurs");
            dto.setPhoto(objectName);
        }
        UtilisateurResponseDto savedUtilisateur = utilisateurService.save(dto);
        return ResponseEntity.ok(savedUtilisateur);
    }

    @Override
    public UtilisateurResponseDto findById(@PathVariable("idUtilisateur") Integer id) {
        return utilisateurService.findById(id);
    }

    @Override
    public List<UtilisateurResponseDto> findAll() {
        return utilisateurService.findAll();
    }

    @Override
    public void delete(@org.springframework.web.bind.annotation.PathVariable("idUtilisateur") Integer id) {
        UtilisateurResponseDto utilisateurToDelete = utilisateurService.findById(id);
        if (utilisateurToDelete != null && utilisateurToDelete.getPhoto() != null && !utilisateurToDelete.getPhoto().isEmpty()) {
            minioFileStorageService.delete(utilisateurToDelete.getPhoto());
        }
        utilisateurService.delete(id);
    }

    @Override
    public ResponseEntity<UtilisateurResponseDto> update(
        @org.springframework.web.bind.annotation.PathVariable("idUtilisateur") Integer idUtilisateur,
        @org.springframework.web.bind.annotation.RequestParam("nom") String nom,
        @org.springframework.web.bind.annotation.RequestParam("prenom") String prenom,
        @org.springframework.web.bind.annotation.RequestParam("email") String email,
        @org.springframework.web.bind.annotation.RequestParam("motDePasse") String motDePasse,
        @org.springframework.web.bind.annotation.RequestParam("dateDeNaissance") String dateDeNaissance,
        @org.springframework.web.bind.annotation.RequestParam("adresse1") String adresse1,
        @org.springframework.web.bind.annotation.RequestParam(value = "adresse2", required = false) String adresse2,
        @org.springframework.web.bind.annotation.RequestParam("ville") String ville,
        @org.springframework.web.bind.annotation.RequestParam("codePostal") String codePostal,
        @org.springframework.web.bind.annotation.RequestParam("pays") String pays,
        @org.springframework.web.bind.annotation.RequestParam("entrepriseId") Integer entrepriseId,
        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    ) {
        AdresseRequestDto adresse = new AdresseRequestDto();
        adresse.setAdresse1(adresse1);
        adresse.setAdresse2(adresse2);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        UtilisateurRequestDto dto = new UtilisateurRequestDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setMotDePasse(motDePasse);
        dto.setAdresse(adresse);
        dto.setEntrepriseId(entrepriseId);
        if (dateDeNaissance != null && !dateDeNaissance.isEmpty()) {
            DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            };
            boolean parsed = false;
            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDate localDate = LocalDate.parse(dateDeNaissance, formatter);
                    dto.setDateDeNaissance(localDate);
                    parsed = true;
                    break;
                } catch (Exception ignored) {}
            }
            if (!parsed) {
                throw new IllegalArgumentException("Format de date de naissance invalide. Formats acceptés : dd-MM-yyyy, dd/MM/yyyy, yyyy/MM/dd, yyyy-MM-dd");
            }
        }
        // Récupérer l'utilisateur existant pour avoir l'ancienne image
        UtilisateurResponseDto existing = utilisateurService.findById(idUtilisateur);
        String oldPhoto = existing != null ? existing.getPhoto() : null;
        // Si nouvelle image, supprimer l'ancienne dans MinIO
        if (image != null && !image.isEmpty()) {
            if (oldPhoto != null && !oldPhoto.isEmpty()) {
                minioFileStorageService.delete(oldPhoto);
            }
            String objectName = minioFileStorageService.uploadImage(image, "utilisateurs");
            dto.setPhoto(objectName);
        } else {
            // Si pas de nouvelle image, garder l'ancienne
            dto.setPhoto(oldPhoto);
        }
        UtilisateurResponseDto updatedUtilisateur = utilisateurService.update(idUtilisateur, dto);
        return ResponseEntity.ok(updatedUtilisateur);
    }
} 