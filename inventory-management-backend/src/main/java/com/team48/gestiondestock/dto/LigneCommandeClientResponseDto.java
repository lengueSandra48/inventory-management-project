package com.team48.gestiondestock.dto;

import com.team48.gestiondestock.model.Article;
import com.team48.gestiondestock.model.LigneCommandeClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeClientResponseDto {
    private Integer id;
    private CommandeClientResponseDto commandeClient;
    private ArticleResponseDto article;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;
    private Integer entrepriseId;

    public static LigneCommandeClientResponseDto fromEntity(LigneCommandeClient ligneCommandeClient) {
        if (ligneCommandeClient == null) return null;

        ArticleResponseDto articleDto = null;
        if (ligneCommandeClient.getArticle() != null) {
            Article article = ligneCommandeClient.getArticle();
            articleDto = ArticleResponseDto.builder()
                    .id(article.getId())
                    .codeArticle(article.getCodeArticle()) // Utiliser codeArticle au lieu de code
                    .designation(article.getDesignation())
                    .prixUnitaire(article.getPrixUnitaire())
                    .tauxTva(article.getTauxTva())
                    .prixUnitaireTtc(article.getPrixUnitaireTtc())
                    .photo(article.getPhoto())
                    .categorie(
                            article.getCategorie() != null ? CategorieResponseDto.fromEntity(article.getCategorie()) : null
                    )
                    .build();
        }

        CommandeClientResponseDto commandeDto = null;
        if (ligneCommandeClient.getCommandeClient() != null) {
            commandeDto = CommandeClientResponseDto.builder()
                    .id(ligneCommandeClient.getCommandeClient().getId())
                    .code(ligneCommandeClient.getCommandeClient().getCode())
                    .dateCommande(ligneCommandeClient.getCommandeClient().getDateCommande() != null ? 
                        ligneCommandeClient.getCommandeClient().getDateCommande().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null)
                    .entrepriseId(ligneCommandeClient.getCommandeClient().getEntreprise_id())
                    .ligneCommandeClients(null) // Éviter la référence circulaire
                    .build();
        }

        return LigneCommandeClientResponseDto.builder()
                .id(ligneCommandeClient.getId())
                .commandeClient(commandeDto)
                .article(articleDto)
                .quantite(ligneCommandeClient.getQuantite())
                .prixUnitaire(ligneCommandeClient.getPrixUnitaire())
                .entrepriseId(ligneCommandeClient.getEntreprise_id())
                .build();
    }


}