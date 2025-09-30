package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Ventes;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VentesResponseDto {
    private Integer id;
    private String code;
    private Instant dateVente;
    private String commentaire;
    private Integer entrepriseId;
    private Integer commandeId;
    private List<LigneVenteResponseDto> ligneVentes;

    // Méthode utilitaire pour convertir une entité en DTO
    public static VentesResponseDto fromEntity(Ventes ventes) {
        if (ventes == null) return null;

        return VentesResponseDto.builder()
                .id(ventes.getId())
                .code(ventes.getCode())
                .dateVente(ventes.getDateVente())
                .commentaire(ventes.getCommentaire())
                .entrepriseId(ventes.getEntreprise_id())
                .commandeId(ventes.getCommandeClient() != null ? ventes.getCommandeClient().getId() : null)
                .ligneVentes(ventes.getLigneVentes() != null
                        ? ventes.getLigneVentes().stream()
                        .map(LigneVenteResponseDto::fromEntity)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
