package com.team48.gestiondestock;

import com.team48.gestiondestock.model.Roles;
import com.team48.gestiondestock.model.Utilisateur;
import com.team48.gestiondestock.model.security.Role;
import com.team48.gestiondestock.repository.RolesRepository;
import com.team48.gestiondestock.repository.security.UtilisateurSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiGestionDeStockApplication {

    private final UtilisateurSecurityRepository utilisateurSecurityRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ApiGestionDeStockApplication.class, args);
    }

    @Bean
    public CommandLineRunner createDefaultUsers() {
        return args -> {
            // Création automatique des utilisateurs par défaut si la table est vide
            if (utilisateurSecurityRepository.count() == 0) {
                
                // Créer un utilisateur admin
                Utilisateur adminUser = Utilisateur.builder()
                    .nom("Admin")
                    .prenom("System")
                    .email("admin@admin.com")
                    .username("admin")
                    .motDePasse(passwordEncoder.encode("password"))
                    .enabled(true)
                    .accountNonExpired(true)
                    .credentialsNonExpired(true)
                    .accountNonLocked(true)
                    .build();

                adminUser = utilisateurSecurityRepository.save(adminUser);

                // Créer un rôle admin pour l'utilisateur admin
                Roles adminRole = Roles.builder()
                    .roleName(Role.ADMIN)
                    .utilisateur(adminUser)
                    .entreprise_id(1)
                    .build();

                // Sauvegarder le rôle
                adminRole = rolesRepository.save(adminRole);
                adminUser.setRoles(Arrays.asList(adminRole));
                utilisateurSecurityRepository.save(adminUser);

                // Créer un utilisateur manager
                Utilisateur managerUser = Utilisateur.builder()
                    .nom("Manager")
                    .prenom("Stock")
                    .email("manager@manager.com")
                    .username("manager")
                    .motDePasse(passwordEncoder.encode("password"))
                    .enabled(true)
                    .accountNonExpired(true)
                    .credentialsNonExpired(true)
                    .accountNonLocked(true)
                    .build();

                managerUser = utilisateurSecurityRepository.save(managerUser);

                // Créer un rôle manager
                Roles managerRole = Roles.builder()
                    .roleName(Role.MANAGER)
                    .utilisateur(managerUser)
                    .entreprise_id(1)
                    .build();

                // Sauvegarder le rôle
                managerRole = rolesRepository.save(managerRole);
                managerUser.setRoles(Arrays.asList(managerRole));
                utilisateurSecurityRepository.save(managerUser);

                System.out.println(">>> Utilisateurs par défaut créés : admin, manager");
            } else {
                System.out.println(">>> Utilisateurs déjà existants, création par défaut ignorée.");
            }
        };
    }
}
