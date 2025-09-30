import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UtilisateurResponseDto } from '../../../gs-api/src';
import { UtilisateurService } from '../../services/utilisateur/utilisateur.service';

@Component({
  selector: 'app-detail-utilisateur',
  imports: [CommonModule],
  templateUrl: './detail-utilisateur.html',
  styleUrl: './detail-utilisateur.css'
})
export class DetailUtilisateur implements OnInit {
  
  utilisateur: UtilisateurResponseDto | null = null;
  isLoading = false;
  errorMsg = '';
  utilisateurId: number = 0;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly utilisateurService: UtilisateurService
  ) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];
    if (id) {
      this.utilisateurId = +id;
      this.loadUtilisateur();
    }
  }

  loadUtilisateur(): void {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.utilisateurService.getUtilisateurById(this.utilisateurId).subscribe({
      next: (data: UtilisateurResponseDto) => {
        this.utilisateur = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMsg = 'Erreur lors du chargement de l\'utilisateur: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  retourALaListe(): void {
    this.router.navigate(['utilisateurs']);
  }

  modifier(): void {
    this.router.navigate(['nouvelutilisateur', this.utilisateurId]);
  }

  supprimer(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'utilisateur "${this.utilisateur?.nom} ${this.utilisateur?.prenom}" ?`)) {
      this.utilisateurService.deleteUtilisateur(this.utilisateurId).subscribe({
        next: () => {
          this.router.navigate(['utilisateurs']);
        },
        error: (error) => {
          this.errorMsg = 'Erreur lors de la suppression: ' + (error.error?.message || error.message);
        }
      });
    }
  }
}
