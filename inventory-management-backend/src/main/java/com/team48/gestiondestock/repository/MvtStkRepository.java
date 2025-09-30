package com.team48.gestiondestock.repository;

import com.team48.gestiondestock.model.MvtStk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MvtStkRepository extends JpaRepository<MvtStk, Integer> {

    @Query("SELECT m FROM MvtStk m LEFT JOIN FETCH m.article WHERE m.id = :id")
    Optional<MvtStk> findByIdWithArticle(@Param("id") Integer id);

    @Query("SELECT m FROM MvtStk m LEFT JOIN FETCH m.article")
    List<MvtStk> findAllWithArticle();
}
