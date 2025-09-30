import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-commande-fournisseur',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detail-commande-fournisseur.html',
  styleUrls: ['./detail-commande-fournisseur.css']
})
export class DetailCommandeFournisseur implements OnInit {
  commandeId!: number;
  commande: any = {};
  fournisseurInfo: any = { nom: '', prenom: '', email: '', numTel: '' };
  loading = true;
  errorMsg = '';

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly http: HttpClient
  ) {}

  ngOnInit(): void {
    this.commandeId = this.activatedRoute.snapshot.params['idCommandeFournisseur'];
    this.loadCommande();
  }

  loadCommande(): void {
    this.loading = true;
    this.http.get<any>(`http://localhost:8888/gestiondestock/api/v1/commandesfournisseurs/${this.commandeId}`)
      .subscribe({
        next: (data: any) => {
          this.commande = data;
          this.loadFournisseurInfo(data);
          this.loading = false;
          console.log('Commande fournisseur chargée:', this.commande);
        },
        error: (error: any) => {
          this.errorMsg = 'Erreur lors du chargement de la commande';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
  }

  loadFournisseurInfo(commande: any): void {
    console.log('=== DEBUG FOURNISSEUR INFO ===');
    console.log('Commande reçue:', commande);
    
    // Stratégie 1: Vérifier si le fournisseur est directement dans la commande
    if (commande.fournisseur) {
      console.log('✓ Fournisseur trouvé directement dans la commande:', commande.fournisseur);
      this.fournisseurInfo = commande.fournisseur;
      return;
    }
    
    // Stratégie 2: Récupérer toutes les commandes avec les infos fournisseur complètes
    console.log('→ Récupération des commandes fournisseurs complètes...');
    this.http.get<any[]>('http://localhost:8888/gestiondestock/api/v1/commandesfournisseurs').subscribe({
      next: (commandes: any[]) => {
        console.log('Toutes les commandes fournisseurs reçues:', commandes);
        const commandeComplete = commandes.find(cmd => cmd.id === this.commandeId);
        console.log('Commande trouvée:', commandeComplete);
        
        if (commandeComplete?.fournisseur) {
          console.log('✓ Fournisseur trouvé dans la liste complète:', commandeComplete.fournisseur);
          this.fournisseurInfo = {
            nom: commandeComplete.fournisseur.nom,
            prenom: commandeComplete.fournisseur.prenom,
            email: commandeComplete.fournisseur.email,
            numTel: commandeComplete.fournisseur.numTel || commandeComplete.fournisseur.telephone
          };
        } else if (commandeComplete?.fournisseurId) {
          // Stratégie 3: Récupérer le fournisseur par son ID
          console.log('→ Récupération fournisseur par ID:', commandeComplete.fournisseurId);
          this.http.get<any>(`http://localhost:8888/gestiondestock/api/v1/fournisseurs/${commandeComplete.fournisseurId}`)
            .subscribe({
              next: (fournisseur: any) => {
                console.log('✓ Fournisseur récupéré par API:', fournisseur);
                this.fournisseurInfo = {
                  nom: fournisseur.nom,
                  prenom: fournisseur.prenom,
                  email: fournisseur.email,
                  numTel: fournisseur.numTel || fournisseur.telephone
                };
              },
              error: (error: any) => {
                console.error('✗ Erreur récupération fournisseur:', error);
                this.fournisseurInfo = { nom: 'Erreur de chargement', prenom: '', email: '', numTel: '' };
              }
            });
        } else {
          console.log('✗ Aucune info fournisseur trouvée');
          this.fournisseurInfo = { nom: 'Information non disponible', prenom: '', email: '', numTel: '' };
        }
      },
      error: (error: any) => {
        console.error('✗ Erreur lors de la récupération des commandes:', error);
        this.fournisseurInfo = { nom: 'Erreur de chargement', prenom: '', email: '', numTel: '' };
      }
    });
    
    console.log('=== FIN DEBUG ===');
  }

  getNbArticles(): number {
    if (!this.commande.ligneCommandeFournisseurs) return 0;
    return this.commande.ligneCommandeFournisseurs.length;
  }

  getTotalCommande(): number {
    if (!this.commande.ligneCommandeFournisseurs) return 0;
    return this.commande.ligneCommandeFournisseurs.reduce((total: number, ligne: any) => {
      return total + (ligne.quantite * ligne.prixUnitaire);
    }, 0);
  }

  retourListe(): void {
    this.router.navigate(['/commandesfournisseur']);
  }

  modifierCommande(): void {
    this.router.navigate(['/nouvellecommandefrs', this.commandeId]);
  }

  supprimerCommande(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la commande "${this.commande.code}" ?`)) {
      this.http.delete(`http://localhost:8888/gestiondestock/api/v1/commandesfournisseurs/${this.commandeId}`)
        .subscribe({
          next: () => {
            console.log('Commande supprimée avec succès');
            this.router.navigate(['/commandesfournisseur']);
          },
          error: (error: any) => {
            console.error('Erreur lors de la suppression:', error);
            alert('Erreur lors de la suppression de la commande');
          }
        });
    }
  }
}
