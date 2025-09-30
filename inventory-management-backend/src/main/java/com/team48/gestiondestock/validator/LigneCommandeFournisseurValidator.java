package com.team48.gestiondestock.validator;

import java.util.ArrayList;
import java.util.List;

import com.team48.gestiondestock.dto.LigneCommandeFournisseurRequestDto;

public class LigneCommandeFournisseurValidator {

    public static List<String> validate(LigneCommandeFournisseurRequestDto ligneCommandeFournisseurDto){
        List<String> errors = new ArrayList<>();

        if (ligneCommandeFournisseurDto.getArticleId() == null){
            errors.add("Veillez renseigner l'identifiant de l'article de la ligne de commande fournisseur");
        }
        if (ligneCommandeFournisseurDto.getQuantite() == null){
            errors.add("Veillez renseigner la quantité de la ligne de commande fournisseur");
        }

        return errors;
    }
}
