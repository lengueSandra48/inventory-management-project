package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {
    private Integer id;
    private String codeArticle;
    private String designation;
    private BigDecimal prixUnitaire;
    private BigDecimal tauxTva;
    private BigDecimal prixUnitaireTtc;
    private String photo;
    private CategorieResponseDto categorie;
    private EntrepriseResponseDto entreprise;

    // Méthode utilitaire pour convertir une entité en DTO
    public static ArticleResponseDto fromEntity(Article article) {
        if (article == null) return null;
        return ArticleResponseDto.builder()
                .id(article.getId())
                .codeArticle(article.getCodeArticle())
                .designation(article.getDesignation())
                .prixUnitaire(article.getPrixUnitaire())
                .tauxTva(article.getTauxTva())
                .prixUnitaireTtc(article.getPrixUnitaireTtc())
                .photo(article.getPhoto())
                .categorie(article.getCategorie() != null ? CategorieResponseDto.fromEntity(article.getCategorie()) : null)
                .entreprise(article.getEntreprise() != null ? EntrepriseResponseDto.fromEntity(article.getEntreprise()) : null)
                .build();
    }
}