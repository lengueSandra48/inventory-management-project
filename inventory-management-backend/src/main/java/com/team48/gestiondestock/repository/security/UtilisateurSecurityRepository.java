package com.team48.gestiondestock.repository.security;

import com.team48.gestiondestock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurSecurityRepository extends JpaRepository<Utilisateur, Integer> {

    /**
     * Trouve un utilisateur par son nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à rechercher.
     * @return Un Optional contenant l'utilisateur s'il est trouvé, sinon vide.
     */
    Optional<Utilisateur> findByUsername(String username);

    /**
     * Trouve un utilisateur par son adresse email.
     *
     * @param email L'adresse email à rechercher.
     * @return Un Optional contenant l'utilisateur s'il est trouvé, sinon vide.
     */
    Optional<Utilisateur> findByEmail(String email);

    /**
     * Trouve un utilisateur par son nom d'utilisateur avec ses rôles chargés.
     *
     * @param username Le nom d'utilisateur à rechercher.
     * @return Un Optional contenant l'utilisateur avec ses rôles s'il est trouvé, sinon vide.
     */
    @Query("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<Utilisateur> findByUsernameWithRoles(@Param("username") String username);

    /**
     * Trouve un utilisateur par son adresse email avec ses rôles chargés.
     *
     * @param email L'adresse email à rechercher.
     * @return Un Optional contenant l'utilisateur avec ses rôles s'il est trouvé, sinon vide.
     */
    @Query("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Utilisateur> findByEmailWithRoles(@Param("email") String email);

    /**
     * Vérifie si un utilisateur existe par son nom d'utilisateur.
     *
     * @param username Le nom d'utilisateur à vérifier.
     * @return true si l'utilisateur existe, false sinon.
     */
    boolean existsByUsername(String username);

    /**
     * Vérifie si un utilisateur existe par son adresse email.
     *
     * @param email L'adresse email à vérifier.
     * @return true si l'utilisateur existe, false sinon.
     */
    boolean existsByEmail(String email);
} 