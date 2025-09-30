package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.EntrepriseRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {
    public static List<String> validate (EntrepriseRequestDto entrepriseDto){
        List<String> errors = new ArrayList<>();

        if (entrepriseDto == null){
            errors.add("Veiller renseigner le nom de l'entreprise");
            errors.add("Veiller renseigner la description de l'entreprise");
            errors.add("Veiller renseigner le mail de l'entreprise");
            errors.add("Veiller renseigner l'adresse de l'entreprise");
            errors.add("Veiller renseigner le code fiscal de l'entreprise");
            errors.add("Veiller renseigner le numero de telephone de l'entreprise");
            errors.add("Veiller renseigner le steWeb de l'entreprise");
            errors.add("Veillez renseigner l'adresse de l'entreprise ");
            return errors;
        }

        if(!StringUtils.hasLength(entrepriseDto.getNomEntreprise())){
            errors.add("Veiller renseigner le nom de l'entreprise");
        }
        if(!StringUtils.hasLength(entrepriseDto.getDescription())){
            errors.add("Veiller renseigner la description de l'entreprise");
        }
        if(!StringUtils.hasLength(entrepriseDto.getEmail())){
            errors.add("Veiller renseigner le mail de l'entreprise");
        }
        if(entrepriseDto.getAdresse() == null){
            errors.add("Veiller renseigner l'adresse de l'entreprise");
        }
        if(!StringUtils.hasLength(entrepriseDto.getCodeFiscal())){
            errors.add("Veiller renseigner le code fiscal de l'entreprise");
        }
        if(!StringUtils.hasLength(entrepriseDto.getNumTel())){
            errors.add("Veiller renseigner le numero de telephone de l'entreprise");
        }
        if(!StringUtils.hasLength(entrepriseDto.getSteWeb())){
            errors.add("Veiller renseigner le steWeb de l'entreprise");
        }
        if (entrepriseDto.getAdresse() == null){
            errors.add("Veillez renseigner l'adresse de l'entreprise ");
        }else{
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getAdresse1())) {
                errors.add("Le champs 'Adresse 1' est obligatoir");
            }
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getVille())) {
                errors.add("Le champs 'Ville' est obligatoir");
            }
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getCodePostal())) {
                errors.add("Le champs 'Code postale' est obligatoir");
            }
            if (!StringUtils.hasLength(entrepriseDto.getAdresse().getPays())) {
                errors.add("Le champs 'Pays' est obligatoir");
            }
        }
        return errors;
    }
}
