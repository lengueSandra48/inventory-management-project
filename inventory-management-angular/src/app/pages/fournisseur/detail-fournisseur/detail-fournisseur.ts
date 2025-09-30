import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-fournisseur',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail-fournisseur.html',
  styleUrls: ['./detail-fournisseur.css']
})
export class DetailFournisseur implements OnInit {
  fournisseurId!: number;
  fournisseur: any = {};
  loading = true;
  errorMsg = '';

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly http: HttpClient
  ) {}

  ngOnInit(): void {
    this.fournisseurId = this.activatedRoute.snapshot.params['idFournisseur'];
    this.loadFournisseur();
  }

  loadFournisseur(): void {
    this.loading = true;
    this.http.get<any>(`http://localhost:8888/gestiondestock/api/v1/fournisseurs/${this.fournisseurId}`)
      .subscribe({
        next: (data: any) => {
          this.fournisseur = data;
          this.loading = false;
          console.log('Fournisseur chargé:', this.fournisseur);
        },
        error: (error: any) => {
          this.errorMsg = 'Erreur lors du chargement du fournisseur';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
  }

  retourListe(): void {
    this.router.navigate(['/fournisseurs']);
  }

  modifierFournisseur(): void {
    this.router.navigate(['/nouveaufournisseur', this.fournisseurId]);
  }

  supprimerFournisseur(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le fournisseur "${this.fournisseur.nom} ${this.fournisseur.prenom}" ?`)) {
      this.http.delete(`http://localhost:8888/gestiondestock/api/v1/fournisseurs/${this.fournisseurId}`)
        .subscribe({
          next: () => {
            console.log('Fournisseur supprimé avec succès');
            this.router.navigate(['/fournisseurs']);
          },
          error: (error: any) => {
            console.error('Erreur lors de la suppression:', error);
            alert('Erreur lors de la suppression du fournisseur');
          }
        });
    }
  }

  getPhotoUrl(): string {
    if (this.fournisseur.photo) {
      return `http://localhost:8888/gestiondestock/api/v1/files/${this.fournisseur.photo}`;
    }
    return 'assets/default-supplier.png';
  }
}
