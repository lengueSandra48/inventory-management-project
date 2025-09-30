package com.team48.gestiondestock.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;
    private String email;
    private String password;
    private String nom;      // firstName
    private String prenom;   // lastName
    private String role;
    private Integer entrepriseId;
} 