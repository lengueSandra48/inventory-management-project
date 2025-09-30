import { Component, OnInit } from '@angular/core';
import {BouttonAction} from "../../../composants/boutton-action/boutton-action";
import {Pagination} from "../../../composants/pagination/pagination";
import {Router} from '@angular/router';
import {UtilisateurResponseDto} from '../../../../gs-api/src';
import {UtilisateurService} from '../../../services/utilisateur/utilisateur.service';
import {NgForOf, NgIf} from '@angular/common';
import {ModalConfirmation} from '../../../composants/modal-confirmation/modal-confirmation';

@Component({
  selector: 'app-page-utilisateur',
  imports: [
    BouttonAction,
    Pagination,
    NgForOf,
    NgIf,
    ModalConfirmation
  ],
  templateUrl: './page-utilisateur.html',
  styleUrl: './page-utilisateur.css'
})
export class PageUtilisateur implements OnInit {
  
  utilisateurs: UtilisateurResponseDto[] = [];
  isLoading = false;
  errorMessage = '';
  showDeleteModal = false;
  utilisateurToDelete: UtilisateurResponseDto | null = null;

  constructor(
    private readonly router: Router,
    private readonly utilisateurService: UtilisateurService
  ) {
  }

  ngOnInit(): void {
    this.loadUtilisateurs();
  }

  loadUtilisateurs(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.utilisateurService.getAllUtilisateurs().subscribe({
      next: (data: UtilisateurResponseDto[]) => {
        this.utilisateurs = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement des utilisateurs: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  nouvelUtilisateur(): void {
    this.router.navigate(['nouvelutilisateur']);
  }

  onUtilisateurDeleted(): void {
    this.loadUtilisateurs(); // Reload the list after deletion
  }

  modifierUtilisateur(id?: number): void {
    this.router.navigate(['nouvelutilisateur', id]);
  }

  supprimerUtilisateur(id?: number): void {
    if (id) {
      const utilisateur = this.utilisateurs.find(u => u.id === id);
      if (utilisateur) {
        this.utilisateurToDelete = utilisateur;
        this.showDeleteModal = true;
      }
    }
  }

  confirmerSuppression(): void {
    if (this.utilisateurToDelete?.id) {
      this.utilisateurService.deleteUtilisateur(this.utilisateurToDelete.id).subscribe({
        next: () => {
          this.loadUtilisateurs();
          this.fermerModal();
        },
        error: (error: any) => {
          this.errorMessage = 'Erreur lors de la suppression: ' + (error.error?.message || error.message);
          this.fermerModal();
        }
      });
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.utilisateurToDelete = null;
  }

  getDeleteMessage(): string {
    return `Êtes-vous sûr de vouloir supprimer l'utilisateur "${this.utilisateurToDelete?.nom || ''} ${this.utilisateurToDelete?.prenom || ''}" ?`;
  }

  voirDetails(utilisateur: UtilisateurResponseDto): void {
    this.router.navigate(['detailutilisateur', utilisateur.id]);
  }
}
