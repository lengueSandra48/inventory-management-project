package com.team48.gestiondestock.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private UtilisateurResponseDto user;
} 