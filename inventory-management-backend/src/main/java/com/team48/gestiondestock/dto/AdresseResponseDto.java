package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Adresse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdresseResponseDto {
    private Integer id;
    private String adresse1;
    private String adresse2;
    private String ville;
    private String codePostal;
    private String pays;


    //Convertir une entité Adresse en AdresseDto
    public static AdresseResponseDto fromEntity(Adresse adresse) {
        if (adresse == null) {
            return null;
            //TODO throw an exception
        }

        return AdresseResponseDto.builder()
                .adresse1(adresse.getAdresse1())
                .adresse2(adresse.getAdresse2())
                .ville(adresse.getVille())
                .codePostal(adresse.getCodePostal())
                .pays(adresse.getPays())
                .build();
    }

    //convertir AdresseDto et Adresse
    public static Adresse toEntity(AdresseResponseDto adresseDto) {
        if (adresseDto == null) {
            return null;
            //TODO throw an exception
        }

        Adresse adresse = new Adresse();
        adresse.setAdresse1(adresseDto.getAdresse1());
        adresse.setAdresse2(adresseDto.getAdresse2());
        adresse.setVille(adresseDto.getVille());
        adresse.setCodePostal(adresseDto.getCodePostal());
        adresse.setPays(adresseDto.getPays());
        return adresse;
    }

}


