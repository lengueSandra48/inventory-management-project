import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EntrepriseService } from '../../../services/entreprise/entreprise';

// Local interface to replace missing generated DTO
interface EntrepriseResponseDto {
  id?: number;
  nomEntreprise?: string;
  description?: string;
  email?: string;
  numTel?: string;
  steWeb?: string;
  codeFiscal?: string;
  photo?: string;
  adresse?: {
    id?: number;
    adresse1?: string;
    adresse2?: string;
    ville?: string;
    codePostal?: string;
    pays?: string;
  };
}

@Component({
  selector: 'app-detail-entreprise',
  imports: [CommonModule],
  templateUrl: './detail-entreprise.html',
  styleUrl: './detail-entreprise.css'
})
export class DetailEntreprise implements OnInit {
  
  entreprise: EntrepriseResponseDto | null = null;
  isLoading = false;
  errorMsg = '';
  entrepriseId: number = 0;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly entrepriseService: EntrepriseService
  ) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];
    if (id) {
      this.entrepriseId = +id;
      this.loadEntreprise();
    }
  }

  loadEntreprise(): void {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.entrepriseService.findById(this.entrepriseId).subscribe({
      next: (data: EntrepriseResponseDto) => {
        this.entreprise = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMsg = 'Erreur lors du chargement de l\'entreprise: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  retourALaListe(): void {
    this.router.navigate(['entreprises']);
  }

  modifier(): void {
    this.router.navigate(['nouvelleentreprise', this.entrepriseId]);
  }

  supprimer(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'entreprise "${this.entreprise?.nomEntreprise}" ?`)) {
      this.entrepriseService.delete(this.entrepriseId).subscribe({
        next: () => {
          this.router.navigate(['entreprises']);
        },
        error: (error) => {
          this.errorMsg = 'Erreur lors de la suppression: ' + (error.error?.message || error.message);
        }
      });
    }
  }
}
