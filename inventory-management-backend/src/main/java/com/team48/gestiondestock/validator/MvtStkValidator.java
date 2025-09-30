package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.MvtStkRequestDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MvtStkValidator {

    public static List<String> validate(MvtStkRequestDto mvtStkDto) {
        List<String> errors = new ArrayList<>();
        
        log.debug("Validation MvtStk: dateMvt={}, quantite={}, typeMvt={}, articleId={}, entrepriseId={}", 
                mvtStkDto.getDateMvt(), mvtStkDto.getQuantite(), mvtStkDto.getTypeMvt(), 
                mvtStkDto.getArticleId(), mvtStkDto.getEntrepriseId());

        if (mvtStkDto.getDateMvt() == null) {
            errors.add("Veillez renseigner la date du mouvement de stock");
        }
        if (mvtStkDto.getArticleId() == null) {
            errors.add("Veillez renseigner l'identifiant de l'article du mouvement de stock");
        }
        if (mvtStkDto.getQuantite() == null) {
            errors.add("Veillez renseigner la quantité du mouvement de stock");
        }
        if (mvtStkDto.getTypeMvt() == null) {
            errors.add("Veillez renseigner le type du mouvement de stock");
        }

        return errors;
    }
}
