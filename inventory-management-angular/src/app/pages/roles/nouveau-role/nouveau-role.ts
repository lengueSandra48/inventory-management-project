import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RolesService } from '../../../services/roles/roles.service';
import { UtilisateurService } from '../../../services/utilisateur/utilisateur.service';
import { EntrepriseService } from '../../../services/entreprise/entreprise';
import { RolesRequestDto, RolesResponseDto, UtilisateurResponseDto, EntrepriseResponseDto } from '../../../../gs-api/src';

@Component({
  selector: 'app-nouveau-role',
  imports: [FormsModule, CommonModule],
  templateUrl: './nouveau-role.html',
  styleUrl: './nouveau-role.css'
})
export class NouveauRole implements OnInit {

  role: RolesRequestDto = {
    roleName: '',
    utilisateurId: undefined,
    entrepriseId: undefined
  };

  utilisateurs: UtilisateurResponseDto[] = [];
  entreprises: EntrepriseResponseDto[] = [];
  errorMessage = '';
  successMessage = '';
  isLoading = false;
  isEditMode = false;
  roleId: number | null = null;

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly rolesService: RolesService,
    private readonly utilisateurService: UtilisateurService,
    private readonly entrepriseService: EntrepriseService
  ) {}

  ngOnInit(): void {
    this.loadUtilisateurs();
    this.loadEntreprises();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.roleId = +id;
      this.isEditMode = true;
      this.loadRole(this.roleId);
    }
  }

  loadUtilisateurs(): void {
    this.utilisateurService.getAllUtilisateurs().subscribe({
      next: (utilisateurs: UtilisateurResponseDto[]) => {
        this.utilisateurs = utilisateurs;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des utilisateurs:', error);
      }
    });
  }

  loadEntreprises(): void {
    this.entrepriseService.findAll().subscribe({
      next: (entreprises: EntrepriseResponseDto[]) => {
        this.entreprises = entreprises;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des entreprises:', error);
      }
    });
  }

  loadRole(id: number): void {
    this.isLoading = true;
    this.rolesService.getRoleById(id).subscribe({
      next: (role: RolesResponseDto) => {
        this.role = {
          roleName: role.roleName || '',
          utilisateurId: (role as any).utilisateurId || undefined,
          entrepriseId: (role as any).entrepriseId || undefined
        };
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement du rôle: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  getPageTitle(): string {
    return this.isEditMode ? 'Modifier le rôle' : 'Nouveau rôle';
  }

  save(): void {
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    if (this.isEditMode && this.roleId) {
      this.updateRole();
    } else {
      this.createRole();
    }
  }

  createRole(): void {
    this.rolesService.createRole(this.role).subscribe({
      next: (response: RolesResponseDto) => {
        this.successMessage = 'Rôle créé avec succès!';
        this.isLoading = false;
        setTimeout(() => {
          this.router.navigate(['roles']);
        }, 1500);
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de la création du rôle: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  updateRole(): void {
    if (!this.roleId) return;
    
    this.rolesService.updateRole(this.roleId, this.role).subscribe({
      next: (response: RolesResponseDto) => {
        this.successMessage = 'Rôle modifié avec succès!';
        this.isLoading = false;
        setTimeout(() => {
          this.router.navigate(['roles']);
        }, 1500);
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors de la modification du rôle: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  private validateForm(): boolean {
    this.errorMessage = '';
    
    if (!this.role.roleName || this.role.roleName.trim().length < 2) {
      this.errorMessage = 'Le nom du rôle est requis et doit contenir au moins 2 caractères';
      return false;
    }
    
    if (!this.role.utilisateurId) {
      this.errorMessage = 'La sélection d\'un utilisateur est requise';
      return false;
    }
    
    if (!this.role.entrepriseId) {
      this.errorMessage = 'La sélection d\'une entreprise est requise';
      return false;
    }
    
    return true;
  }

  cancel(): void {
    this.router.navigate(['roles']);
  }
}
