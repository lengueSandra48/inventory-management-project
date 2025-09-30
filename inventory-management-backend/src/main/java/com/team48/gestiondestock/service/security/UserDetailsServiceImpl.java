package com.team48.gestiondestock.service.security;

import com.team48.gestiondestock.repository.security.UtilisateurSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurSecurityRepository utilisateurSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // On tente d'abord par email avec rôles, puis par username avec rôles
        return utilisateurSecurityRepository.findByEmailWithRoles(usernameOrEmail)
                .or(() -> utilisateurSecurityRepository.findByUsernameWithRoles(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec email ou username : " + usernameOrEmail));
    }
} 