package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.RolesRequestDto;
import com.team48.gestiondestock.dto.RolesResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Roles;
import com.team48.gestiondestock.model.security.Role;
import com.team48.gestiondestock.repository.RolesRepository;
import com.team48.gestiondestock.repository.UtilisateurRepository;
import com.team48.gestiondestock.service.RolesService;
import com.team48.gestiondestock.validator.RolesValidator;
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
public class RolesServiceImpl implements RolesService {

    private final RolesRepository rolesRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public RolesServiceImpl(RolesRepository rolesRepository, UtilisateurRepository utilisateurRepository) {
        this.rolesRepository = rolesRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @Transactional
    public RolesResponseDto save(RolesRequestDto dto) {
        if (dto == null) {
            log.error("Roles is null");
            throw new InvalidEntityException(
                    "Le rôle ne peut pas être null",
                    ErrorCodes.ROLES_NOT_VALID,
                    Collections.emptyList()
            );
        }
        // 1. Validation métier
        List<String> errors = RolesValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Roles is not valid: {}", dto);
            throw new InvalidEntityException(
                    "Le rôle n'est pas valide",
                    ErrorCodes.ROLES_NOT_VALID,
                    errors
            );
        }
        // 2. Unicité du roleName - permettre les doublons pour différents utilisateurs
        if (StringUtils.isNotBlank(dto.getRoleName()) && dto.getUtilisateurId() != null && dto.getEntrepriseId() != null) {
            // Vérifier s'il existe déjà un rôle avec le même nom pour le même utilisateur et la même entreprise
            boolean duplicateExists = rolesRepository.findAll().stream()
                    .anyMatch(existingRole -> 
                        dto.getRoleName().trim().equals(existingRole.getRoleName().name()) &&
                        dto.getUtilisateurId().equals(existingRole.getUtilisateur() != null ? existingRole.getUtilisateur().getId() : null) &&
                        dto.getEntrepriseId().equals(existingRole.getEntreprise_id())
                    );
            
            if (duplicateExists) {
                throw new InvalidEntityException(
                        "Ce rôle existe déjà pour cet utilisateur dans cette entreprise",
                        ErrorCodes.ROLES_NOT_VALID,
                        List.of("Doublon : " + dto.getRoleName() + " pour l'utilisateur " + dto.getUtilisateurId() + " dans l'entreprise " + dto.getEntrepriseId())
                );
            }
        }
        // 3. Persistance de l'entité Roles
        Roles entity = RolesRequestDto.toEntity(dto);

        // Associer l'utilisateur si un ID est fourni
        if (dto.getUtilisateurId() != null) {
            utilisateurRepository.findById(dto.getUtilisateurId()).ifPresent(entity::setUtilisateur);
        }

        Roles saved = rolesRepository.save(entity);
        // 4. Retour du DTO reconstruit
        return RolesResponseDto.fromEntity(saved);
    }

    @Override
    public RolesResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Roles ID is null");
            return null;
        }
        return rolesRepository.findById(id)
                .map(RolesResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun rôle avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ROLES_NOT_FOUND
                ));
    }

    @Override
    public RolesResponseDto findByRoleName(String roleName) {
        if (StringUtils.isBlank(roleName)) {
            log.error("RoleName is null or blank");
            return null;
        }
        try {
            Role roleEnum = Role.valueOf(roleName.toUpperCase());
            return rolesRepository.findByRoleName(roleEnum)
                    .map(RolesResponseDto::fromEntity)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucun rôle avec le nom \"" + roleName + "\" n'a été trouvé dans la BDD",
                            ErrorCodes.ROLES_NOT_FOUND
                    ));
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(
                    "Le rôle \"" + roleName + "\" n'existe pas",
                    ErrorCodes.ROLES_NOT_FOUND
            );
        }
    }

    @Override
    public List<RolesResponseDto> findAll() {
        return rolesRepository.findAll().stream()
                .map(RolesResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Roles ID is null");
            throw new InvalidEntityException(
                    "L'ID du rôle ne peut pas être null",
                    ErrorCodes.ROLES_NOT_VALID,
                    Collections.emptyList()
            );
        }
        
        // Vérifier si le rôle existe avant de le supprimer
        rolesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun rôle avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ROLES_NOT_FOUND
                ));
        
        rolesRepository.deleteById(id);
        log.info("Role with ID {} has been successfully deleted", id);
    }

    @Override
    public RolesResponseDto update(Integer id, RolesRequestDto dto) {
        if (dto == null || id == null) {
            log.error("Roles or Roles ID is null");
            throw new InvalidEntityException(
                    "Le rôle ou son ID ne peut pas être null",
                    ErrorCodes.ROLES_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = RolesValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Roles is not valid: {}", dto);
            throw new InvalidEntityException(
                    "Le rôle n'est pas valide",
                    ErrorCodes.ROLES_NOT_VALID,
                    errors
            );
        }
        // Vérifier si le rôle existe et éviter les doublons lors de la mise à jour
        Roles existing = rolesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun rôle avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ROLES_NOT_FOUND
                ));
        
        // Vérifier l'unicité pour la mise à jour (même utilisateur + même entreprise + même rôle)
        if (StringUtils.isNotBlank(dto.getRoleName()) && dto.getUtilisateurId() != null && dto.getEntrepriseId() != null) {
            boolean duplicateExists = rolesRepository.findAll().stream()
                    .anyMatch(existingRole -> 
                        !existingRole.getId().equals(id) && // Exclure le rôle actuel
                        dto.getRoleName().trim().equals(existingRole.getRoleName().name()) &&
                        dto.getUtilisateurId().equals(existingRole.getUtilisateur() != null ? existingRole.getUtilisateur().getId() : null) &&
                        dto.getEntrepriseId().equals(existingRole.getEntreprise_id())
                    );
            
            if (duplicateExists) {
                throw new InvalidEntityException(
                        "Ce rôle existe déjà pour cet utilisateur dans cette entreprise",
                        ErrorCodes.ROLES_NOT_VALID,
                        List.of("Doublon : " + dto.getRoleName() + " pour l'utilisateur " + dto.getUtilisateurId() + " dans l'entreprise " + dto.getEntrepriseId())
                );
            }
        }
        Roles toSave = RolesRequestDto.toEntity(dto);
        toSave.setId(existing.getId());

        // Associer l'utilisateur si un ID est fourni
        if (dto.getUtilisateurId() != null) {
            utilisateurRepository.findById(dto.getUtilisateurId()).ifPresent(toSave::setUtilisateur);
        }

        Roles saved = rolesRepository.save(toSave);
        return RolesResponseDto.fromEntity(saved);
    }
}
