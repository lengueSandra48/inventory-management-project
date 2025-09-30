import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientResponseDto } from '../../../../gs-api/src';
import { Cltfrs } from '../../../services/cltfrs/cltfrs';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-detail-client',
  imports: [NgIf],
  templateUrl: './detail-client.html',
  styleUrl: './detail-client.css'
})
export class DetailClient implements OnInit {
  client: ClientResponseDto = {};
  clientId: number = 0;
  errorMsg: string = '';
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cltFrsService: Cltfrs
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.clientId = parseInt(id, 10);
      this.loadClient();
    }
  }

  loadClient(): void {
    this.loading = true;
    this.errorMsg = '';
    
    this.cltFrsService.findclientById(this.clientId)
      .subscribe({
        next: (client: ClientResponseDto) => {
          this.client = client;
          this.loading = false;
        },
        error: (error: any) => {
          this.errorMsg = 'Erreur lors du chargement du client';
          this.loading = false;
          console.error('Erreur:', error);
        }
      });
  }

  retourListe(): void {
    this.router.navigate(['/clients']);
  }

  modifierClient(): void {
    this.router.navigate(['/nouveauclient', this.clientId]);
  }

  supprimerClient(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le client "${this.client.nom} ${this.client.prenom}" ?`)) {
      this.cltFrsService.deleteClient(this.clientId)
        .subscribe({
          next: () => {
            this.router.navigate(['/clients']);
          },
          error: (error: any) => {
            this.errorMsg = 'Erreur lors de la suppression';
            console.error('Erreur:', error);
          }
        });
    }
  }
}
