import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
// Utilisation d'un type générique pour éviter les erreurs d'import
interface CommandeClientResponseDto {
  id?: number;
  code?: string;
  dateCommande?: string;
  etatCommande?: string;
  client?: any;
  clientId?: number;
  ligneCommandeClients?: any[];
  entrepriseId?: number;
}
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-detail-commande-client',
  imports: [CommonModule],
  templateUrl: './detail-commande-client.html',
  styleUrl: './detail-commande-client.css'
})
export class DetailCommandeClient implements OnInit {
  commande: CommandeClientResponseDto = {};
  commandeId: number = 0;
  errorMsg: string = '';
  loading: boolean = false;
  clientInfo: any = {};

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
    private readonly http: HttpClient
  ) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    if (id) {
      this.commandeId = parseInt(id, 10);
      this.loadCommande();
    }
  }

  loadCommande(): void {
    this.loading = true;
    this.errorMsg = '';
    
    this.http.get<CommandeClientResponseDto>(`http://localhost:8888/gestiondestock/api/v1/commandesclients/${this.commandeId}`)
      .subscribe({
        next: (commande: CommandeClientResponseDto) => {
          console.log('Commande reçue:', commande);
          this.commande = commande;
          this.loadClientInfo(commande);
          this.loading = false;
        },
        error: (error: any) => {
          this.errorMsg = 'Erreur lors du chargement de la commande';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
  }

  loadClientInfo(commande: CommandeClientResponseDto): void {
    
    
    // Stratégie 1: Vérifier si le client est directement dans la commande
    const commandeAny = commande as any;
    if (commandeAny.client) {
      console.log('✓ Client trouvé directement dans la commande:', commandeAny.client);
      this.clientInfo = commandeAny.client;
      return;
    }
    
    // Stratégie 2: Récupérer toutes les commandes avec les infos client complètes
    console.log('→ Récupération des commandes complètes...');
    this.http.get<any[]>('http://localhost:8888/gestiondestock/api/v1/commandesclients').subscribe({
      next: (commandes: any[]) => {
        console.log('Toutes les commandes reçues:', commandes);
        const commandeComplete = commandes.find(cmd => cmd.id === this.commandeId);
        console.log('Commande trouvée:', commandeComplete);
        
        if (commandeComplete?.client) {
          console.log('✓ Client trouvé dans la liste complète:', commandeComplete.client);
          this.clientInfo = {
            nom: commandeComplete.client.nom,
            prenom: commandeComplete.client.prenom,
            email: commandeComplete.client.email,
            numTel: commandeComplete.client.numTel || commandeComplete.client.telephone
          };
        } else if (commandeComplete?.clientId) {
          // Stratégie 3: Récupérer le client par son ID
          console.log('→ Récupération client par ID:', commandeComplete.clientId);
          this.http.get<any>(`http://localhost:8888/gestiondestock/api/v1/clients/${commandeComplete.clientId}`)
            .subscribe({
              next: (client: any) => {
                console.log('✓ Client récupéré par API:', client);
                this.clientInfo = {
                  nom: client.nom,
                  prenom: client.prenom,
                  email: client.email,
                  numTel: client.numTel || client.telephone
                };
              },
              error: (error: any) => {
                console.error('✗ Erreur récupération client:', error);
                this.clientInfo = { nom: 'Erreur de chargement', prenom: '', email: '', numTel: '' };
              }
            });
        } else {
          console.log('✗ Aucune info client trouvée');
          this.clientInfo = { nom: 'Information non disponible', prenom: '', email: '', numTel: '' };
        }
      },
      error: (error: any) => {
        console.error('✗ Erreur lors de la récupération des commandes:', error);
        this.clientInfo = { nom: 'Erreur de chargement', prenom: '', email: '', numTel: '' };
      }
    });
    
    console.log('=== FIN DEBUG ===');
  }

  retourListe(): void {
    this.router.navigate(['/commandesclient']);
  }

  modifierCommande(): void {
    this.router.navigate(['/nouvellecommandeclt', this.commandeId]);
  }

  supprimerCommande(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer la commande "${this.commande.code}" ?`)) {
      this.http.delete(`http://localhost:8888/gestiondestock/api/v1/commandesclients/${this.commandeId}`)
        .subscribe({
          next: () => {
            this.router.navigate(['/commandesclient']);
          },
          error: (error: any) => {
            this.errorMsg = 'Erreur lors de la suppression';
            console.error('Erreur:', error);
          }
        });
    }
  }

  calculerTotal = (total: number, ligne: any): number => {
    return total + ((ligne.quantite || 0) * (ligne.prixUnitaire || 0));
  }

  getTotalCommande(): number {
    if (this.commande.ligneCommandeClients) {
      return this.commande.ligneCommandeClients.reduce(this.calculerTotal, 0);
    }
    return 0;
  }

  getNbArticles(): number {
    return this.commande.ligneCommandeClients?.length || 0;
  }
}
