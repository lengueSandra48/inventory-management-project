package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.VentesRequestDto;
import com.team48.gestiondestock.dto.VentesResponseDto;

import java.util.List;

public interface VentesService {

    /**
     * Crée une vente à partir d'une commande existante.
     * @param ventesDto données de la vente (sans lignes)
     * @param commandeId ID de la commande dont les lignes seront reprises
     * @return DTO de la vente créée
     */
    VentesResponseDto save(VentesRequestDto ventesDto, Integer commandeId);

    VentesResponseDto findById(Integer id);

    VentesResponseDto findByCodeVente(String code);

    List<VentesResponseDto> findAll();

    void delete(Integer id);

    VentesResponseDto update(Integer id, VentesRequestDto ventesDto);
}
