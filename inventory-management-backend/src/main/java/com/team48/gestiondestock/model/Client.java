package com.team48.gestiondestock.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
public class Client extends AbstractEntity {

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Embedded //champ composé capable d'etre utilisé plusieurs fois dans les autres entités
    private Adresse adresse;

    @Column(name = "photo")
    private String photo;

    @Column(name = "email")
    private String email;

    @Column(name = "numTel")
    private String numTel;

    @OneToMany(mappedBy = "client")
    public List<CommandeClient> commandeClients;

    //attribut ajouté dans toutes les classes/entités sauf entreprise et utilisateur pour faciliter la recherche des données
    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

}
