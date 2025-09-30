package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.LigneVenteRequestDto;

import java.util.ArrayList;
import java.util.List;

public class LigneVenteValidator {

    public static List<String> validate(LigneVenteRequestDto ligneVenteDto) {
        List<String> errors = new ArrayList<>();

        if (ligneVenteDto.getArticleId() == null) {
            errors.add("Veillez renseigner l'identifiant de l'article de la ligne de vente");
        }
        if (ligneVenteDto.getQuantite()== null) {
            errors.add("Veillez renseigner la quantité de la ligne de vente");
        }
        if (ligneVenteDto.getPrixUnitaire() == null) {
            errors.add("Veillez renseigner le prix unitaire de la ligne de vente");
        }

        return errors;
    }
}
