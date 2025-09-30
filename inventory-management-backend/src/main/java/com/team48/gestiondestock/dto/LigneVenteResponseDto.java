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
public class LigneVenteResponseDto {
    private Integer id;
    private VentesResponseDto vente;
    private ArticleResponseDto article;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir une entité en DTO
    public static LigneVenteResponseDto fromEntity(LigneVente ligneVente) {
        if (ligneVente == null) return null;

        return LigneVenteResponseDto.builder()
                .id(ligneVente.getId())
                .vente(null)
                .article(ligneVente.getArticle() != null
                        ? ArticleResponseDto.fromEntity(ligneVente.getArticle())
                        : null)
                .quantite(ligneVente.getQuantite())
                .prixUnitaire(ligneVente.getPrixUnitaire())
                .entrepriseId(ligneVente.getEntreprise_id())
                .build();
    }
}
