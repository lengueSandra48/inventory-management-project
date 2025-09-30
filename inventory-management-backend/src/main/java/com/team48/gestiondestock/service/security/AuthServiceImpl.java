package com.team48.gestiondestock.service.security;

import com.team48.gestiondestock.dto.UtilisateurResponseDto;
import com.team48.gestiondestock.dto.AuthResponseDto;
import com.team48.gestiondestock.dto.LoginRequestDto;
import com.team48.gestiondestock.dto.RegisterRequestDto;
import com.team48.gestiondestock.model.Utilisateur;
import com.team48.gestiondestock.model.Roles;
import com.team48.gestiondestock.model.security.Role;
import com.team48.gestiondestock.repository.security.UtilisateurSecurityRepository;
import com.team48.gestiondestock.service.AuthService;
import com.team48.gestiondestock.repository.RolesRepository;
import com.team48.gestiondestock.repository.EntrepriseRepository;
import com.team48.gestiondestock.exception.BadCredentialsException;
import com.team48.gestiondestock.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UtilisateurSecurityRepository utilisateurSecurityRepository;
    private final RolesRepository rolesRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {

        // Vérifier si l'utilisateur existe déjà
        if (utilisateurSecurityRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        if (utilisateurSecurityRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Nom d'utilisateur déjà utilisé");
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setUsername(request.getUsername());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        utilisateur.setEnabled(true);
        utilisateur.setAccountNonExpired(true);
        utilisateur.setCredentialsNonExpired(true);
        utilisateur.setAccountNonLocked(true);
        if (request.getEntrepriseId() != null) {
            utilisateur.setEntreprise(entrepriseRepository.findById(request.getEntrepriseId()).orElse(null));
        }
        utilisateur = utilisateurSecurityRepository.save(utilisateur);
        // Associer le rôle
        Role role = Role.valueOf(request.getRole());
        Roles userRole = new Roles();
        userRole.setRoleName(role);
        userRole.setUtilisateur(utilisateur);
        userRole.setEntreprise_id(request.getEntrepriseId());
        rolesRepository.save(userRole);
        utilisateur.getRoles().add(userRole);
        // Générer le token JWT
        String token = jwtService.generateToken(utilisateur);
        UtilisateurResponseDto userDto = UtilisateurResponseDto.fromEntity(utilisateur);
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUser(userDto);
        return response;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Identifiants invalides", ErrorCodes.INVALID_CREDENTIALS);
        }

        Utilisateur utilisateur = utilisateurSecurityRepository.findByEmailWithRoles(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        String token = jwtService.generateToken(utilisateur);
        UtilisateurResponseDto userDto = UtilisateurResponseDto.fromEntity(utilisateur);
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);
        response.setUser(userDto);
        return response;
    }
} 