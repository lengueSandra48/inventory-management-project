package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.CategorieRequestDto;
import com.team48.gestiondestock.dto.CategorieResponseDto;

import java.util.List;

public interface CategorieService {

    CategorieResponseDto save(CategorieRequestDto categorieDto);

    CategorieResponseDto findById(Integer id);

    CategorieResponseDto findByCodeCategorie(String code);

    List<CategorieResponseDto> findAll();

    void delete(Integer id);

    CategorieResponseDto update(Integer id, CategorieRequestDto categorieDto);

}
