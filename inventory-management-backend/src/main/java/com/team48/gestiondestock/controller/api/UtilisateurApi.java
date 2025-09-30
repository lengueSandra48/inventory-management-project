package com.team48.gestiondestock.controller.api;

import com.team48.gestiondestock.dto.UtilisateurResponseDto;
import com.team48.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT + "/utilisateurs")
public interface UtilisateurApi {

    @Operation(summary = "Créer un nouvel utilisateur")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UtilisateurResponseDto> save(
        @RequestParam("nom") String nom,
        @RequestParam("prenom") String prenom,
        @RequestParam("email") String email,
        @RequestParam("motDePasse") String motDePasse,
        @RequestParam("dateDeNaissance") String dateDeNaissance,
        @RequestParam("adresse1") String adresse1,
        @RequestParam(value = "adresse2", required = false) String adresse2,
        @RequestParam("ville") String ville,
        @RequestParam("codePostal") String codePostal,
        @RequestParam("pays") String pays,
        @RequestParam("entrepriseId") Integer entrepriseId,
        @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    );

    @GetMapping(value = "/{idUtilisateur}", produces = MediaType.APPLICATION_JSON_VALUE)
    UtilisateurResponseDto findById(@PathVariable("idUtilisateur") Integer id);

    @GetMapping(value = "/showAll", produces = MediaType.APPLICATION_JSON_VALUE)
    List<UtilisateurResponseDto> findAll();

    @DeleteMapping(value = "/delete/{idUtilisateur}")
    void delete(@PathVariable("idUtilisateur") Integer id);

    @Operation(summary = "Mettre à jour un utilisateur existant")
    @PutMapping(value = "/update/{idUtilisateur}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UtilisateurResponseDto> update(
        @PathVariable("idUtilisateur") Integer idUtilisateur,
        @RequestParam("nom") String nom,
        @RequestParam("prenom") String prenom,
        @RequestParam("email") String email,
        @RequestParam("motDePasse") String motDePasse,
        @RequestParam("dateDeNaissance") String dateDeNaissance,
        @RequestParam("adresse1") String adresse1,
        @RequestParam(value = "adresse2", required = false) String adresse2,
        @RequestParam("ville") String ville,
        @RequestParam("codePostal") String codePostal,
        @RequestParam("pays") String pays,
        @RequestParam("entrepriseId") Integer entrepriseId,
        @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    );
} 