package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.CategorieRequestDto;
import com.team48.gestiondestock.dto.CategorieResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Categorie;
import com.team48.gestiondestock.repository.CategorieRepository;
import com.team48.gestiondestock.service.CategorieService;
import com.team48.gestiondestock.validator.CategorieValidator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    @Transactional
    public CategorieResponseDto save(CategorieRequestDto dto) {
        // Log du contenu du DTO reçu
        System.out.println("DTO reçu dans save(CategorieRequestDto) : " + dto);
        if (dto == null) {
            log.error("Categorie is null");
            throw new InvalidEntityException(
                    "La categorie ne peut pas etre null",
                    ErrorCodes.CATEGORIE_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = CategorieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Categorie is not valid: {}", dto);
            throw new InvalidEntityException(
                    "La categorie n'est pas valide",
                    ErrorCodes.CATEGORIE_NOT_VALID,
                    errors
            );
        }
        if (!StringUtils.isBlank(dto.getCode())) {
            categorieRepository.findByCode(dto.getCode().trim())
                    .ifPresent(existing -> {
                        throw new InvalidEntityException(
                                "Une categorie avec ce code existe déjà",
                                ErrorCodes.CATEGORIE_NOT_VALID,
                                List.of("Doublon de code : " + dto.getCode())
                        );
                    });
        }
        Categorie entity = CategorieRequestDto.toEntity(dto);
        Categorie saved = categorieRepository.save(entity);
        return CategorieResponseDto.fromEntity(saved);
    }

    @Override
    public CategorieResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Categorie ID is null");
            return null;
        }
        Optional<Categorie> categorie = categorieRepository.findById(id);
        return categorie.map(CategorieResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune categorie avec l'ID " + id + " n'a ete trouvee dans la BDD",
                        ErrorCodes.CATEGORIE_NOT_FOUND
                ));
    }

    @Override
    public CategorieResponseDto findByCodeCategorie(String code) {
        if (StringUtils.isBlank(code)) {
            log.error("Categorie CODE is null or blank");
            return null;
        }
        return categorieRepository.findByCode(code)
                .map(CategorieResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune categorie avec le CODE " + code + " n'a ete trouvee dans la BDD",
                        ErrorCodes.CATEGORIE_NOT_FOUND
                ));
    }

    @Override
    public List<CategorieResponseDto> findAll() {
        return categorieRepository.findAll().stream()
                .map(CategorieResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Categorie ID is null");
            throw new InvalidEntityException(
                    "L'ID de la categorie ne peut pas etre null",
                    ErrorCodes.CATEGORIE_NOT_VALID,
                    Collections.emptyList()
            );
        }
        
        // Vérifier si la catégorie existe avant de la supprimer
        Optional<Categorie> existingCategorie = categorieRepository.findById(id);
        if (existingCategorie.isEmpty()) {
            log.error("Categorie with ID {} not found for deletion", id);
            throw new EntityNotFoundException(
                    "Aucune categorie avec l'ID " + id + " n'a ete trouvee dans la BDD",
                    ErrorCodes.CATEGORIE_NOT_FOUND
            );
        }
        
        categorieRepository.deleteById(id);
        log.info("Categorie with ID {} has been successfully deleted", id);
    }

    @Override
    public CategorieResponseDto update(Integer id, CategorieRequestDto dto) {
        if (dto == null || id == null) {
            log.error("Categorie ou son ID ne peut pas etre null");
            throw new InvalidEntityException(
                    "La categorie ou son ID ne peut pas etre null",
                    ErrorCodes.CATEGORIE_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = CategorieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Categorie is not valid: {}", dto);
            throw new InvalidEntityException(
                    "La categorie n'est pas valide",
                    ErrorCodes.CATEGORIE_NOT_VALID,
                    errors
            );
        }
        Optional<Categorie> existing = categorieRepository.findById(id);
        if (existing.isEmpty()) {
            log.error("Categorie with ID {} not found", id);
            throw new EntityNotFoundException(
                    "Aucune categorie avec l'ID " + id + " n'a ete trouvee dans la BDD",
                    ErrorCodes.CATEGORIE_NOT_FOUND
            );
        }
        Categorie toSave = CategorieRequestDto.toEntity(dto);
        toSave.setId(existing.get().getId());
        Categorie saved = categorieRepository.save(toSave);
        return CategorieResponseDto.fromEntity(saved);
    }
}
