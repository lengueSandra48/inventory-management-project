import { Component } from '@angular/core';
import {BouttonAction} from "../../../composants/boutton-action/boutton-action";
import {Pagination} from "../../../composants/pagination/pagination";
import {Router} from '@angular/router';
import {ClientResponseDto} from '../../../../gs-api/src';
import {Cltfrs} from '../../../services/cltfrs/cltfrs';
import {NgForOf, NgIf} from '@angular/common';
import {ModalConfirmation} from '../../../composants/modal-confirmation/modal-confirmation';

@Component({
  selector: 'app-page-client',
  imports: [
    BouttonAction,
    Pagination,
    NgForOf,
    NgIf,
    ModalConfirmation
  ],
  templateUrl: './page-client.html',
  styleUrl: './page-client.css'
})
export class PageClient {
  listclient: Array<ClientResponseDto>=[];
 errorMsg= '';
  showDeleteModal = false;
  clientToDelete: ClientResponseDto | null = null;
  constructor(
    private router: Router,
    private cltFrsService : Cltfrs
  ) {
  }

  ngOnInit(): void{
    this.findAllClient()
  }

  findAllClient(): void{
    this.cltFrsService.findAllClients()
      .subscribe({
        next: (clients)=>{
          this.listclient = clients
        }
      })
  }
  nouveauClient(): void{
    this.router.navigate(['nouveauclient']);
  }

  handleSuppression($event: any) {
    if($event === 'success'){
      this.findAllClient()
    }else{
      this.errorMsg = $event
    }
  }

  modifierClient(client: ClientResponseDto): void {
    this.router.navigate(['nouveauclient', client.id]);
  }

  supprimerClient(client: ClientResponseDto): void {
    this.clientToDelete = client;
    this.showDeleteModal = true;
  }

  confirmerSuppression(): void {
    if (this.clientToDelete?.id) {
      this.cltFrsService.deleteClient(this.clientToDelete.id)
        .subscribe({
          next: () => {
            this.findAllClient();
            this.fermerModal();
          },
          error: (error: any) => {
            this.errorMsg = 'Erreur lors de la suppression';
            this.fermerModal();
          }
        });
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.clientToDelete = null;
  }

  getDeleteMessage(): string {
    return `Êtes-vous sûr de vouloir supprimer le client "${this.clientToDelete?.nom || ''} ${this.clientToDelete?.prenom || ''}" ?`;
  }

  voirDetails(client: ClientResponseDto): void {
    if (client.id) {
      this.router.navigate(['/detailclient', client.id]);
    }
  }
}
