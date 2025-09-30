package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.ClientApi;
import com.team48.gestiondestock.dto.ClientRequestDto;
import com.team48.gestiondestock.dto.ClientResponseDto;
import com.team48.gestiondestock.dto.AdresseRequestDto;
import com.team48.gestiondestock.service.ClientService;
import com.team48.gestiondestock.service.stockage.MinioFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENTREPRISE', 'USER')")
@RestController
public class ClientController implements ClientApi {

    private final ClientService clientService;
    private final MinioFileStorageService minioFileStorageService;

    @Autowired
    public ClientController(ClientService clientService, MinioFileStorageService minioFileStorageService) {
        this.clientService = clientService;
        this.minioFileStorageService = minioFileStorageService;
    }

    @Override
    public ResponseEntity<ClientResponseDto> save(
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
        ClientRequestDto dto = new ClientRequestDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setAdresse(adresse);
        dto.setNumTel(numTel);
        dto.setEntrepriseId(entrepriseId);
        if (image != null && !image.isEmpty()) {
            String objectName = minioFileStorageService.uploadImage(image, "clients");
            dto.setPhoto(objectName);
        }
        ClientResponseDto savedClient = clientService.save(dto);
        return ResponseEntity.ok(savedClient);
    }

    @Override
    public ClientResponseDto findById(@PathVariable("idClient") Integer id) {
        return clientService.findById(id);
    }

    @Override
    public List<ClientResponseDto> findAll() {
        return clientService.findAll();
    }

    @Override
    public void delete(@org.springframework.web.bind.annotation.PathVariable("idClient") Integer id) {
        ClientResponseDto clientToDelete = clientService.findById(id);
        if (clientToDelete != null && clientToDelete.getPhoto() != null && !clientToDelete.getPhoto().isEmpty()) {
            minioFileStorageService.delete(clientToDelete.getPhoto());
        }
        clientService.delete(id);
    }

    @Override
    public ResponseEntity<ClientResponseDto> update(
        @org.springframework.web.bind.annotation.PathVariable("idClient") Integer idClient,
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
        ClientRequestDto dto = new ClientRequestDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setAdresse(adresse);
        dto.setNumTel(numTel);
        dto.setEntrepriseId(entrepriseId);
        // Récupérer le client existant pour avoir l'ancienne image
        ClientResponseDto existing = clientService.findById(idClient);
        String oldPhoto = existing != null ? existing.getPhoto() : null;
        // Si nouvelle image, supprimer l'ancienne dans MinIO
        if (image != null && !image.isEmpty()) {
            if (oldPhoto != null && !oldPhoto.isEmpty()) {
                minioFileStorageService.delete(oldPhoto);
            }
            String objectName = minioFileStorageService.uploadImage(image, "clients");
            dto.setPhoto(objectName);
        } else {
            // Si pas de nouvelle image, garder l'ancienne
            dto.setPhoto(oldPhoto);
        }
        ClientResponseDto updatedClient = clientService.update(idClient, dto);
        return ResponseEntity.ok(updatedClient);
    }
} 