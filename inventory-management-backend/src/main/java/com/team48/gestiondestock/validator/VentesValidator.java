package com.team48.gestiondestock.validator;

import com.team48.gestiondestock.dto.VentesRequestDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VentesValidator {
    public static List<String> validate(VentesRequestDto ventesDto) {
        List<String> errors = new ArrayList<>();

        if (ventesDto == null) {
            errors.add("Veillez renseigner le code de la vente");
            errors.add("Veillez renseigner la date de la vente");
            errors.add("Veillez renseigner le commantaire de la vente");
            return errors;
        }

        if (!StringUtils.hasLength(ventesDto.getCode())){
            errors.add("Veillez renseigner le code de la vente");
        }
        if (ventesDto.getDateVente() == null){
            errors.add("Veillez renseigner la date de la vente");
        }
        if (!StringUtils.hasLength(ventesDto.getCommentaire())){
            errors.add("Veillez renseigner le commantaire de la vente");
        }

        return errors;
    }
}
