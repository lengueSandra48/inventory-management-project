package com.team48.gestiondestock.repository;

import com.team48.gestiondestock.model.Roles;
import com.team48.gestiondestock.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByRoleName(Role roleName);
}
