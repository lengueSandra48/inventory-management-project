import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ClientResponseDto} from '../../../gs-api/src';
import {Router} from '@angular/router';
import {Cltfrs} from '../../services/cltfrs/cltfrs';

@Component({
  selector: 'app-detail-clt-frs',
  imports: [],
  templateUrl: './detail-clt-frs.html',
  styleUrl: './detail-clt-frs.css'
})
export class DetailCltFrs {

    @Input() clientFournisseur: any = {}
  @Output() suppressionResult = new EventEmitter()
  @Input() origin: string = '';


  constructor(
    private router: Router,
    private cltFrsService: Cltfrs
  ) {
  }
  confirmerEtSupprimer():void {
      if(this.origin === 'client'){
        this.cltFrsService.deleteClient(this.clientFournisseur.id)
          .subscribe({
            next: (res)=>{
              this.suppressionResult.emit('success')
            },error: (error)=>{
              this.suppressionResult.emit(error.error.error)
            }
          })
      }else if(this.origin === 'fournisseur'){
        this.cltFrsService.deleteFournisseur(this.clientFournisseur.id)
          .subscribe({
            next: (res)=>{
              this.suppressionResult.emit('success')
            },error: (error)=>{
              this.suppressionResult.emit(error.error.error)
            }
          })
      }
  }
}
