package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.LigneCommandeClientRequestDto;

import java.util.ArrayList;
import java.util.List;

public class LigneCommandeClientValidator {

    public static List<String> validate(LigneCommandeClientRequestDto ligneCommandeClientDto){
        List<String> errors = new ArrayList<>();

        if (ligneCommandeClientDto.getArticleId() == null){
            errors.add("Veillez renseigner l'identifiant de l'article de la ligne de commande client");
        }
        if (ligneCommandeClientDto.getQuantite() == null){
            errors.add("Veillez renseigner la quantité de la ligne de commande client");
        }

        return errors;
    }
}
