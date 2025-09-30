package com.team48.gestiondestock.controller.api;

import com.team48.gestiondestock.dto.EntrepriseResponseDto;
import com.team48.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping(Constants.APP_ROOT + "/entreprises")
public interface EntrepriseApi {

    @Operation(summary = "Créer une nouvelle entreprise compatible Swagger UI")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntrepriseResponseDto> save(
        @RequestParam("nomEntreprise") String nomEntreprise,
        @RequestParam("description") String description,
        @RequestParam("email") String email,
        @RequestParam("adresse1") String adresse1,
        @RequestParam(value = "adresse2", required = false) String adresse2,
        @RequestParam("ville") String ville,
        @RequestParam("codePostal") String codePostal,
        @RequestParam("pays") String pays,
        @RequestParam("codeFiscal") String codeFiscal,
        @RequestParam("numTel") String numTel,
        @RequestParam("steWeb") String steWeb,
        @RequestPart(value = "photo", required = false) MultipartFile photo
    );

    @GetMapping(value = "/{idEntreprise}", produces = MediaType.APPLICATION_JSON_VALUE)
    EntrepriseResponseDto findById(@PathVariable("idEntreprise") Integer id);

    @GetMapping(value = "/showAll", produces = MediaType.APPLICATION_JSON_VALUE)
    List<EntrepriseResponseDto> findAll();

    @DeleteMapping(value = "/delete/{idEntreprise}")
    void delete(@PathVariable("idEntreprise") Integer id);

    @Operation(summary = "Mettre à jour une entreprise existante compatible Swagger UI")
    @PutMapping(value = "/update/{idEntreprise}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntrepriseResponseDto> update(
        @PathVariable("idEntreprise") Integer idEntreprise,
        @RequestParam("nomEntreprise") String nomEntreprise,
        @RequestParam("description") String description,
        @RequestParam("email") String email,
        @RequestParam("adresse1") String adresse1,
        @RequestParam(value = "adresse2", required = false) String adresse2,
        @RequestParam("ville") String ville,
        @RequestParam("codePostal") String codePostal,
        @RequestParam("pays") String pays,
        @RequestParam("codeFiscal") String codeFiscal,
        @RequestParam("numTel") String numTel,
        @RequestParam("steWeb") String steWeb,
        @RequestPart(value = "photo", required = false) MultipartFile photo
    );
} 