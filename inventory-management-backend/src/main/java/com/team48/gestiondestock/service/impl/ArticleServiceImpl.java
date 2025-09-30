// com/loic/gestiondestock/service/impl/ArticleServiceImpl.java
package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.ArticleRequestDto;
import com.team48.gestiondestock.dto.ArticleResponseDto;
import com.team48.gestiondestock.dto.CategorieResponseDto;
import com.team48.gestiondestock.dto.EntrepriseResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Article;
import com.team48.gestiondestock.model.Categorie;
import com.team48.gestiondestock.model.Entreprise;
import com.team48.gestiondestock.repository.ArticleRepository;
import com.team48.gestiondestock.repository.CategorieRepository;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.service.ArticleService;
import com.team48.gestiondestock.validator.ArticleValidator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //Indique que cette classe est un service
@Slf4j // permet de faire des logs
public class ArticleServiceImpl implements ArticleService {

    private final EntrepriseRepository entrepriseRepository;
    private final CategorieRepository categorieRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, EntrepriseRepository entrepriseRepository, CategorieRepository categorieRepository) {
        this.articleRepository = articleRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.categorieRepository = categorieRepository;
    }

    @Override
    @Transactional
    public ArticleResponseDto save(ArticleRequestDto articleDto) {
        List<String> errors = ArticleValidator.validate(articleDto);
        if (!errors.isEmpty()) {
            log.error("Article non valide: {}", articleDto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        // Validate and check if category exists
        if (articleDto.getCategorieId() == null) {
            throw new InvalidEntityException("Catégorie non valide", ErrorCodes.CATEGORY_NOT_VALID);
        }
        Categorie categorie = categorieRepository.findById(articleDto.getCategorieId())
                .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée avec l'ID: "
                        + articleDto.getCategorieId(), ErrorCodes.CATEGORY_NOT_FOUND));

        // Validate and check if entreprise exists
        if (articleDto.getEntrepriseId() == null) {
            throw new InvalidEntityException("Entreprise non valide", ErrorCodes.ENTREPRISE_NOT_VALID);
        }
        Entreprise entreprise = entrepriseRepository.findById(articleDto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException("Entreprise non trouvée avec l'ID: "
                        + articleDto.getEntrepriseId(), ErrorCodes.ENTREPRISE_NOT_FOUND));

        Article article = new Article();
        article.setCodeArticle(articleDto.getCodeArticle());
        article.setDesignation(articleDto.getDesignation());
        article.setPrixUnitaire(articleDto.getPrixUnitaire());
        article.setTauxTva(articleDto.getTauxTva());
        article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
        article.setPhoto(articleDto.getPhoto());
        article.setCategorie(categorie);
        article.setEntreprise(entreprise);

        Article saved = articleRepository.save(article);
        return mapToResponseDto(saved);
    }

    @Override
    public ArticleResponseDto findById(Integer id) {
        if(id == null) {
            log.error("Article ID is null");
            return null;
        }
        Optional<Article> article = articleRepository.findById(id);
        return article.map(this::mapToResponseDto).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec l'ID " + id + " n'a ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND
                )
        );
    }

    @Override
    public ArticleResponseDto findByCodeArticle(String codeArticle) {
        if(StringUtils.isBlank(codeArticle)) {
            log.error("Article CODE is null");
            return null;
        }
        Optional<Article> article = articleRepository.findArticleByCodeArticle(codeArticle);
        return article.map(this::mapToResponseDto).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun article avec le CODE " + codeArticle + " n'a ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND
                )
        );
    }

    @Override
    public List<ArticleResponseDto> findAll() {
        return articleRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return;
        }
        articleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ArticleResponseDto update(Integer id, ArticleRequestDto articleDto) {
        if (articleDto == null || id == null) {
            log.error("Article ou son ID est null pour la mise à jour");
            throw new InvalidEntityException("ID Article manquant", ErrorCodes.ARTICLE_NOT_VALID);
        }

        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Article non trouvé avec ID: " + id,
                        ErrorCodes.ARTICLE_NOT_FOUND
                ));

        List<String> errors = ArticleValidator.validate(articleDto);
        if (!errors.isEmpty()) {
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        // Validate and check if category exists for update
        if (articleDto.getCategorieId() != null) {
            Categorie categorie = categorieRepository.findById(articleDto.getCategorieId())
                    .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée avec l'ID: "
                            + articleDto.getCategorieId(), ErrorCodes.CATEGORY_NOT_FOUND));
            existingArticle.setCategorie(categorie);
        } else {
            throw new InvalidEntityException("Catégorie non valide pour la mise à jour", ErrorCodes.CATEGORY_NOT_VALID);
        }

        // Validate and check if entreprise exists for update
        if (articleDto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(articleDto.getEntrepriseId())
                    .orElseThrow(() -> new EntityNotFoundException("Entreprise non trouvée avec l'ID: "
                            + articleDto.getEntrepriseId(), ErrorCodes.ENTREPRISE_NOT_FOUND));
            existingArticle.setEntreprise(entreprise);
        } else {
            throw new InvalidEntityException("Entreprise non valide pour la mise à jour", ErrorCodes.ENTREPRISE_NOT_VALID);
        }

        existingArticle.setCodeArticle(articleDto.getCodeArticle());
        existingArticle.setDesignation(articleDto.getDesignation());
        existingArticle.setPrixUnitaire(articleDto.getPrixUnitaire());
        existingArticle.setTauxTva(articleDto.getTauxTva());
        existingArticle.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
        existingArticle.setPhoto(articleDto.getPhoto());

        Article updatedArticle = articleRepository.save(existingArticle);
        return mapToResponseDto(updatedArticle);
    }

    private ArticleResponseDto mapToResponseDto(Article article) {
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