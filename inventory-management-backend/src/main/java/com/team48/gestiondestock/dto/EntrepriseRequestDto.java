package com.team48.gestiondestock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrepriseRequestDto {
    private String nomEntreprise;
    private String description;
    private String email;
    private AdresseRequestDto adresse;
    private String codeFiscal;
    private String numTel;
    private String steWeb;
    private String photo;
} 