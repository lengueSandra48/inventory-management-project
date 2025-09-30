package com.team48.gestiondestock.controller.api;

import com.team48.gestiondestock.dto.VentesRequestDto;
import com.team48.gestiondestock.dto.VentesResponseDto;
import com.team48.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT + "/ventes")
public interface VentesApi {

    @Operation(summary = "Créer une nouvelle vente")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<VentesResponseDto> save(@RequestBody VentesRequestDto ventesDto);

    @GetMapping(value = "/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    VentesResponseDto findById(@PathVariable("idVente") Integer id);

    @GetMapping(value = "/code/{codeVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    VentesResponseDto findByCodeVente(@PathVariable("codeVente") String codeVente);

    @GetMapping(value = "/showAll", produces = MediaType.APPLICATION_JSON_VALUE)
    List<VentesResponseDto> findAll();

    @DeleteMapping(value = "/delete/{idVente}")
    void delete(@PathVariable("idVente") Integer id);

    @Operation(summary = "Mettre à jour une vente existante")
    @PutMapping(value = "/update/{idVente}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<VentesResponseDto> update(@PathVariable("idVente") Integer idVente, @RequestBody VentesRequestDto ventesDto);
} 