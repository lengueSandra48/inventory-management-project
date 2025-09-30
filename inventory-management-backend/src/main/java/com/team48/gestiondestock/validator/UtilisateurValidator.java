package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.UtilisateurRequestDto;
import com.team48.gestiondestock.dto.AdresseRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.time.LocalDate;

public class UtilisateurValidator {

    public static List<String> validate(UtilisateurRequestDto utilisateurDto){
        List<String> errors = new ArrayList<>();

        if (utilisateurDto == null){
            errors.add("Veillez renseigner le nom d'utilisateur");
            errors.add("Veillez renseigner le prenom d'utilisateur");
            errors.add("Veillez renseigner l'email de d'utilisateur");
            errors.add("Veillez renseigner le mot de passe de l'utilisateur");
            errors.add("Veillez renseigner la date de naissance de d'utilisateur");
            errors.add("Veillez renseigner l'adresse d'utilisateur");
            return errors;
        }

        if (!StringUtils.hasLength(utilisateurDto.getNom())) {
            errors.add("Veillez renseigner le nom d'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getPrenom())) {
            errors.add("Veillez renseigner le prenom d'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getEmail())) {
            errors.add("Veuillez renseigner l'email de l'utilisateur");
        } else {
            // Simple RFC 5322 compliant-ish email regex
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            if (!emailPattern.matcher(utilisateurDto.getEmail().trim()).matches()) {
                errors.add("Format d'email invalide");
            }
        }

        if (!StringUtils.hasLength(utilisateurDto.getMotDePasse())) {
            errors.add("Veuillez renseigner le mot de passe de l'utilisateur");
        } else {
            String pwd = utilisateurDto.getMotDePasse();
            if (pwd.length() < 8) {
                errors.add("Le mot de passe doit contenir au moins 8 caractères");
            }
            if (!pwd.matches(".*[A-Za-z].*")) {
                errors.add("Le mot de passe doit contenir au moins une lettre");
            }
            if (!pwd.matches(".*[0-9].*")) {
                errors.add("Le mot de passe doit contenir au moins un chiffre");
            }
        }

        if (utilisateurDto.getDateDeNaissance() == null) {
            errors.add("Veuillez renseigner la date de naissance de l'utilisateur");
        } else {
            LocalDate today = LocalDate.now();
            if (utilisateurDto.getDateDeNaissance().isAfter(today)) {
                errors.add("La date de naissance ne peut pas être dans le futur");
            }
        }

        if (utilisateurDto.getEntrepriseId() == null) {
            errors.add("Veuillez renseigner l'identifiant de l'entreprise");
        } else if (utilisateurDto.getEntrepriseId() <= 0) {
            errors.add("L'identifiant de l'entreprise doit être un entier positif");
        }
        if (utilisateurDto.getAdresse() == null) {
            errors.add("Veillez renseigner l'adresse d'utilisateur");
        }else {
            AdresseRequestDto adresse = utilisateurDto.getAdresse();
            if (!StringUtils.hasLength(adresse.getAdresse1())) {
                errors.add("Le champ 'Adresse 1' est obligatoire");
            }
            if (!StringUtils.hasLength(adresse.getVille())) {
                errors.add("Le champ 'Ville' est obligatoire");
            }
            if (!StringUtils.hasLength(adresse.getCodePostal())) {
                errors.add("Le champ 'Code postal' est obligatoire");
            }
            if (!StringUtils.hasLength(adresse.getPays())) {
                errors.add("Le champ 'Pays' est obligatoire");
            }
        }

        return errors;
    }
}
