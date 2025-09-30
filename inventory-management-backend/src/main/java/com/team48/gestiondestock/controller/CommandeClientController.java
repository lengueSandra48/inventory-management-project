package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.CommandeClientApi;
import com.team48.gestiondestock.dto.CommandeClientRequestDto;
import com.team48.gestiondestock.dto.CommandeClientResponseDto;
import com.team48.gestiondestock.dto.LigneCommandeClientRequestDto;
import com.team48.gestiondestock.dto.LigneCommandeClientResponseDto;
import com.team48.gestiondestock.service.CommandeClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENTREPRISE', 'USER')")
@RestController
public class CommandeClientController implements CommandeClientApi {

    private final CommandeClientService commandeClientService;

    @Autowired
    public CommandeClientController(CommandeClientService commandeClientService) {
        this.commandeClientService = commandeClientService;
    }

    @Override
    public ResponseEntity<CommandeClientResponseDto> save(@RequestBody CommandeClientRequestDto commandeClientDto) {
        CommandeClientResponseDto savedCommande = commandeClientService.save(commandeClientDto);
        return ResponseEntity.ok(savedCommande);
    }

    @Override
    public ResponseEntity<CommandeClientResponseDto> update(
            @PathVariable("idCommandeClient") Integer idCommandeClient,
            @RequestBody CommandeClientRequestDto commandeClientDto) {
        CommandeClientResponseDto updatedCommande = commandeClientService.update(idCommandeClient, commandeClientDto);
        return ResponseEntity.ok(updatedCommande);
    }

    @Override
    public CommandeClientResponseDto findById(@PathVariable("idCommandeClient") Integer id) {
        return commandeClientService.findById(id);
    }

    @Override
    public CommandeClientResponseDto findByCodeCommandeClient(@PathVariable("codeCommandeClient") String codeCommandeClient) {
        return commandeClientService.findByCodeCommandeClient(codeCommandeClient);
    }

    @Override
    public List<CommandeClientResponseDto> findAll() {
        return commandeClientService.findAll();
    }

    @Override
    public void delete(@PathVariable("idCommandeClient") Integer id) {
        commandeClientService.delete(id);
    }

    @Operation(
            summary = "Ajouter une ligne à une commande client",
            description = "Cette méthode permet d’ajouter une nouvelle ligne (article, quantité, prix) à une commande client existante."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La commande client mise à jour avec la nouvelle ligne",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommandeClientResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides (ligne null ou mal formée)"),
            @ApiResponse(responseCode = "404", description = "Commande client introuvable pour l’ID fourni")
    })
    @PostMapping("/{commandeId}/lignes")
    public ResponseEntity<CommandeClientResponseDto> addLigneToCommande(
            @Parameter(description = "ID de la commande client à mettre à jour", required = true)
            @PathVariable Integer commandeId,

            @Parameter(description = "Les détails de la nouvelle ligne à ajouter", required = true)
            @RequestBody LigneCommandeClientRequestDto ligneDto
    ) {
        CommandeClientResponseDto response = commandeClientService.addLigne(commandeId, ligneDto);
        return ResponseEntity.ok(response);
    }


    // 🔄 Mettre à jour une ligne de commande
    @Operation(summary = "Mettre à jour une ligne d'une commande client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ligne mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande ou ligne non trouvée"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PutMapping("/{commandeId}/lignes/{ligneId}")
    public ResponseEntity<CommandeClientResponseDto> updateLigne(
            @PathVariable Integer commandeId,
            @PathVariable Integer ligneId,
            @RequestBody LigneCommandeClientRequestDto ligneDto) {
        // on s'assure que l'ID correspond bien
        ligneDto.setId(ligneId);
        return ResponseEntity.ok(commandeClientService.updateLigne(commandeId, ligneDto));
    }

    // ❌ Supprimer une ligne de commande
    @Operation(summary = "Supprimer une ligne d'une commande client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ligne supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Commande ou ligne non trouvée")
    })
    @DeleteMapping("/{commandeId}/lignes/{ligneId}")
    public ResponseEntity<CommandeClientResponseDto> removeLigne(
            @PathVariable Integer commandeId,
            @PathVariable Integer ligneId) {
        return ResponseEntity.ok(commandeClientService.removeLigne(commandeId, ligneId));
    }

    @Operation(
            summary = "Supprimer toutes les lignes d'une commande client",
            description = "Cette méthode permet de supprimer toutes les lignes associées à une commande client."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Toutes les lignes supprimées avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommandeClientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Commande client introuvable pour l'ID fourni")
    })
    @DeleteMapping("/{commandeId}/lignes")
    public ResponseEntity<CommandeClientResponseDto> removeAllLignes(
            @Parameter(description = "ID de la commande client", required = true)
            @PathVariable Integer commandeId
    ) {
        CommandeClientResponseDto response = commandeClientService.removeAllLignes(commandeId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister toutes les lignes d’une commande client",
            description = "Cette méthode permet de récupérer toutes les lignes (articles, quantités, prix) associées à une commande client donnée."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des lignes de commande client trouvée",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LigneCommandeClientResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "Commande client introuvable pour l’ID fourni")
    })
    @GetMapping("/{commandeId}/lignes")
    public ResponseEntity<List<LigneCommandeClientResponseDto>> getAllLignesByCommande(
            @Parameter(description = "ID de la commande client", required = true)
            @PathVariable Integer commandeId
    ) {
        List<LigneCommandeClientResponseDto> lignes = commandeClientService.findAllLignesByCommandeId(commandeId);
        return ResponseEntity.ok(lignes);
    }



}
