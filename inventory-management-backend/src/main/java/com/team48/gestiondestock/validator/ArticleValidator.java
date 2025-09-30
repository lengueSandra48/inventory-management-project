package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.ArticleRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleValidator {

    public static List<String> validate(ArticleRequestDto articleDto){
        List<String> errors = new ArrayList<>();

        if (articleDto == null){
            errors.add("Veillez renseigner le code de l'article'");
            errors.add("Veillez renseigner la designation de l'article'");
            errors.add("Veillez renseigner le prix unitaire HT de l'article'");
            errors.add("Veillez renseigner le taux TVA de l'article'");
            errors.add("Veillez renseigner le prix unitaire TTC de l'article'");
            errors.add("Veillez selectionner une categorie");
            return errors;
        }

        if (!StringUtils.hasLength(articleDto.getCodeArticle())) {
            errors.add("Veillez renseigner le code de l'article'");
        }
        if (!StringUtils.hasLength(articleDto.getDesignation())) {
            errors.add("Veillez renseigner la designation de l'article'");
        }
        if (articleDto.getPrixUnitaire() == null) {
            errors.add("Veillez renseigner le prix unitaire HT de l'article'");
        }
        if (articleDto.getTauxTva() == null) {
            errors.add("Veillez renseigner le taux TVA de l'article'");
        }
        if (articleDto.getPrixUnitaireTtc() == null) {
            errors.add("Veillez renseigner le prix unitaire TTC de l'article'");
        }
        if (articleDto.getCategorieId() == null) {
            errors.add("Veillez selectionner une categorie");
        }

        return errors;
    }
}
