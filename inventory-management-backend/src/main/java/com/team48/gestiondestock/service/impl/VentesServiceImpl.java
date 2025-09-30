package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.VentesRequestDto;
import com.team48.gestiondestock.dto.VentesResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.CommandeClient;
import com.team48.gestiondestock.model.LigneVente;
import com.team48.gestiondestock.model.Ventes;
import com.team48.gestiondestock.repository.ArticleRepository;
import com.team48.gestiondestock.repository.CommandeClientRepository;
import com.team48.gestiondestock.repository.LigneVenteRepository;
import com.team48.gestiondestock.repository.VentesRepository;
import com.team48.gestiondestock.service.VentesService;
import com.team48.gestiondestock.validator.VentesValidator;
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
public class VentesServiceImpl implements VentesService {

    private final VentesRepository ventesRepository;
    private final ArticleRepository articleRepository;
    private final LigneVenteRepository ligneVenteRepository;
    private final CommandeClientRepository commandeClientRepository;

    @Autowired
    public VentesServiceImpl(VentesRepository ventesRepository,
                             ArticleRepository articleRepository,
                             LigneVenteRepository ligneVenteRepository, CommandeClientRepository commandeClientRepository) {
        this.ventesRepository = ventesRepository;
        this.articleRepository = articleRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.commandeClientRepository = commandeClientRepository;
    }

    @Override
    @Transactional
    public VentesResponseDto save(VentesRequestDto dto, Integer commandeId) {

        if (dto == null) {
            log.error("VentesRequestDto is null");
            throw new InvalidEntityException(
                    "La vente ne peut pas être null",
                    ErrorCodes.VENTES_NOT_VALID,
                    Collections.emptyList()
            );
        }

        if (commandeId == null) {
            log.error("Commande ID is null");
            throw new InvalidEntityException(
                    "La commande associée à la vente est obligatoire",
                    ErrorCodes.VENTES_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // 1️⃣ Récupérer la commande
        CommandeClient commande = commandeClientRepository.findById(commandeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande trouvée avec l'ID " + commandeId,
                        ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                ));

        // 2️⃣ Validation simple de la vente
        List<String> errors = VentesValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("VentesRequestDto non valide: {}", dto);
            throw new InvalidEntityException(
                    "La vente n'est pas valide",
                    ErrorCodes.VENTES_NOT_VALID,
                    errors
            );
        }

        // 3️⃣ Créer l'entité Ventes
        Ventes vente = VentesRequestDto.toEntity(dto);
        vente.setCommandeClient(commande);

        // 4️⃣ Copier les lignes de la commande vers la vente
        List<LigneVente> lignes = commande.getLigneCommandeClients().stream()
                .map(lc -> {
                    LigneVente lv = new LigneVente();
                    lv.setArticle(lc.getArticle());
                    lv.setQuantite(lc.getQuantite());
                    lv.setPrixUnitaire(lc.getPrixUnitaire());
                    lv.setEntreprise_id(dto.getEntrepriseId());
                    lv.setVente(vente);
                    return lv;
                }).collect(Collectors.toList());

        vente.setLigneVentes(lignes);

        // 5️⃣ Sauvegarder la vente (les lignes seront sauvegardées grâce au cascade si configuré)
        Ventes savedVente = ventesRepository.save(vente);

        // 6️⃣ Retourner le DTO
        return VentesResponseDto.fromEntity(savedVente);
    }

