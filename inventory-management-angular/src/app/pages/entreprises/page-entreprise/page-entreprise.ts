import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
import { BouttonAction } from '../../../composants/boutton-action/boutton-action';
import { DataTable, DataTableColumn, DataTableAction } from '../../../composants/data-table/data-table';
import { ModalConfirmation } from '../../../composants/modal-confirmation/modal-confirmation';

@Component({
  selector: 'app-page-entreprise',
  templateUrl: './page-entreprise.html',
  styleUrls: ['./page-entreprise.css'],
  imports: [CommonModule, BouttonAction, DataTable, ModalConfirmation]
})
export class PageEntrepriseComponent implements OnInit {

  listeEntreprises: Array<EntrepriseResponseDto> = [];
  errorMsg: Array<string> = [];
  showDeleteModal = false;
  entrepriseToDelete: EntrepriseResponseDto | null = null;
  isLoading = false;

  // DataTable configuration
  columns: DataTableColumn[] = [
    { key: 'nomEntreprise', label: 'Nom Entreprise', type: 'text', sortable: true },
    { key: 'email', label: 'Email', type: 'text', sortable: true },
    { key: 'numTel', label: 'Téléphone', type: 'text', sortable: false },
    { key: 'adresse.adresse1', label: 'Adresse', type: 'text', sortable: false }
  ];

  actions: DataTableAction[] = [
    { icon: 'fas fa-edit', class: 'btn-edit', tooltip: 'Modifier', action: 'edit' },
    { icon: 'fas fa-trash', class: 'btn-delete', tooltip: 'Supprimer', action: 'delete' },
    { icon: 'fas fa-eye', class: 'btn-view', tooltip: 'Voir détails', action: 'view' }
  ];

  constructor(
    private readonly router: Router,
    private readonly entrepriseService: EntrepriseService
  ) { }

  ngOnInit(): void {
    this.findAllEntreprises();
  }

  findAllEntreprises(): void {
    this.isLoading = true;
    this.errorMsg = [];
    
    this.entrepriseService.findAll()
      .subscribe({
        next: (entreprises: any[]) => {
          this.listeEntreprises = entreprises;
          this.isLoading = false;
        },
        error: (error: any) => {
          console.error('Erreur lors du chargement des entreprises:', error);
          this.errorMsg = ['Erreur lors du chargement des entreprises'];
          this.isLoading = false;
        }
      });
  }


  nouvelleEntreprise(): void {
    this.router.navigate(['nouvelleentreprise']);
  }

  modifierEntreprise(entreprise: EntrepriseResponseDto): void {
    this.router.navigate(['nouvelleentreprise', entreprise.id]);
  }

  voirDetails(entreprise: EntrepriseResponseDto): void {
    this.router.navigate(['detailentreprise', entreprise.id]);
  }

  supprimerEntreprise(entreprise: EntrepriseResponseDto): void {
    if (entreprise) {
      this.entrepriseToDelete = entreprise;
      this.showDeleteModal = true;
    }
  }

  confirmerSuppression(): void {
    if (this.entrepriseToDelete?.id) {
      this.isLoading = true;
      this.errorMsg = [];
      
      this.entrepriseService.delete(this.entrepriseToDelete.id)
        .subscribe({
          next: () => {
            this.findAllEntreprises();
            this.fermerModal();
            this.isLoading = false;
          },
          error: (error: any) => {
            console.error('Erreur lors de la suppression:', error);
            let errorMessage = 'Erreur lors de la suppression de l\'entreprise';
            
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            } else if (error.message) {
              errorMessage = error.message;
            }
            
            this.errorMsg = [errorMessage];
            this.fermerModal();
            this.isLoading = false;
          }
        });
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.entrepriseToDelete = null;
  }

  getDeleteMessage(): string {
    return `Êtes-vous sûr de vouloir supprimer l'entreprise "${this.entrepriseToDelete?.nomEntreprise || ''}" ?`;
  }

  onActionClick(event: {action: string, item: EntrepriseResponseDto}): void {
    switch (event.action) {
      case 'edit':
        this.modifierEntreprise(event.item);
        break;
      case 'delete':
        this.supprimerEntreprise(event.item);
        break;
      case 'view':
        this.voirDetails(event.item);
        break;
    }
  }

  onSortChange(event: {column: string, direction: 'asc' | 'desc'}): void {
    // Handle sorting logic here if needed
    console.log('Sort:', event);
  }
}
