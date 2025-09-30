package com.team48.gestiondestock.controller;

import com.team48.gestiondestock.controller.api.CommandeFournisseurApi;
import com.team48.gestiondestock.dto.CommandeFournisseurRequestDto;
import com.team48.gestiondestock.dto.CommandeFournisseurResponseDto;
import com.team48.gestiondestock.service.CommandeFournisseurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ENTREPRISE')")
@RestController
public class CommandeFournisseurController implements CommandeFournisseurApi {

    private final CommandeFournisseurService commandeFournisseurService;

    @Autowired
    public CommandeFournisseurController(CommandeFournisseurService commandeFournisseurService) {
        this.commandeFournisseurService = commandeFournisseurService;
    }

    @Override
    public ResponseEntity<CommandeFournisseurResponseDto> save(@RequestBody CommandeFournisseurRequestDto commandeFournisseurDto) {
        
        
        CommandeFournisseurResponseDto savedCommande = commandeFournisseurService.save(commandeFournisseurDto);
        return ResponseEntity.ok(savedCommande);
    }

    @Override
    public CommandeFournisseurResponseDto findById(@PathVariable("idCommandeFournisseur") Integer id) {
        return commandeFournisseurService.findById(id);
    }

    @Override
    public CommandeFournisseurResponseDto findByCodeCommandeFournisseur(@PathVariable("codeCommandeFournisseur") String codeCommandeFournisseur) {
        return commandeFournisseurService.findByCodeCommandeFournisseur(codeCommandeFournisseur);
    }

    @Override
    public List<CommandeFournisseurResponseDto> findAll() {
        return commandeFournisseurService.findAll();
    }

    @Override
    public void delete(@PathVariable("idCommandeFournisseur") Integer id) {
        commandeFournisseurService.delete(id);
    }

    @Override
    public ResponseEntity<CommandeFournisseurResponseDto> update(@PathVariable("idCommandeFournisseur") Integer idCommandeFournisseur, @RequestBody CommandeFournisseurRequestDto commandeFournisseurDto) {
        CommandeFournisseurResponseDto updatedCommande = commandeFournisseurService.update(idCommandeFournisseur, commandeFournisseurDto);
        return ResponseEntity.ok(updatedCommande);
    }
} 