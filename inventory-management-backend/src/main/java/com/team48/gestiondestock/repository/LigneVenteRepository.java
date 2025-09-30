package com.team48.gestiondestock.repository;

import com.team48.gestiondestock.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Integer> {
    void deleteAllByVenteId(Integer id);
}
