package com.team48.gestiondestock.controller.api;

import com.team48.gestiondestock.dto.FournisseurResponseDto;
import com.team48.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT + "/fournisseurs")
public interface FournisseurApi {

    @Operation(summary = "Créer un nouveau fournisseur")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<FournisseurResponseDto> save(
        @RequestParam("nom") String nom,
        @RequestParam("prenom") String prenom,
        @RequestParam("email") String email,
        @RequestParam("adresse1") String adresse1,
        @RequestParam(value = "adresse2", required = false) String adresse2,
        @RequestParam("ville") String ville,
        @RequestParam("codePostal") String codePostal,
        @RequestParam("pays") String pays,
        @RequestParam("numTel") String numTel,
        @RequestParam("entrepriseId") Integer entrepriseId,
        @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    );

    @GetMapping(value = "/{idFournisseur}", produces = MediaType.APPLICATION_JSON_VALUE)
    FournisseurResponseDto findById(@PathVariable("idFournisseur") Integer id);

    @GetMapping(value = "/showAll", produces = MediaType.APPLICATION_JSON_VALUE)
    List<FournisseurResponseDto> findAll();

    @DeleteMapping(value = "/delete/{idFournisseur}")
    void delete(@PathVariable("idFournisseur") Integer id);

    @Operation(summary = "Mettre à jour un fournisseur existant")
    @PutMapping(value = "/update/{idFournisseur}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<FournisseurResponseDto> update(
        @PathVariable("idFournisseur") Integer idFournisseur,
        @RequestParam("nom") String nom,
        @RequestParam("prenom") String prenom,
        @RequestParam("email") String email,
        @RequestParam("adresse1") String adresse1,
        @RequestParam(value = "adresse2", required = false) String adresse2,
        @RequestParam("ville") String ville,
        @RequestParam("codePostal") String codePostal,
        @RequestParam("pays") String pays,
        @RequestParam("numTel") String numTel,
        @RequestParam("entrepriseId") Integer entrepriseId,
        @RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image
    );
} 