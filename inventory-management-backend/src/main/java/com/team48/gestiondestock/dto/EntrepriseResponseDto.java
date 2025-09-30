package com.team48.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team48.gestiondestock.model.Entreprise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepriseResponseDto {
    private Integer id;
    private String nomEntreprise;
    private String description;
    private String photo;
    private String email;
    private AdresseResponseDto adresse;
    private String codeFiscal;
    private String numTel;
    private  String steWeb;
    //private Integer entreprise_id;
    private Instant creationDate;

    @JsonIgnore
    private List<UtilisateurResponseDto> utilisateurs;

    @JsonIgnore
    private List<ArticleResponseDto> articles;

    //mapping de l'entité Entreprise vers EntrepriseDto
    public static EntrepriseResponseDto fromEntity(Entreprise entreprise) {
        if (entreprise == null) {
            return  null;
            //TODO throw an exception
        }
        return EntrepriseResponseDto.builder()
                .id(entreprise.getId())
                .nomEntreprise(entreprise.getNomEntreprise())
                .description(entreprise.getDescription())
                .photo(entreprise.getPhoto())
                .email(entreprise.getEmail())
                .adresse(entreprise.getAdresse() != null ? AdresseResponseDto.fromEntity(entreprise.getAdresse()) : null)
                .codeFiscal(entreprise.getCodeFiscal())
                .numTel(entreprise.getNumTel())
                .steWeb(entreprise.getSteWeb())
                //.entreprise_id(entreprise.getId())
                .creationDate(entreprise.getCreationDate())
                .build();
    }

    //mapping de EntrepriseDto vers l'entité Entreprise
    public static Entreprise toEntity(EntrepriseResponseDto entrepriseDto) {
        if (entrepriseDto == null) {
            return  null;
            //TODO throw an exception
        }
        Entreprise entreprise = new Entreprise();
        entreprise.setId(entrepriseDto.getId());
        entreprise.setNomEntreprise(entrepriseDto.getNomEntreprise());
        entreprise.setDescription(entrepriseDto.getDescription());
        entreprise.setPhoto(entrepriseDto.getPhoto());
        entreprise.setEmail(entrepriseDto.getEmail());
        entreprise.setCodeFiscal(entrepriseDto.getCodeFiscal());
        entreprise.setNumTel(entrepriseDto.getNumTel());
        entreprise.setSteWeb(entrepriseDto.getSteWeb());
        entreprise.setAdresse(entrepriseDto.getAdresse() != null ? AdresseResponseDto.toEntity(entrepriseDto.getAdresse()) : null);
        if (entrepriseDto.getCreationDate() != null) {
            entreprise.setCreationDate(entrepriseDto.getCreationDate());
        } else {
            entreprise.setCreationDate(java.time.Instant.now());
        }
        return entreprise;
    }
}
