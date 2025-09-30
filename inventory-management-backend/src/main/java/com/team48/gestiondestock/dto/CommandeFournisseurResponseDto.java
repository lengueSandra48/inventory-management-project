package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.CommandeFournisseur;
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
public class CommandeFournisseurResponseDto {
    private Integer id;
    private String code;
    private Instant dateCommande;
    private FournisseurResponseDto fournisseur;
    private Integer entrepriseId;
    private List<LigneCommandeFournisseurResponseDto> ligneCommandeFournisseurs;

    // Méthode utilitaire pour convertir une entité en DTO
    public static CommandeFournisseurResponseDto fromEntity(CommandeFournisseur commandeFournisseur) {
        if (commandeFournisseur == null) return null;
        return CommandeFournisseurResponseDto.builder()
                .id(commandeFournisseur.getId())
                .code(commandeFournisseur.getCode())
                .dateCommande(commandeFournisseur.getDateCommande())
                .fournisseur(commandeFournisseur.getFournisseur() != null ? 
                    FournisseurResponseDto.fromEntity(commandeFournisseur.getFournisseur()) : null)
                .entrepriseId(commandeFournisseur.getEntreprise_id())
                .build();
    }
} 