package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.FournisseurRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurValidator {

    public static List<String> validate(FournisseurRequestDto fournisseurDto) {
        List<String> errors = new ArrayList<>();

        if (fournisseurDto == null) {
            errors.add("Veillez renseigner le nom du fournisseur");
            errors.add("Veillez renseigner le prenom du fournisseur");
            errors.add("Veillez renseigner l'email du fournisseur");
            errors.add("Veillez renseigner le numero de telephone du fournisseur");
            return errors;
        }

        if (!StringUtils.hasLength(fournisseurDto.getNom())) {
            errors.add("Veillez renseigner le nom du fournisseur");
        }
        if (!StringUtils.hasLength(fournisseurDto.getPrenom())) {
            errors.add("Veillez renseigner le prenom du fournisseur");
        }
        if (!StringUtils.hasLength(fournisseurDto.getEmail())) {
            errors.add("Veillez renseigner l'email du fournisseur");
        }
        if (!StringUtils.hasLength(fournisseurDto.getNumTel())) {
            errors.add("Veillez renseigner le numero de telephone du fournisseur");
        }
        if (fournisseurDto.getAdresse() == null) {
            errors.add("Veillez renseigner l'adresse du fournisseur");
        }

        return errors;
    }
}