    @Override
    public VentesResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Ventes ID is null");
            return null;
        }
        return ventesRepository.findById(id)
                .map(VentesResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente avec l'ID " + id + " n'a été trouvée dans la BDD",
                        ErrorCodes.VENTES_NOT_FOUND
                ));
    }

    @Override
    public VentesResponseDto findByCodeVente(String code) {
        if (StringUtils.isBlank(code)) {
            log.error("Ventes CODE is null or blank");
            return null;
        }
        return ventesRepository.findVentesByCode(code.trim())
                .map(VentesResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente avec le CODE \"" + code + "\" n'a été trouvée dans la BDD",
                        ErrorCodes.VENTES_NOT_FOUND
                ));
    }

    @Override
    public List<VentesResponseDto> findAll() {
        return ventesRepository.findAll().stream()
                .map(VentesResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (id == null) {
            log.error("Ventes ID is null");
            return;
        }
        if (!ventesRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Aucune vente avec l'ID " + id + " n'a été trouvée dans la BDD",
                    ErrorCodes.VENTES_NOT_FOUND
            );
        }
        ligneVenteRepository.deleteAllByVenteId(id); // supprime d’abord les lignes associées
        ventesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public VentesResponseDto update(Integer id, VentesRequestDto dto) {
        log.info("Update vente - ID: {}, DTO: {}", id, dto);
        if (dto == null || id == null) {
            log.error("Ventes ou son ID ne peut pas être null");
            throw new InvalidEntityException(
                    "La vente ou son ID ne peut pas être null",
                    ErrorCodes.VENTES_NOT_VALID,
                    Collections.emptyList()
            );
        }

        // Vérification existence vente
        Ventes existing = ventesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune vente avec l'ID " + id + " n'a été trouvée dans la BDD",
                        ErrorCodes.VENTES_NOT_FOUND
                ));

        // Validation basique pour update
        log.info("Code reçu: '{}'", dto.getCode());
        if (dto.getCode() == null || dto.getCode().trim().isEmpty()) {
            log.error("Code invalide - null: {}, empty: {}", dto.getCode() == null, dto.getCode() != null && dto.getCode().trim().isEmpty());
            throw new InvalidEntityException(
                    "Le code de la vente est obligatoire",
                    ErrorCodes.VENTES_NOT_VALID,
                    List.of("Veillez renseigner le code de la vente")
            );
        }

        // Mise à jour des champs
        existing.setCode(dto.getCode());
        existing.setDateVente(dto.getDateVente());
        existing.setCommentaire(dto.getCommentaire());
        existing.setEntreprise_id(dto.getEntrepriseId());

        // Association de la commande si elle est fournie
        if (dto.getCommandeId() != null) {
            CommandeClient commande = commandeClientRepository.findById(dto.getCommandeId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune commande avec l'ID " + dto.getCommandeId() + " n'a été trouvée dans la BDD",
                            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                    ));
            existing.setCommandeClient(commande);
        }

        // Si commandeId est fourni, copier les lignes de la commande
        if (dto.getCommandeId() != null && !dto.getCommandeId().equals(existing.getCommandeClient().getId())) {
            // Supprimer les anciennes lignes
            ligneVenteRepository.deleteAllByVenteId(id);
            
            // Copier les lignes de la nouvelle commande
            CommandeClient nouvelleCommande = commandeClientRepository.findById(dto.getCommandeId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune commande avec l'ID " + dto.getCommandeId() + " n'a été trouvée dans la BDD",
                            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND
                    ));
            
            List<LigneVente> nouvelles = nouvelleCommande.getLigneCommandeClients().stream()
                    .map(lc -> {
                        LigneVente lv = new LigneVente();
                        lv.setArticle(lc.getArticle());
                        lv.setQuantite(lc.getQuantite());
                        lv.setPrixUnitaire(lc.getPrixUnitaire());
                        lv.setEntreprise_id(dto.getEntrepriseId());
                        lv.setVente(existing);
                        return lv;
                    }).collect(Collectors.toList());
            
            existing.setLigneVentes(nouvelles);
        }

        // Sauvegarde finale
        Ventes saved = ventesRepository.save(existing);

        // Re-fetch pour s'assurer d'avoir les données à jour
        Ventes refreshed = ventesRepository.findById(saved.getId())
                .orElse(saved);

        // Retour du DTO bien rempli
        return VentesResponseDto.fromEntity(refreshed);
    }


}
