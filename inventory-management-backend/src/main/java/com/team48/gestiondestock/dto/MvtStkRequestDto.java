package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.TypeMvtStk;
import com.team48.gestiondestock.model.MvtStk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvtStkRequestDto {
    private Instant dateMvt;
    private Integer quantite;
    private TypeMvtStk typeMvt;
    private Integer articleId;
    private Integer entrepriseId;

    // Méthode utilitaire pour convertir en entité
    public static MvtStk toEntity(MvtStkRequestDto dto) {
        if (dto == null) return null;
        MvtStk mvtStk = new MvtStk();
        mvtStk.setDateMvt(dto.getDateMvt());
        mvtStk.setQuantite(dto.getQuantite() != null ? BigDecimal.valueOf(dto.getQuantite()) : null);
        mvtStk.setTypeMvt(dto.getTypeMvt());
        mvtStk.setEntreprise_id(dto.getEntrepriseId());
        // L'association à l'article se fait dans le service à partir de l'id
        return mvtStk;
    }
} 