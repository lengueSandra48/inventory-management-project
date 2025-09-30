package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.CategorieApi;
import com.team48.gestiondestock.dto.CategorieRequestDto;
import com.team48.gestiondestock.dto.CategorieResponseDto;
import com.team48.gestiondestock.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENTREPRISE')")
@RestController
public class CategorieController implements CategorieApi {

    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @Override
    public ResponseEntity<CategorieResponseDto> save(@org.springframework.web.bind.annotation.RequestBody CategorieRequestDto categorieDto) {
        CategorieResponseDto savedCategorie = categorieService.save(categorieDto);
        return ResponseEntity.ok(savedCategorie);
    }

    @Override
    public CategorieResponseDto findById(@PathVariable("idCategorie") Integer id) {
        return categorieService.findById(id);
    }

    @Override
    public List<CategorieResponseDto> findAll() {
        return categorieService.findAll();
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("idCategorie") Integer id) {
        categorieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategorieResponseDto> update(@PathVariable("idCategorie") Integer idCategorie, @org.springframework.web.bind.annotation.RequestBody CategorieRequestDto categorieDto) {
        CategorieResponseDto updatedCategorie = categorieService.update(idCategorie, categorieDto);
        return ResponseEntity.ok(updatedCategorie);
    }

    @Override
    public CategorieResponseDto findByCodeCategorie(@PathVariable("codeCategorie") String codeCategorie) {
        return categorieService.findByCodeCategorie(codeCategorie);
    }
} 