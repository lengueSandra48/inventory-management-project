package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.FournisseurRequestDto;
import com.team48.gestiondestock.dto.FournisseurResponseDto;

import java.util.List;

public interface FournisseurService {

    FournisseurResponseDto save(FournisseurRequestDto fournisseurDto);

    FournisseurResponseDto findById(Integer id);

    FournisseurResponseDto findByNomFournisseur(String nom);

    List<FournisseurResponseDto> findAll();

    void delete(Integer id);

    FournisseurResponseDto update(Integer id, FournisseurRequestDto fournisseurDto);
}
