import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {EntrepriseRequestDto} from '../../../gs-api/src/model/entrepriseRequestDto';
import {EntrepriseService} from '../../services/entreprise/entreprise';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-page-inscription',
  imports: [
    RouterLink,
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './page-inscription.html',
  styleUrl: './page-inscription.css'
})
export class PageInscription {

  errorsMsg: Array<string> = []

  entrepriseRequestDto: EntrepriseRequestDto={
    nomEntreprise: '',
    description: 'ras',
    email: '',
    adresse: {
      adresse1: '',
      adresse2 : '',
      ville: '',
      codePostal: '',
      pays: ''
    },
    codeFiscal: '',
    numTel: '',
    steWeb: '',
    photo: ''
  }
  private successMsg: string | undefined;

  constructor(
    private entrepriseService: EntrepriseService
  ) {
  }

  inscrire(): void {
    this.entrepriseService.create(this.entrepriseRequestDto)
      .subscribe({
        next: (entrepriseResponseDto) => {
          // Exemple : redirection ou affichage d’un message de succès
          this.successMsg = 'Entreprise enregistrée avec succès !';
          this.errorsMsg = [];
          // Optionnel : rediriger vers une autre page
          // this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          // Affichage d’un message d’erreur lisible
          this.errorsMsg = [error?.error?.message || 'Une erreur est survenue lors de l’inscription.'];
          this.successMsg = '';
        }
      });
  }

}
