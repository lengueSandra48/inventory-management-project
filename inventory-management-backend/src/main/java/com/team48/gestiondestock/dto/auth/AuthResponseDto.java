package com.team48.gestiondestock.dto.auth;

import com.team48.gestiondestock.dto.UtilisateurResponseDto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private UtilisateurResponseDto user;
} 