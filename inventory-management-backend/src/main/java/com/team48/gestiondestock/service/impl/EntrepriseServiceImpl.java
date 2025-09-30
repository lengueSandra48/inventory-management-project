package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.AdresseRequestDto;
import com.team48.gestiondestock.dto.EntrepriseRequestDto;
import com.team48.gestiondestock.dto.EntrepriseResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Entreprise;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.service.EntrepriseService;
import com.team48.gestiondestock.validator.EntrepriseValidator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    @Transactional
    public EntrepriseResponseDto save(EntrepriseRequestDto dto) {
        if (dto == null) {
            log.error("Entreprise is null");
            throw new InvalidEntityException(
                    "L'entreprise ne peut pas être null",
                    ErrorCodes.ENTREPRISE_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // 1. Validation métier via le Validator
        List<String> errors = EntrepriseValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid: {}", dto);
            throw new InvalidEntityException(
                    "L'entreprise n'est pas valide",
                    ErrorCodes.ENTREPRISE_NOT_VALID,
                    errors
            );
        }

        // 2. (Optionnel) Vérification d'unicité du nom
        if (StringUtils.isNotBlank(dto.getNomEntreprise())) {
            entrepriseRepository.findByNomEntreprise(dto.getNomEntreprise().trim()).ifPresent(existing -> {
                throw new InvalidEntityException(
                        "Une entreprise avec ce nom existe déjà",
                        ErrorCodes.ENTREPRISE_NOT_VALID,
                        List.of("Nom en doublon : " + dto.getNomEntreprise())
                );
            });
        }

        // 3. Persistance de l'entité
        Entreprise entity = Entreprise.builder()
                .nomEntreprise(dto.getNomEntreprise())
                .description(dto.getDescription())
                .photo(dto.getPhoto())
                .email(dto.getEmail())
                .adresse(dto.getAdresse() != null ? AdresseRequestDto.toEntity(dto.getAdresse()) : null)
                .codeFiscal(dto.getCodeFiscal())
                .numTel(dto.getNumTel())
                .steWeb(dto.getSteWeb())
                //.creationDate(dto.getCreationDate())
                .build();
        Entreprise saved = entrepriseRepository.save(entity);

        // 4. Retour du DTO reconstruit (avec ID généré, etc.)
        return EntrepriseResponseDto.fromEntity(saved);
    }

    @Override
    public EntrepriseResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return null;
        }
        return entrepriseRepository.findById(id)
                .map(EntrepriseResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune entreprise avec l'ID " + id + " n'a été trouvée dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND
                ));
    }

    @Override
    public EntrepriseResponseDto findByNomEntreprise(String nom) {
        if (StringUtils.isBlank(nom)) {
            log.error("Entreprise NOM is null or blank");
            return null;
        }
        return entrepriseRepository.findByNomEntreprise(nom)
                .map(EntrepriseResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune entreprise avec le NOM \"" + nom + "\" n'a été trouvée dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND
                ));
    }

    @Override
    public List<EntrepriseResponseDto> findAll() {
        return entrepriseRepository.findAll().stream()
                .map(EntrepriseResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Entreprise ID is null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }

    @Override
    public EntrepriseResponseDto update(Integer id, EntrepriseRequestDto dto) {
        if (dto == null || id == null) {
            log.error("Entreprise ou son ID ne peut pas être null");
            throw new InvalidEntityException(
                    "L'entreprise ou son ID ne peut pas être null",
                    ErrorCodes.ENTREPRISE_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = EntrepriseValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid: {}", dto);
            throw new InvalidEntityException(
                    "L'entreprise n'est pas valide",
                    ErrorCodes.ENTREPRISE_NOT_VALID,
                    errors
            );
        }
        Entreprise existing = entrepriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune entreprise avec l'ID " + id + " n'a été trouvée dans la BDD",
                        ErrorCodes.ENTREPRISE_NOT_FOUND
                ));
        Entreprise toSave = Entreprise.builder()
                .id(existing.getId())
                .nomEntreprise(dto.getNomEntreprise())
                .description(dto.getDescription())
                .photo(dto.getPhoto())
                .email(dto.getEmail())
                .adresse(dto.getAdresse() != null ? AdresseRequestDto.toEntity(dto.getAdresse()) : null)
                .codeFiscal(dto.getCodeFiscal())
                .numTel(dto.getNumTel())
                .steWeb(dto.getSteWeb())
                //.creationDate(dto.getCreationDate())
                .build();
        Entreprise saved = entrepriseRepository.save(toSave);
        return EntrepriseResponseDto.fromEntity(saved);
    }
}






