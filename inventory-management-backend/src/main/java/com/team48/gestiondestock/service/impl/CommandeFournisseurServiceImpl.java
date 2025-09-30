package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.CommandeFournisseurRequestDto;
import com.team48.gestiondestock.dto.CommandeFournisseurResponseDto;
import com.team48.gestiondestock.dto.LigneCommandeFournisseurRequestDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.CommandeFournisseur;
import com.team48.gestiondestock.model.LigneCommandeFournisseur;
import com.team48.gestiondestock.repository.ArticleRepository;
import com.team48.gestiondestock.repository.CommandeFournisseurRepository;
import com.team48.gestiondestock.repository.FournisseurRepository;
import com.team48.gestiondestock.repository.LigneCommandeFournisseurRepository;
import com.team48.gestiondestock.service.CommandeFournisseurService;
import com.team48.gestiondestock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeFournisseurRepository;
    private final LigneCommandeFournisseurRepository ligneFournisseurRepo;
    private final ArticleRepository articleRepository;
    private final FournisseurRepository fournisseurRepository;

    @Autowired
    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository,
                                          LigneCommandeFournisseurRepository ligneFournisseurRepo, ArticleRepository articleRepository, FournisseurRepository fournisseurRepository) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneFournisseurRepo = ligneFournisseurRepo;
        this.articleRepository = articleRepository;
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    @Transactional
    public CommandeFournisseurResponseDto save(CommandeFournisseurRequestDto dto) {
        if (dto == null) {
            log.error("CommandeFournisseur is null");
            throw new InvalidEntityException(
                    "La commande fournisseur ne peut pas être null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = CommandeFournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("CommandeFournisseur not valid: {}", dto);
            throw new InvalidEntityException(
                    "La commande fournisseur n'est pas valide",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID,
                    errors
            );
        }
        if (dto.getFournisseurId() == null || fournisseurRepository.findById(dto.getFournisseurId()).isEmpty()) {
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID " + dto.getFournisseurId() + " n'a été trouvé dans la BDD");
        }
        
        CommandeFournisseur commandeFournisseur = CommandeFournisseurRequestDto.toEntity(dto);
        // Set the fournisseur entity
        commandeFournisseur.setFournisseur(fournisseurRepository.findById(dto.getFournisseurId()).get());
        
        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(commandeFournisseur);
        return CommandeFournisseurResponseDto.fromEntity(savedCommande);
    }

    @Override
    public CommandeFournisseurResponseDto findById(Integer id) {
        if (id == null) {
            log.error("CommandeFournisseur ID is null");
            return null;
        }
        return commandeFournisseurRepository.findById(id)
                .map(CommandeFournisseurResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur avec l'ID " + id + " trouvée",
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public CommandeFournisseurResponseDto findByCodeCommandeFournisseur(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Commande fournisseur CODE is NULL");
            return null;
        }
        return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
                .map(CommandeFournisseurResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a été trouvée avec le code " + code,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeFournisseurResponseDto> findAll() {
        return commandeFournisseurRepository.findAll().stream()
                .map(CommandeFournisseurResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            log.error("Commande Fournisseur ID is null");
            return;
        }
        commandeFournisseurRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommandeFournisseurResponseDto update(Integer id, CommandeFournisseurRequestDto dto) {
        if (dto == null || id == null) {
            log.error("Commande Fournisseur ou son ID ne peut pas être null");
            throw new InvalidEntityException(
                    "La commande fournisseur ou son ID ne peut pas être null",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = CommandeFournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Commande Fournisseur not valid: {}", dto);
            throw new InvalidEntityException(
                    "La commande fournisseur n'est pas valide",
                    ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID,
                    errors
            );
        }
        CommandeFournisseur existing = commandeFournisseurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur avec l'ID " + id + " trouvée",
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
        CommandeFournisseur toSave = CommandeFournisseurRequestDto.toEntity(dto);
        toSave.setId(existing.getId());
        if (dto.getFournisseurId() != null) {
            toSave.setFournisseur(fournisseurRepository.findById(dto.getFournisseurId())
                .orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur avec l'ID " + dto.getFournisseurId() + " n'a été trouvé dans la BDD")));
        }
        CommandeFournisseur saved = commandeFournisseurRepository.save(toSave);
        return CommandeFournisseurResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public CommandeFournisseurResponseDto addLigne(Integer commandeId, LigneCommandeFournisseurRequestDto ligneDto) {
        CommandeFournisseur cmd = commandeFournisseurRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande fournisseur non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
        LigneCommandeFournisseur ligne = LigneCommandeFournisseurRequestDto.toEntity(ligneDto);
        ligne.setCommandeFournisseur(cmd);
        cmd.getLigneCommandeFournisseurs().add(ligne);
        CommandeFournisseur saved = commandeFournisseurRepository.save(cmd);
        return CommandeFournisseurResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public CommandeFournisseurResponseDto updateLigne(Integer commandeId, LigneCommandeFournisseurRequestDto ligneDto) {
        if (ligneDto == null) {
            throw new InvalidEntityException(
                    "LigneCommandeFournisseurRequestDto ne peut pas être null",
                    ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        CommandeFournisseur cmd = commandeFournisseurRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande fournisseur non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
        Integer ligneId = null;
        try { ligneId = (Integer) ligneDto.getClass().getMethod("getId").invoke(ligneDto); } catch (Exception ignored) {}
        if (ligneId == null) {
            throw new InvalidEntityException(
                    "LigneCommandeFournisseur doit avoir un ID pour mise à jour",
                    ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_VALID,
                    Collections.emptyList()
            );
        }
        LigneCommandeFournisseur ligne = null;
        for (LigneCommandeFournisseur l : cmd.getLigneCommandeFournisseurs()) {
            if (l.getId().equals(ligneId)) {
                ligne = l;
                break;
            }
        }
        if (ligne == null) {
            throw new EntityNotFoundException(
                    "LigneCommandeFournisseur non trouvée pour ID " + ligneId,
                    ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND
            );
        }
        ligne.setQuantite(ligneDto.getQuantite());
        ligne.setPrixUnitaire(ligneDto.getPrixUnitaire());
        CommandeFournisseur saved = commandeFournisseurRepository.save(cmd);
        return CommandeFournisseurResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public CommandeFournisseurResponseDto removeLigne(Integer commandeId, Integer ligneId) {
        CommandeFournisseur cmd = commandeFournisseurRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande fournisseur non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND
                ));
        cmd.getLigneCommandeFournisseurs().removeIf(l -> l.getId().equals(ligneId));
        CommandeFournisseur saved = commandeFournisseurRepository.save(cmd);
        return CommandeFournisseurResponseDto.fromEntity(saved);
    }
}

