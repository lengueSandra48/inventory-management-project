package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.CommandeFournisseurRequestDto;
import com.team48.gestiondestock.dto.CommandeFournisseurResponseDto;
import com.team48.gestiondestock.dto.LigneCommandeFournisseurRequestDto;

import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurResponseDto save(CommandeFournisseurRequestDto dto);

    CommandeFournisseurResponseDto findById(Integer id);

    CommandeFournisseurResponseDto findByCodeCommandeFournisseur(String code);

    List<CommandeFournisseurResponseDto> findAll();

    void delete(Integer id);

    CommandeFournisseurResponseDto update(Integer id, CommandeFournisseurRequestDto dto);

    // méthodes de gestion des lignes
    CommandeFournisseurResponseDto addLigne(Integer commandeId, LigneCommandeFournisseurRequestDto ligneDto);
    CommandeFournisseurResponseDto updateLigne(Integer commandeId, LigneCommandeFournisseurRequestDto ligneDto);
    CommandeFournisseurResponseDto removeLigne(Integer commandeId, Integer ligneId);
}

