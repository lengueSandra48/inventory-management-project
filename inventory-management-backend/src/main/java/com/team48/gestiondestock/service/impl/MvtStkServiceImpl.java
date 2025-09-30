package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.MvtStkRequestDto;
import com.team48.gestiondestock.dto.MvtStkResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.MvtStk;
import com.team48.gestiondestock.repository.ArticleRepository;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.repository.MvtStkRepository;
import com.team48.gestiondestock.service.MvtStkService;
import com.team48.gestiondestock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

    private final MvtStkRepository mvtStkRepository;
    private final ArticleRepository articleRepository;
    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public MvtStkServiceImpl(MvtStkRepository mvtStkRepository, ArticleRepository articleRepository, EntrepriseRepository entrepriseRepository) {
        this.mvtStkRepository = mvtStkRepository;
        this.articleRepository = articleRepository;
        this.entrepriseRepository = entrepriseRepository;
    }

   

    @Override
    @Transactional
    public MvtStkResponseDto save(MvtStkRequestDto dto) {
        if (dto == null) {
            log.error("MvtStk is null");
            throw new InvalidEntityException(
                    "Le mouvement de stock ne peut pas être null",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    Collections.emptyList()
            );
        }
        // 1. Validation métier
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("MvtStk is not valid: {}", dto);
            throw new InvalidEntityException(
                    "Le mouvement de stock n'est pas valide",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    errors
            );
        }
        // 2. Vérification de l'article lié
        if (dto.getArticleId() == null) {
            throw new InvalidEntityException(
                    "L'article associé au mouvement ne peut pas être null",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    List.of("Article manquant")
            );
        }
        var article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun article avec l'ID " + dto.getArticleId() + " n'a été trouvé",
                        ErrorCodes.ARTICLE_NOT_FOUND
                ));
        // 3. Persistance du mouvement
        MvtStk entity = MvtStkRequestDto.toEntity(dto);
        entity.setArticle(article); // Associer l'article à l'entité
        MvtStk saved = mvtStkRepository.save(entity);
        // 4. Retour du DTO reconstruit avec le nom de l'entreprise
        return MvtStkResponseDto.fromEntity(saved);
    }

    @Override
    public MvtStkResponseDto findById(Integer id) {
        if (id == null) {
            log.error("MvtStk ID is null");
            return null;
        }
        return mvtStkRepository.findByIdWithArticle(id)
                .map(mvtStk -> MvtStkResponseDto.fromEntity(mvtStk))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun mouvement de stock avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.MVT_STK_NOT_FOUND
                ));
    }

    @Override
    public List<MvtStkResponseDto> findAll() {
        return mvtStkRepository.findAllWithArticle().stream()
                .map(mvtStk -> MvtStkResponseDto.fromEntity(mvtStk))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("MvtStk ID is null");
            return;
        }
        mvtStkRepository.deleteById(id);
    }

    @Override
    public MvtStkResponseDto update(Integer id, MvtStkRequestDto dto) {
        if (dto == null || id == null) {
            log.error("MvtStk or MvtStk ID is null");
            throw new InvalidEntityException(
                    "Le mouvement de stock ou son ID ne peut pas être null",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = MvtStkValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("MvtStk is not valid: {}", dto);
            throw new InvalidEntityException(
                    "Le mouvement de stock n'est pas valide",
                    ErrorCodes.MVT_STK_NOT_VALID,
                    errors
            );
        }
        MvtStk existing = mvtStkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun mouvement de stock avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.MVT_STK_NOT_FOUND
                ));
        // Vérifier et récupérer l'article si l'articleId est fourni
        var article = dto.getArticleId() != null ?
                articleRepository.findById(dto.getArticleId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Aucun article avec l'ID " + dto.getArticleId() + " n'a été trouvé",
                                ErrorCodes.ARTICLE_NOT_FOUND
                        )) : null;
        MvtStk toSave = MvtStkRequestDto.toEntity(dto);
        toSave.setId(existing.getId());
        if (article != null) {
            toSave.setArticle(article);
        }
        MvtStk saved = mvtStkRepository.save(toSave);
        return MvtStkResponseDto.fromEntity(saved);
    }
}

