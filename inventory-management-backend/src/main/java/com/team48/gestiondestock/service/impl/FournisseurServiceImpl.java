package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.FournisseurRequestDto;
import com.team48.gestiondestock.dto.FournisseurResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Fournisseur;
import com.team48.gestiondestock.model.Entreprise;
import com.team48.gestiondestock.repository.FournisseurRepository;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.service.FournisseurService;
import com.team48.gestiondestock.validator.FournisseurValidator;
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
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository, EntrepriseRepository entrepriseRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    @Transactional
    public FournisseurResponseDto save(FournisseurRequestDto dto) {
        if (dto == null) {
            log.error("Fournisseur is null");
            throw new InvalidEntityException(
                    "Le fournisseur ne peut pas être null",
                    ErrorCodes.FOURNISSEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = FournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Fournisseur is not valid: {}", dto);
            throw new InvalidEntityException(
                    "Le fournisseur n'est pas valide",
                    ErrorCodes.FOURNISSEUR_NOT_VALID,
                    errors
            );
        }
        if (StringUtils.isNotBlank(dto.getNom())) {
            fournisseurRepository.findByNom(dto.getNom().trim())
                    .ifPresent(existing -> {
                        throw new InvalidEntityException(
                                "Un fournisseur avec ce nom existe déjà",
                                ErrorCodes.FOURNISSEUR_NOT_VALID,
                                List.of("Doublon de nom : " + dto.getNom())
                        );
                    });
        }
        Fournisseur entity = FournisseurRequestDto.toEntity(dto);
        // Associer l'entreprise si entrepriseId présent
        if (dto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(dto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException("Aucune entreprise avec l'ID " + dto.getEntrepriseId() + " n'a été trouvée dans la BDD", ErrorCodes.ENTREPRISE_NOT_FOUND));
            entity.setEntreprise(entreprise);
        }
        Fournisseur saved = fournisseurRepository.save(entity);
        return FournisseurResponseDto.fromEntity(saved);
    }

    @Override
    public FournisseurResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return null;
        }
        return fournisseurRepository.findById(id)
                .map(FournisseurResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public FournisseurResponseDto findByNomFournisseur(String nom) {
        if (StringUtils.isBlank(nom)) {
            log.error("Fournisseur NOM is null or blank");
            return null;
        }
        return fournisseurRepository.findByNom(nom)
                .map(FournisseurResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur avec le NOM \"" + nom + "\" n'a été trouvé dans la BDD",
                        ErrorCodes.FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public List<FournisseurResponseDto> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return;
        }
        fournisseurRepository.deleteById(id);
    }

    @Override
    public FournisseurResponseDto update(Integer id, FournisseurRequestDto dto) {
        if (dto == null || id == null) {
            log.error("Fournisseur ou son ID ne peut pas être null");
            throw new InvalidEntityException(
                    "Le fournisseur ou son ID ne peut pas être null",
                    ErrorCodes.FOURNISSEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = FournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Fournisseur is not valid: {}", dto);
            throw new InvalidEntityException(
                    "Le fournisseur n'est pas valide",
                    ErrorCodes.FOURNISSEUR_NOT_VALID,
                    errors
            );
        }
        Fournisseur existing = fournisseurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.FOURNISSEUR_NOT_FOUND
                ));
        Fournisseur toSave = FournisseurRequestDto.toEntity(dto);
        toSave.setId(existing.getId());
        // Associer l'entreprise si entrepriseId présent
        if (dto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(dto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException("Aucune entreprise avec l'ID " + dto.getEntrepriseId() + " n'a été trouvée dans la BDD", ErrorCodes.ENTREPRISE_NOT_FOUND));
            toSave.setEntreprise(entreprise);
        }
        Fournisseur saved = fournisseurRepository.save(toSave);
        return FournisseurResponseDto.fromEntity(saved);
    }
}

