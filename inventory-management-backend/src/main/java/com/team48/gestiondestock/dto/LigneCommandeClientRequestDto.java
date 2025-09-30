package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.LigneCommandeClient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeClientRequestDto {
    @Schema(hidden=true)
    private Integer id;
    private Integer commandeClientId;
    private Integer articleId;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir en entité
    public static LigneCommandeClient toEntity(LigneCommandeClientRequestDto dto) {
        if (dto == null) return null;
        LigneCommandeClient ligne = new LigneCommandeClient();
        ligne.setQuantite(dto.getQuantite());
        ligne.setPrixUnitaire(dto.getPrixUnitaire());
        ligne.setEntreprise_id(dto.getEntrepriseId());
        // L'association de l'article se fait dans le service à partir de l'id
        return ligne;
    }


}