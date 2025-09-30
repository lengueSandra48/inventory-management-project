package com.team48.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team48.gestiondestock.model.CommandeClient;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeClientResponseDto {

    private Integer id;
    private String code;

    @NotNull(message = "La date de la commande client est obligatoire")
    @FutureOrPresent(message = "La date de la commande doit être aujourd'hui ou dans le futur")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateCommande;

    private Integer entrepriseId;

    @JsonIgnore
    private ClientResponseDto client;

    private List<LigneCommandeClientResponseDto> ligneCommandeClients;

    // Conversion depuis l'entité
    public static CommandeClientResponseDto fromEntity(CommandeClient commandeClient) {
        if (commandeClient == null) return null;

        LocalDate date = null;
        if (commandeClient.getDateCommande() != null) {
            date = commandeClient.getDateCommande()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        // On convertit toutes les lignes de commande
        List<LigneCommandeClientResponseDto> lignes = commandeClient.getLigneCommandeClients() != null ?
                commandeClient.getLigneCommandeClients().stream()
                        .map(LigneCommandeClientResponseDto::fromEntity)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        return CommandeClientResponseDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateCommande(date)
                .entrepriseId(commandeClient.getEntreprise_id())
                .ligneCommandeClients(lignes)
                .build();
    }
}
