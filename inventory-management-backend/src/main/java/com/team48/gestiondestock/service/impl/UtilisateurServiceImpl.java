package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.UtilisateurRequestDto;
import com.team48.gestiondestock.dto.UtilisateurResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Utilisateur;
import com.team48.gestiondestock.model.Entreprise;
import com.team48.gestiondestock.repository.UtilisateurRepository;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.service.UtilisateurService;
import com.team48.gestiondestock.validator.UtilisateurValidator;
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
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, EntrepriseRepository entrepriseRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    @Transactional
    public UtilisateurResponseDto save(UtilisateurRequestDto dto) {
        if (dto == null) {
            log.error("Utilisateur is null");
            throw new InvalidEntityException(
                    "L'utilisateur ne peut pas être null",
                    ErrorCodes.UTILISATEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // 1. Validation métier
        List<String> errors = UtilisateurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Utilisateur is not valid: {}", dto);
            throw new InvalidEntityException(
                    "L'utilisateur n'est pas valide",
                    ErrorCodes.UTILISATEUR_NOT_VALID,
                    errors
            );
        }

        // 2. Unicité de l'email
        if (StringUtils.isNotBlank(dto.getEmail())) {
            utilisateurRepository.findByEmail(dto.getEmail().trim())
                    .ifPresent(existing -> {
                        throw new InvalidEntityException(
                                "Un utilisateur avec cet email existe déjà",
                                ErrorCodes.UTILISATEUR_NOT_VALID,
                                List.of("Doublon d'email : " + dto.getEmail())
                        );
                    });
        }

        // 3. Persistance de l'entité Utilisateur
        Utilisateur entity = UtilisateurRequestDto.toEntity(dto);
        // Associer l'entreprise si entrepriseId présent
        if (dto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(dto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Aucune entreprise avec l'ID " + dto.getEntrepriseId() + " n'a été trouvée",
                    ErrorCodes.ENTREPRISE_NOT_FOUND
                ));
            entity.setEntreprise(entreprise);
        }
        Utilisateur saved = utilisateurRepository.save(entity);

        // 4. Retour du DTO reconstruit
        return UtilisateurResponseDto.fromEntity(saved);
    }

    @Override
    public UtilisateurResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return null;
        }
        return utilisateurRepository.findById(id)
                .map(UtilisateurResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND
                ));
    }

    @Override
    public UtilisateurResponseDto findByEmailUser(String email) {
        if (StringUtils.isBlank(email)) {
            log.error("Utilisateur EMAIL is null or blank");
            return null;
        }
        return utilisateurRepository.findByEmail(email)
                .map(UtilisateurResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'EMAIL \"" + email + "\" n'a été trouvé dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND
                ));
    }

    @Override
    public List<UtilisateurResponseDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Utilisateur ID is null");
            return;
        }
        utilisateurRepository.deleteById(id);
    }

    @Override
    public UtilisateurResponseDto update(Integer id, UtilisateurRequestDto utilisateurDto) {
        if (utilisateurDto == null || id == null) {
            log.error("Utilisateur or Utilisateur ID is null");
            throw new InvalidEntityException(
                    "L'utilisateur ou son ID ne peut pas être null",
                    ErrorCodes.UTILISATEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = UtilisateurValidator.validate(utilisateurDto);
        if (!errors.isEmpty()) {
            log.error("Utilisateur is not valid: {}", utilisateurDto);
            throw new InvalidEntityException(
                    "L'utilisateur n'est pas valide",
                    ErrorCodes.UTILISATEUR_NOT_VALID,
                    errors
            );
        }
        Utilisateur existing = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun utilisateur avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.UTILISATEUR_NOT_FOUND
                ));
        Utilisateur toSave = UtilisateurRequestDto.toEntity(utilisateurDto);
        toSave.setId(existing.getId());
        // Associer l'entreprise si entrepriseId présent
        if (utilisateurDto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(utilisateurDto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Aucune entreprise avec l'ID " + utilisateurDto.getEntrepriseId() + " n'a été trouvée",
                    ErrorCodes.ENTREPRISE_NOT_FOUND
                ));
            toSave.setEntreprise(entreprise);
        }
        Utilisateur saved = utilisateurRepository.save(toSave);
        return UtilisateurResponseDto.fromEntity(saved);
    }
}

