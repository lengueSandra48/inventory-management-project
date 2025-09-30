package com.team48.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team48.gestiondestock.model.Categorie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorieResponseDto {
    private Integer id;
    private String code;
    private String designation;
    private Integer entrepriseId;

    @JsonIgnore
    private List<ArticleResponseDto> articles;

    // Méthode utilitaire pour convertir une entité en DTO
    public static CategorieResponseDto fromEntity(Categorie categorie) {
        if (categorie == null) return null;
        return CategorieResponseDto.builder()
                .id(categorie.getId())
                .code(categorie.getCode())
                .designation(categorie.getDesignation())
                .entrepriseId(categorie.getEntrepriseId())
                .build();
    }
} 