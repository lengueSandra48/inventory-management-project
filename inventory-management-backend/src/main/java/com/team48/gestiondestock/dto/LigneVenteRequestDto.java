package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.LigneVente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneVenteRequestDto {
    private Integer articleId;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir en entité
    public static LigneVente toEntity(LigneVenteRequestDto dto) {
        if (dto == null) return null;
        LigneVente ligne = new LigneVente();
        ligne.setQuantite(dto.getQuantite());
        ligne.setPrixUnitaire(dto.getPrixUnitaire());
        ligne.setEntreprise_id(dto.getEntrepriseId());
        // L'association de l'article se fait dans le service à partir de l'id
        return ligne;
    }
} 