package com.team48.gestiondestock.repository;

import com.team48.gestiondestock.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
    Optional<Entreprise> findByNomEntreprise(String nomEntreprise);
}
