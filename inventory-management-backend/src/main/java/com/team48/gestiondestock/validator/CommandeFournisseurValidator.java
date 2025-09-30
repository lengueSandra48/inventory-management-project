package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.CommandeFournisseurRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandeFournisseurValidator {

    public static List<String> validate(CommandeFournisseurRequestDto commandeFournisseurDto) {
        List<String> errors = new ArrayList<>();

        if (commandeFournisseurDto == null){
            errors.add("Veuillez renseigner le code de la commande fournisseur");
            errors.add("Veuillez renseigner la date de la commande fournisseur");
            return errors;
        }

        if (!StringUtils.hasLength(commandeFournisseurDto.getCode())) {
            errors.add("Veuillez renseigner le code de la commande fournisseur");
        }
        if (commandeFournisseurDto.getDateCommande() == null) {
            errors.add("Veuillez renseigner la date de la commande fournisseur");
        }
        // Ajoutez d'autres validations si nécessaire
        return errors;
    }
}
