package com.team48.gestiondestock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team48.gestiondestock.model.CommandeClient;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeClientRequestDto {

    @NotBlank(message = "Le code de la commande client est obligatoire")
    private String code;

    @NotNull(message = "La date de la commande client est obligatoire")
    @FutureOrPresent(message = "La date de la commande doit être aujourd'hui ou dans le futur")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateCommande;

    private Integer entrepriseId;
    private Integer clientId;

    // Conversion vers l'entité
    public static CommandeClient toEntity(CommandeClientRequestDto dto) {
        if (dto == null) return null;
        CommandeClient commandeClient = new CommandeClient();
        commandeClient.setCode(dto.getCode());

        // Conversion LocalDate -> Instant
        if (dto.getDateCommande() != null) {
            commandeClient.setDateCommande(dto.getDateCommande()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
        }

        commandeClient.setEntreprise_id(dto.getEntrepriseId());
        return commandeClient;
    }
}
