package com.team48.gestiondestock.service;

import com.team48.gestiondestock.dto.RolesRequestDto;
import com.team48.gestiondestock.dto.RolesResponseDto;

import java.util.List;

public interface RolesService {
    RolesResponseDto save(RolesRequestDto rolesDto);

    RolesResponseDto findById(Integer id);

    RolesResponseDto findByRoleName(String roleName);

    List<RolesResponseDto> findAll();

    void delete(Integer id);

    RolesResponseDto update(Integer id, RolesRequestDto rolesDto);
}

 
