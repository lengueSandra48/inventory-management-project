import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RolesService } from '../../../services/roles/roles.service';
import { RolesResponseDto } from '../../../../gs-api/src';
import { BouttonAction } from '../../../composants/boutton-action/boutton-action';
import { Pagination } from '../../../composants/pagination/pagination';
import { ModalConfirmation } from '../../../composants/modal-confirmation/modal-confirmation';

@Component({
  selector: 'app-page-roles',
  imports: [CommonModule, BouttonAction, Pagination, ModalConfirmation],
  templateUrl: './page-roles.html',
  styleUrl: './page-roles.css'
})
export class PageRoles implements OnInit {
  
  roles: RolesResponseDto[] = [];
  isLoading = false;
  errorMessage = '';
  showDeleteModal = false;
  roleToDelete: RolesResponseDto | null = null;

  constructor(
    private readonly router: Router,
    private readonly rolesService: RolesService
  ) {}

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.rolesService.getAllRoles().subscribe({
      next: (data: RolesResponseDto[]) => {
        this.roles = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des rôles: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  nouveauRole(): void {
    this.router.navigate(['nouveaurole']);
  }

  onRoleDeleted(): void {
    this.loadRoles();
  }

  deleteRole(id: number): void {
    this.rolesService.deleteRole(id).subscribe({
      next: () => {
        // Supprimer immédiatement de la liste locale pour un feedback instantané
        this.roles = this.roles.filter(role => role.id !== id);
        this.fermerModal();
        // Recharger la liste pour s'assurer de la synchronisation
        this.loadRoles();
      },
      error: (error) => {
        console.error('Erreur lors de la suppression:', error);
        this.errorMessage = 'Erreur lors de la suppression du rôle';
        this.fermerModal();
      }
    });
  }

  modifierRole(id?: number): void {
    this.router.navigate(['nouveaurole', id]);
  }

  supprimerRole(role: RolesResponseDto): void {
    this.roleToDelete = role;
    this.showDeleteModal = true;
  }

  confirmerSuppression(): void {
    if (this.roleToDelete?.id) {
      this.deleteRole(this.roleToDelete.id);
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.roleToDelete = null;
  }

  getDeleteMessage(): string {
    if (this.roleToDelete) {
      return `Êtes-vous sûr de vouloir supprimer le rôle "${this.roleToDelete.roleName}" ?`;
    }
    return 'Êtes-vous sûr de vouloir supprimer ce rôle ?';
  }

  voirDetails(role: RolesResponseDto): void {
    this.router.navigate(['detailrole', role.id]);
  }
}
