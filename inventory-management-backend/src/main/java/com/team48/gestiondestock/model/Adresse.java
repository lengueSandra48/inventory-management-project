package com.team48.gestiondestock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Data
@Builder // permet de créer un builder en utilisant le design patern 'builder'. C'est une classe qui permet de construire un objet en exposant des methodes qui contiennent le meme nom de l'attribut et renvoyant la meme methode  et avec une methode à la fin qui renvoi une methode appelé build qui va construire un objet(ex: La classe Entreprise avec toutes ses attributs).
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable //classe qu doit etre utilisée par plusieurs autres classes
public class Adresse{

    @Column(name = "adresse1")
    private String adresse1;

    @Column(name = "adresse2")
    private String adresse2;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "pays")
    private String pays;
}
