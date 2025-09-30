package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.RolesApi;
import com.team48.gestiondestock.dto.RolesRequestDto;
import com.team48.gestiondestock.dto.RolesResponseDto;
import com.team48.gestiondestock.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
public class RolesController implements RolesApi {

    private final RolesService rolesService;

    @Autowired
    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @Override
    public ResponseEntity<RolesResponseDto> save(@RequestBody RolesRequestDto rolesDto) {
        RolesResponseDto savedRole = rolesService.save(rolesDto);
        return ResponseEntity.ok(savedRole);
    }

    @Override
    public RolesResponseDto findById(@PathVariable("idRole") Integer id) {
        return rolesService.findById(id);
    }

    @Override
    public List<RolesResponseDto> findAll() {
        return rolesService.findAll();
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("idRole") Integer id) {
        rolesService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @Override
    public ResponseEntity<RolesResponseDto> update(@PathVariable("idRole") Integer idRole, @RequestBody RolesRequestDto rolesDto) {
        RolesResponseDto updatedRole = rolesService.update(idRole, rolesDto);
        return ResponseEntity.ok(updatedRole);
    }
}
