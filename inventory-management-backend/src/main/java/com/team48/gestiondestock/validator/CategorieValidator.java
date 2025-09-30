package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.CategorieRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CategorieValidator {

    public static List<String> validate(CategorieRequestDto categorieDto){
        List<String> errors = new ArrayList<>();

        if (categorieDto == null || !StringUtils.hasLength(categorieDto.getCode())) {
            errors.add("Veillez renseigner le code de la categorie");
        }
        return errors;
    }
}