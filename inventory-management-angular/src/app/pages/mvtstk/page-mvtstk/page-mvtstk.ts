import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { BouttonAction } from '../../../composants/boutton-action/boutton-action';
import { ModalConfirmation } from '../../../composants/modal-confirmation/modal-confirmation';
import { DataTable, DataTableColumn, DataTableAction } from '../../../composants/data-table/data-table';

import { MvtStkService } from '../../../services/mvtstk.service';

@Component({
  selector: 'app-page-mvtstk',
  imports: [
    CommonModule,
    ModalConfirmation,
    BouttonAction,
    DataTable
  ],
  templateUrl: './page-mvtstk.html',
  styleUrl: './page-mvtstk.css'
})
export class PageMvtstk implements OnInit {
  listMouvements: any[] = [];
  showDeleteModal = false;
  mouvementToDelete: any = null;
  errorMsg = '';
  loading = false;

  // DataTable configuration
  columns: DataTableColumn[] = [
    { key: 'dateMvt', label: 'Date', type: 'date', sortable: true },
    { key: 'article.designation', label: 'Article', type: 'text', sortable: true },
    { 
      key: 'typeMvt', 
      label: 'Type Mouvement', 
      type: 'badge', 
      sortable: true,
      badgeConfig: {
        'ENTREE': { class: 'badge-entree', label: 'ENTREE' },
        'SORTIE': { class: 'badge-sortie', label: 'SORTIE' }
      }
    },
    { key: 'quantite', label: 'Quantité', type: 'text', sortable: true },
    { key: 'source', label: 'Source/Destination', type: 'text' }
  ];

  actions: DataTableAction[] = [
    { icon: 'fas fa-edit', class: 'btn-edit', tooltip: 'Modifier', action: 'edit' },
    { icon: 'fas fa-trash', class: 'btn-delete', tooltip: 'Supprimer', action: 'delete' },
    { icon: 'fas fa-eye', class: 'btn-view', tooltip: 'Voir détails', action: 'view' }
  ];

  constructor(
    private readonly router: Router,
    private readonly mvtStkService: MvtStkService
  ) {}

  ngOnInit(): void {   
    // Initialize movements data
    this.loadMouvements();
  }

  loadMouvements(): void {
    this.loading = true;
    this.mvtStkService.findAll().subscribe({
      next: (mouvements: any[]) => {
        // Add source/destination info to each movement
        this.listMouvements = mouvements.map(mvt => ({
          ...mvt,
          source: mvt.typeMvt === 'ENTREE' ? 'Fournisseur' : 'Client/Vente'
        }));
        this.loading = false;
        this.errorMsg = '';
      },
      error: (error) => {
        console.error('Erreur lors du chargement des mouvements:', error);
        this.errorMsg = 'Erreur lors du chargement des mouvements';
        this.loading = false;
      }
    });
  }

  onActionClick(event: {action: string, item: any}): void {
    switch (event.action) {
      case 'edit':
        this.modifierMouvement(event.item);
        break;
      case 'delete':
        this.supprimerMouvement(event.item);
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

  modifierMouvement(mvt: any): void {
    console.log('Modifier mouvement:', mvt);
    // Navigate to movement edit page
    this.router.navigate(['nouveaumouvement', mvt.id]);
  }

  supprimerMouvement(mvt: any): void {
    this.mouvementToDelete = mvt;
    this.showDeleteModal = true;
  }

  confirmerSuppression(): void {
    if (this.mouvementToDelete?.id) {
      this.mvtStkService.delete(this.mouvementToDelete.id).subscribe({
        next: () => {
          console.log('Mouvement supprimé avec succès');
          // Remove from local list
          const index = this.listMouvements.findIndex(m => m.id === this.mouvementToDelete?.id);
          if (index > -1) {
            this.listMouvements.splice(index, 1);
          }
          this.fermerModal();
          this.errorMsg = '';
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
          this.errorMsg = 'Erreur lors de la suppression du mouvement';
          this.fermerModal();
        }
      });
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.mouvementToDelete = null;
  }

  getDeleteMessage(): string {
    if (this.mouvementToDelete?.dateMvt) {
      const dateStr = new Date(this.mouvementToDelete.dateMvt).toLocaleDateString('fr-FR');
      return `Êtes-vous sûr de vouloir supprimer le mouvement du ${dateStr} pour "${this.mouvementToDelete.article?.designation}" ?`;
    }
    return 'Êtes-vous sûr de vouloir supprimer ce mouvement ?';
  }

  voirDetails(mvt: any): void {
    console.log('Voir détails mouvement:', mvt);
    // Navigate to movement details page
    this.router.navigate(['detailmouvement', mvt.id]);
  }

  nouveauMouvement(): void {
    this.router.navigate(['nouveaumouvement']);
  }
}
