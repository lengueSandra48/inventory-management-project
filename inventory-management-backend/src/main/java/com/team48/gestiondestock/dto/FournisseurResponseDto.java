package com.team48.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team48.gestiondestock.model.Fournisseur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurResponseDto {
    private Integer id;
    private String nom;
    private String prenom;
    private AdresseResponseDto adresse;
    private String photo;
    private String email;
    private String numTel;
    private EntrepriseResponseDto entreprise;

    @JsonIgnore
    private List<CommandeFournisseurResponseDto> commandeFournisseurs;

    // Méthode utilitaire pour convertir une entité en DTO
    public static FournisseurResponseDto fromEntity(Fournisseur fournisseur) {
        if (fournisseur == null) return null;
        return FournisseurResponseDto.builder()
                .id(fournisseur.getId())
                .nom(fournisseur.getNom())
                .prenom(fournisseur.getPrenom())
                .photo(fournisseur.getPhoto())
                .email(fournisseur.getEmail())
                .numTel(fournisseur.getNumTel())
                .adresse(fournisseur.getAdresse() != null ? AdresseResponseDto.fromEntity(fournisseur.getAdresse()) : null)
                .entreprise(fournisseur.getEntreprise() != null ? EntrepriseResponseDto.fromEntity(fournisseur.getEntreprise()) : null)
                .build();
    }
} 