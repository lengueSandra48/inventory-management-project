package com.team48.gestiondestock.repository;

import com.team48.gestiondestock.model.LigneCommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {
    
    @Query("SELECT l FROM LigneCommandeClient l " +
           "LEFT JOIN FETCH l.article a " +
           "LEFT JOIN FETCH a.categorie " +
           "LEFT JOIN FETCH l.commandeClient c " +
           "WHERE l.commandeClient.id = :commandeId")
    List<LigneCommandeClient> findByCommandeClientIdWithArticle(@Param("commandeId") Integer commandeId);
}
