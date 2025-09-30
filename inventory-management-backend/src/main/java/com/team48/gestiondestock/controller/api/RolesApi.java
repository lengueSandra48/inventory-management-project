package com.team48.gestiondestock.controller.api;

import com.team48.gestiondestock.dto.RolesRequestDto;
import com.team48.gestiondestock.dto.RolesResponseDto;
import com.team48.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(Constants.APP_ROOT + "/roles")
public interface RolesApi {

    @Operation(summary = "Créer un nouveau rôle")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RolesResponseDto> save(@RequestBody RolesRequestDto rolesDto);

    @GetMapping(value = "/{idRole}", produces = MediaType.APPLICATION_JSON_VALUE)
    RolesResponseDto findById(@PathVariable("idRole") Integer id);

    @GetMapping(value = "/showAll", produces = MediaType.APPLICATION_JSON_VALUE)
    List<RolesResponseDto> findAll();

    @Operation(summary = "Supprimer un rôle par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rôle supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rôle non trouvé")
    })
    @DeleteMapping(value = "/delete/{idRole}")
    ResponseEntity<Void> delete(@PathVariable("idRole") Integer id);

    @Operation(summary = "Mettre à jour un rôle existant")
    @PutMapping(value = "/update/{idRole}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RolesResponseDto> update(@PathVariable("idRole") Integer idRole, @RequestBody RolesRequestDto rolesDto);
}