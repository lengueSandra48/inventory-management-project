package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.CommandeClientRequestDto;
import com.team48.gestiondestock.dto.CommandeClientResponseDto;
import com.team48.gestiondestock.dto.LigneCommandeClientRequestDto;
import com.team48.gestiondestock.dto.LigneCommandeClientResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Article;
import com.team48.gestiondestock.model.Client;
import com.team48.gestiondestock.model.CommandeClient;
import com.team48.gestiondestock.model.LigneCommandeClient;
import com.team48.gestiondestock.service.CommandeClientService;
import com.team48.gestiondestock.validator.CommandeClientValidator;
import com.team48.gestiondestock.repository.*;
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
public class CommandeClientServiceImpl implements CommandeClientService {

    private final CommandeClientRepository commandeClientRepository;
    private final LigneCommandeClientRepository ligneCommandeClientRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,
                                     LigneCommandeClientRepository ligneCommandeClientRepository, ArticleRepository articleRepository, ClientRepository clientRepository, EntrepriseRepository entrepriseRepository) {
        this.commandeClientRepository = commandeClientRepository;
        this.ligneCommandeClientRepository= ligneCommandeClientRepository;
        this.articleRepository = articleRepository;
        this.clientRepository = clientRepository;
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    @Transactional
    public CommandeClientResponseDto save(CommandeClientRequestDto dto) {
        if (dto == null) {
            log.error("CommandeClient is null");
            throw new InvalidEntityException(
                    "La commande client ne peut pas être null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }

        List<String> errors = CommandeClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("CommandeClient not valid: {}", dto);
            throw new InvalidEntityException(
                    "La commande client n'est pas valide",
                    ErrorCodes.COMMANDE_CLIENT_NOT_VALID,
                    errors
            );
        }

        // Vérification de l'existence du client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun client avec l'ID " + dto.getClientId() + " n'a été trouvé dans la BDD"
                ));

        // Vérification de l'existence de l'entreprise
        if (dto.getEntrepriseId() != null) {
            entrepriseRepository.findById(dto.getEntrepriseId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune entreprise avec l'ID " + dto.getEntrepriseId() + " n'a été trouvé dans la BDD"
                    ));
        }

        // Conversion du DTO en entité et association du client
        CommandeClient commandeClient = CommandeClientRequestDto.toEntity(dto);
        commandeClient.setClient(client);

        // Sauvegarde
        CommandeClient savedCommandeClient = commandeClientRepository.save(commandeClient);

        return CommandeClientResponseDto.fromEntity(savedCommandeClient);
    }


    @Override
    public CommandeClientResponseDto findById(Integer id) {
        if (id == null) {
            log.error("CommandeClient ID is null");
            return null;
        }
        return commandeClientRepository.findById(id)
                .map(CommandeClientResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client avec l'ID " + id + " trouvée",
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public CommandeClientResponseDto findByCodeCommandeClient(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Commande client CODE is NULL");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
                .map(CommandeClientResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client avec le CODE '" + code + "' trouvée",
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<CommandeClientResponseDto> findAll() {
        return commandeClientRepository.findAll().stream()
                .map(CommandeClientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            log.error("CommandeClient ID is null");
            return;
        }
        commandeClientRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CommandeClientResponseDto update(Integer id, CommandeClientRequestDto dto) {
        if (dto == null || id == null) {
            log.error("CommandeClient ou son ID ne peut pas être null");
            throw new InvalidEntityException(
                    "La commande client ou son ID ne peut pas être null",
                    ErrorCodes.COMMANDE_CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // Validation du DTO
        List<String> errors = CommandeClientValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("CommandeClient not valid: {}", dto);
            throw new InvalidEntityException(
                    "La commande client n'est pas valide",
                    ErrorCodes.COMMANDE_CLIENT_NOT_VALID,
                    errors
            );
        }

        // Vérifier que la commande existe
        CommandeClient existing = commandeClientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client avec l'ID " + id + " trouvée",
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));

        // Vérifier que le client existe (si fourni, sinon conserver l'existant)
        Client client = existing.getClient(); // Par défaut, conserver le client existant
        if (dto.getClientId() != null) {
            client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucun client avec l'ID " + dto.getClientId() + " n'a été trouvé dans la BDD"
                    ));
        }

        // Vérifier l'entreprise si fournie
        if (dto.getEntrepriseId() != null) {
            entrepriseRepository.findById(dto.getEntrepriseId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune entreprise avec l'ID " + dto.getEntrepriseId() + " n'a été trouvé dans la BDD"
                    ));
        }

        // Conversion du DTO en entité
        CommandeClient toSave = CommandeClientRequestDto.toEntity(dto);

        // Conserver l'ID et les lignes de commande existantes
        toSave.setId(existing.getId());
        toSave.setClient(client);
        toSave.setLigneCommandeClients(existing.getLigneCommandeClients());

        // Sauvegarde
        CommandeClient saved = commandeClientRepository.save(toSave);

        return CommandeClientResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public CommandeClientResponseDto addLigne(Integer commandeId, LigneCommandeClientRequestDto ligneDto) {
        if (ligneDto == null) {
            throw new InvalidEntityException(
                    "LigneCommandeClientRequestDto ne peut pas être null",
                    ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // Vérifier que l'ID de commande du DTO correspond à celui du path
        if (ligneDto.getCommandeClientId() != null && !ligneDto.getCommandeClientId().equals(commandeId)) {
            throw new InvalidEntityException(
                    "L'ID de commande dans le DTO (" + ligneDto.getCommandeClientId() + ") ne correspond pas à l'ID du path (" + commandeId + ")",
                    ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }

        CommandeClient cmd = commandeClientRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande client non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));

        // Vérifier l'entreprise si fournie
        if (ligneDto.getEntrepriseId() != null) {
            entrepriseRepository.findById(ligneDto.getEntrepriseId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune entreprise avec l'ID " + ligneDto.getEntrepriseId() + " n'a été trouvé dans la BDD"
                    ));
        }

        // Charger l'article si spécifié
        Article article = null;
        if (ligneDto.getArticleId() != null) {
            article = articleRepository.findById(ligneDto.getArticleId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Article non trouvé pour ID " + ligneDto.getArticleId(),
                            ErrorCodes.ARTICLE_NOT_FOUND
                    ));
        }

        LigneCommandeClient ligne = LigneCommandeClientRequestDto.toEntity(ligneDto);
        ligne.setCommandeClient(cmd);
        ligne.setArticle(article);
        ligne.setEntreprise_id(cmd.getEntreprise_id());

        // Sauvegarder directement la ligne
        LigneCommandeClient savedLigne = ligneCommandeClientRepository.save(ligne);
        
        return CommandeClientResponseDto.fromEntity(cmd);
    }

    @Override
    @Transactional
    public CommandeClientResponseDto updateLigne(Integer commandeId, LigneCommandeClientRequestDto ligneDto) {
        if (ligneDto == null || ligneDto.getId() == null) {
            throw new InvalidEntityException(
                    "LigneCommandeClient doit avoir un ID valide pour mise à jour",
                    ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // Récupération de la commande
        CommandeClient cmd = commandeClientRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande client non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));

        // Recherche de la ligne à mettre à jour
        LigneCommandeClient ligne = cmd.getLigneCommandeClients().stream()
                .filter(l -> l.getId().equals(ligneDto.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "LigneCommandeClient non trouvée pour ID " + ligneDto.getId(),
                        ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND
                ));

        // Mise à jour des valeurs
        ligne.setQuantite(ligneDto.getQuantite());
        ligne.setPrixUnitaire(ligneDto.getPrixUnitaire());

        // (optionnel si ton DTO contient articleId)
        if (ligneDto.getArticleId() != null) {
            Article article = articleRepository.findById(ligneDto.getArticleId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Article non trouvé pour ID " + ligneDto.getArticleId(),
                            ErrorCodes.ARTICLE_NOT_FOUND
                    ));
            ligne.setArticle(article);
        }

        // Hibernate détecte les changements car 'ligne' est déjà managée par la persistence context
        CommandeClient saved = commandeClientRepository.save(cmd);

        return CommandeClientResponseDto.fromEntity(saved);
    }


    @Override
    @Transactional
    public CommandeClientResponseDto removeLigne(Integer commandeId, Integer ligneId) {
        CommandeClient cmd = commandeClientRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande client non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));

        boolean removed = cmd.getLigneCommandeClients().removeIf(l -> l.getId().equals(ligneId));
        if (!removed) {
            throw new EntityNotFoundException(
                    "LigneCommandeClient non trouvée pour ID " + ligneId,
                    ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND
            );
        }

        CommandeClient saved = commandeClientRepository.save(cmd);
        return CommandeClientResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneCommandeClientResponseDto> findAllLignesByCommandeId(Integer commandeId) {
        // Utiliser le repository pour charger avec les relations
        List<LigneCommandeClient> lignes = ligneCommandeClientRepository.findByCommandeClientIdWithArticle(commandeId);
        
        if (lignes.isEmpty()) {
            // Vérifier que la commande existe
            commandeClientRepository.findById(commandeId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Commande client non trouvée pour ID " + commandeId,
                            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                    ));
        }

        return lignes.stream()
                .map(LigneCommandeClientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommandeClientResponseDto removeAllLignes(Integer commandeId) {
        CommandeClient cmd = commandeClientRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Commande client non trouvée pour ID " + commandeId,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));

        cmd.getLigneCommandeClients().clear();
        CommandeClient saved = commandeClientRepository.save(cmd);
        return CommandeClientResponseDto.fromEntity(saved);
    }


}
