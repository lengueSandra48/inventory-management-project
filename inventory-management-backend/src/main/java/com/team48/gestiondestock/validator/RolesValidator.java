package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.RolesRequestDto;

import java.util.ArrayList;
import java.util.List;

public class RolesValidator {
    
    private RolesValidator() {
        // Constructeur privé pour empêcher l'instanciation
    }
    public static List<String> validate(RolesRequestDto rolesDto) {
        List<String> errors = new ArrayList<>();

        if (rolesDto == null) {
            errors.add("Le rôle ne peut pas être null");
            return errors;
        }

        if (rolesDto.getRoleName() == null || rolesDto.getRoleName().trim().isEmpty()) {
            errors.add("Veillez renseigner le nom du role");
        }

        // L'utilisateurId est optionnel - un rôle peut exister sans être assigné à un utilisateur
        // Pas de validation obligatoire pour utilisateurId

        return errors;
    }
}
