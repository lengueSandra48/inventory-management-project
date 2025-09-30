package com.team48.gestiondestock.controller.api;

import com.team48.gestiondestock.dto.ClientResponseDto;
import com.team48.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT + "/clients")
public interface ClientApi {

    @Operation(summary = "Créer un nouveau client")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClientResponseDto> save(
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

    @GetMapping(value = "/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    ClientResponseDto findById(@PathVariable("idClient") Integer id);

    @GetMapping(value = "/showAll", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ClientResponseDto> findAll();

    @DeleteMapping(value = "/delete/{idClient}")
    void delete(@PathVariable("idClient") Integer id);

    @Operation(summary = "Mettre à jour un client existant")
    @PutMapping(value = "/update/{idClient}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ClientResponseDto> update(
        @PathVariable("idClient") Integer idClient,
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