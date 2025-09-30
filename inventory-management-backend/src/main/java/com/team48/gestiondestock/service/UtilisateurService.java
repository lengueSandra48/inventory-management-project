package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.UtilisateurRequestDto;
import com.team48.gestiondestock.dto.UtilisateurResponseDto;

import java.util.List;

public interface UtilisateurService {
    UtilisateurResponseDto save(UtilisateurRequestDto utilisateurDto);

    UtilisateurResponseDto findById(Integer id);

    UtilisateurResponseDto findByEmailUser(String email);

    List<UtilisateurResponseDto> findAll();

    void delete(Integer id);

    UtilisateurResponseDto update(Integer id, UtilisateurRequestDto utilisateurDto);
}
