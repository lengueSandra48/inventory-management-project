package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Ventes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentesRequestDto {
    private String code;
    private Instant dateVente;
    private String commentaire;
    private Integer entrepriseId;
    private Integer commandeId;
    private List<LigneVenteRequestDto> ligneVentes;

    // Méthode utilitaire pour convertir en entité
    public static Ventes toEntity(VentesRequestDto dto) {
        if (dto == null) return null;
        Ventes ventes = new Ventes();
        ventes.setCode(dto.getCode());
        ventes.setDateVente(dto.getDateVente());
        ventes.setCommentaire(dto.getCommentaire());
        ventes.setEntreprise_id(dto.getEntrepriseId());
        return ventes;
    }

}
