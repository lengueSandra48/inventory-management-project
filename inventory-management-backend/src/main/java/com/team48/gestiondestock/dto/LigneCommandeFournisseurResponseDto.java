package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.LigneCommandeFournisseur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeFournisseurResponseDto {
    private Integer id;
    private CommandeFournisseurResponseDto commandeFournisseur;
    private ArticleResponseDto article;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir une entité en DTO
    public static LigneCommandeFournisseurResponseDto fromEntity(LigneCommandeFournisseur ligneCommandeFournisseur) {
        if (ligneCommandeFournisseur == null) return null;
        return LigneCommandeFournisseurResponseDto.builder()
                .id(ligneCommandeFournisseur.getId())
                .quantite(ligneCommandeFournisseur.getQuantite())
                .prixUnitaire(ligneCommandeFournisseur.getPrixUnitaire())
                .entrepriseId(ligneCommandeFournisseur.getEntreprise_id())
                .build();
    }
} 