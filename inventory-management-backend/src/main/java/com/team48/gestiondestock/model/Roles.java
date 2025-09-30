package com.team48.gestiondestock.model;

import com.team48.gestiondestock.model.security.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "roles")
public class Roles extends AbstractEntity {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private Role roleName;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    //attribut ajouté dans toutes les classes/entités sauf entreprise et utilisateur pour faciliter la recherche des données
    @Column(name = "entreprise_id")
    private Integer entreprise_id;
}
