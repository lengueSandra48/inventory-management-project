import { Component } from '@angular/core';
import {BouttonAction} from "../../../composants/boutton-action/boutton-action";
import {Pagination} from "../../../composants/pagination/pagination";
import {Router} from '@angular/router';
import {FournisseurResponseDto} from '../../../../gs-api/src';
import {Cltfrs} from '../../../services/cltfrs/cltfrs';
import {NgForOf, NgIf} from '@angular/common';
import {ModalConfirmation} from '../../../composants/modal-confirmation/modal-confirmation';

@Component({
  selector: 'app-page-fournisseur',
  imports: [
    BouttonAction,
    Pagination,
    NgForOf,
    NgIf,
    ModalConfirmation
  ],
  templateUrl: './page-fournisseur.html',
  styleUrl: './page-fournisseur.css'
})
export class PageFournisseur {
  listFournisseur: Array<FournisseurResponseDto>=[];
  errorMsg='';
  showDeleteModal = false;
  fournisseurToDelete: FournisseurResponseDto | null = null;
  constructor(
    private router: Router,
    private cltFrsService : Cltfrs
  ) {
  }

  ngOnInit(): void{
    this.findAllFournisseurs()
  }

  findAllFournisseurs(): void{
    this.cltFrsService.findAllFournisseurs()
      .subscribe({
        next: (fournisseurs)=>{
          this.listFournisseur = fournisseurs
        }
      })
  }
  nouveauFournisseur(): void{
    this.router.navigate(['nouveaufournisseur']);
  }

  handleSuppression($event: any) {
    if($event === 'success'){
      this.findAllFournisseurs()
    }else{
      this.errorMsg = $event
    }
  }

  modifierFournisseur(fournisseur: FournisseurResponseDto): void {
    this.router.navigate(['nouveaufournisseur', fournisseur.id]);
  }

  supprimerFournisseur(fournisseur: FournisseurResponseDto): void {
    this.fournisseurToDelete = fournisseur;
    this.showDeleteModal = true;
  }

  confirmerSuppression(): void {
    if (this.fournisseurToDelete) {
      this.cltFrsService.deleteFournisseur(this.fournisseurToDelete.id!)
        .subscribe({
          next: () => {
            this.findAllFournisseurs();
            this.fermerModal();
          },
          error: (error) => {
            this.errorMsg = 'Erreur lors de la suppression';
            this.fermerModal();
          }
        });
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.fournisseurToDelete = null;
  }

  getDeleteMessage(): string {
    if (this.fournisseurToDelete) {
      return `Êtes-vous sûr de vouloir supprimer le fournisseur "${this.fournisseurToDelete.nom}" ?`;
    }
    return 'Êtes-vous sûr de vouloir supprimer ce fournisseur ?';
  }

  voirDetails(fournisseur: FournisseurResponseDto): void {
    this.router.navigate(['detailfournisseur', fournisseur.id]);
  }
}
