package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Categorie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorieRequestDto {
    private String code;
    private String designation;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir en entité
    public static Categorie toEntity(CategorieRequestDto dto) {
        if (dto == null) return null;
        Categorie categorie = new Categorie();
        categorie.setCode(dto.getCode());
        categorie.setDesignation(dto.getDesignation());
        categorie.setEntrepriseId(dto.getEntrepriseId());
        return categorie;
    }
} 