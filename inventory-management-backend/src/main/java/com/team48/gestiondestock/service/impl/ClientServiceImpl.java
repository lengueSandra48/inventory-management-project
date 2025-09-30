package com.team48.gestiondestock.service.impl;

import com.team48.gestiondestock.dto.ClientRequestDto;
import com.team48.gestiondestock.dto.ClientResponseDto;
import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.ErrorCodes;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.model.Client;
import com.team48.gestiondestock.model.Entreprise;
import com.team48.gestiondestock.repository.ClientRepository;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.service.ClientService;
import com.team48.gestiondestock.validator.ClientValidator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final EntrepriseRepository entrepriseRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, EntrepriseRepository entrepriseRepository) {
        this.clientRepository = clientRepository;
        this.entrepriseRepository = entrepriseRepository;
    }

    @Override
    public ClientResponseDto save(ClientRequestDto clientDto) {
        if (clientDto == null) {
            log.error("Client is null");
            throw new InvalidEntityException(
                    "Le client ne peut pas être null",
                    ErrorCodes.CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = ClientValidator.validate(clientDto);
        if (!errors.isEmpty()) {
            log.error("Client is not valid: {}", clientDto);
            throw new InvalidEntityException(
                    "Le client n'est pas valide",
                    ErrorCodes.CLIENT_NOT_VALID,
                    errors
            );
        }
        Client entity = ClientRequestDto.toEntity(clientDto);
        // Associer l'entreprise si entrepriseId présent
        if (clientDto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(clientDto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException("Aucune entreprise avec l'ID " + clientDto.getEntrepriseId() + " n'a été trouvée dans la BDD", ErrorCodes.ENTREPRISE_NOT_FOUND));
            entity.setEntreprise(entreprise);
        }
        Client saved = clientRepository.save(entity);
        return ClientResponseDto.fromEntity(saved);
    }

    @Override
    public ClientResponseDto findById(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return null;
        }
        return clientRepository.findById(id)
                .map(ClientResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun client avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND
                ));
    }

    @Override
    public ClientResponseDto findByNomClient(String nom) {
        if (StringUtils.isBlank(nom)) {
            log.error("Client NOM is null or blank");
            return null;
        }
        return clientRepository.findByNom(nom)
                .map(ClientResponseDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun client avec le NOM " + nom + " n'a été trouvé dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<ClientResponseDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Client ID is null");
            return;
        }
        clientRepository.deleteById(id);
    }

    @Override
    public ClientResponseDto update(Integer id, ClientRequestDto clientDto) {
        if (clientDto == null || id == null) {
            log.error("Client ou son ID ne peut pas être null");
            throw new InvalidEntityException(
                    "Le client ou son ID ne peut pas être null",
                    ErrorCodes.CLIENT_NOT_VALID,
                    Collections.emptyList()
            );
        }
        List<String> errors = ClientValidator.validate(clientDto);
        if (!errors.isEmpty()) {
            log.error("Client is not valid: {}", clientDto);
            throw new InvalidEntityException(
                    "Le client n'est pas valide",
                    ErrorCodes.CLIENT_NOT_VALID,
                    errors
            );
        }
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun client avec l'ID " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.CLIENT_NOT_FOUND
                ));
        Client toSave = ClientRequestDto.toEntity(clientDto);
        toSave.setId(existing.getId());
        // Associer l'entreprise si entrepriseId présent
        if (clientDto.getEntrepriseId() != null) {
            Entreprise entreprise = entrepriseRepository.findById(clientDto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException("Aucune entreprise avec l'ID " + clientDto.getEntrepriseId() + " n'a été trouvée dans la BDD", ErrorCodes.ENTREPRISE_NOT_FOUND));
            toSave.setEntreprise(entreprise);
        }
        Client saved = clientRepository.save(toSave);
        return ClientResponseDto.fromEntity(saved);
    }
}

