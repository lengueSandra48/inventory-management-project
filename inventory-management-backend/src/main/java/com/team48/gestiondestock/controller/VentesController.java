package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.VentesApi;
import com.team48.gestiondestock.dto.VentesRequestDto;
import com.team48.gestiondestock.dto.VentesResponseDto;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.service.VentesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'VENDEUR', 'ENTREPRISE')")
@RestController
public class VentesController implements VentesApi {

    private final VentesService ventesService;

    @Autowired
    public VentesController(VentesService ventesService) {
        this.ventesService = ventesService;
    }

    @Override
    public ResponseEntity<VentesResponseDto> save(@RequestBody VentesRequestDto ventesDto) {
        if (ventesDto.getCommandeId() == null) {
            throw new InvalidEntityException(
                    "L'ID de la commande est obligatoire pour créer une vente",
                    ErrorCodes.VENTES_NOT_VALID,
                    Collections.emptyList()
            );
        }

        VentesResponseDto savedVente = ventesService.save(ventesDto, ventesDto.getCommandeId());
        return ResponseEntity.ok(savedVente);
    }


    @Override
    public VentesResponseDto findById(@PathVariable("idVente") Integer id) {
        return ventesService.findById(id);
    }

    @Override
    public VentesResponseDto findByCodeVente(@PathVariable("codeVente") String codeVente) {
        return ventesService.findByCodeVente(codeVente);
    }

    @Override
    public List<VentesResponseDto> findAll() {
        return ventesService.findAll();
    }

    @Override
    public void delete(@PathVariable("idVente") Integer id) {
        ventesService.delete(id);
    }

    @Override
    public ResponseEntity<VentesResponseDto> update(@PathVariable("idVente") Integer idVente, @RequestBody VentesRequestDto ventesDto) {
        VentesResponseDto updatedVente = ventesService.update(idVente, ventesDto);
        return ResponseEntity.ok(updatedVente);
    }
}