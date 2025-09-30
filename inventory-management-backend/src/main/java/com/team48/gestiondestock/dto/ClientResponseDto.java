package com.team48.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team48.gestiondestock.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto {
    private Integer id;
    private String nom;
    private String prenom;
    private AdresseResponseDto adresse;
    private String photo;
    private String email;
    private String numTel;
    private EntrepriseResponseDto entreprise;

    @JsonIgnore
    private List<CommandeClientResponseDto> commandeClients;

    // Méthode utilitaire pour convertir une entité en DTO
    public static ClientResponseDto fromEntity(Client client) {
        if (client == null) return null;
        return ClientResponseDto.builder()
                .id(client.getId())
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .photo(client.getPhoto())
                .email(client.getEmail())
                .numTel(client.getNumTel())
                .adresse(client.getAdresse() != null ? AdresseResponseDto.fromEntity(client.getAdresse()) : null)
                .entreprise(client.getEntreprise() != null ? EntrepriseResponseDto.fromEntity(client.getEntreprise()) : null)
                .build();
    }
} 