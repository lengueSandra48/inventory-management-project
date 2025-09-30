package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Fournisseur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurRequestDto {
    private String nom;
    private String prenom;
    private AdresseRequestDto adresse;
    private String photo;
    private String email;
    private String numTel;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir en entité
    public static Fournisseur toEntity(FournisseurRequestDto dto) {
        if (dto == null) return null;
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom(dto.getNom());
        fournisseur.setPrenom(dto.getPrenom());
        fournisseur.setPhoto(dto.getPhoto());
        fournisseur.setEmail(dto.getEmail());
        fournisseur.setNumTel(dto.getNumTel());
        fournisseur.setAdresse(dto.getAdresse() != null ? AdresseRequestDto.toEntity(dto.getAdresse()) : null);
        // L'entreprise sera associée dans le service à partir de l'id
        return fournisseur;
    }
} 