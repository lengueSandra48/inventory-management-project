import {Component, Input, Output, EventEmitter} from '@angular/core';
import {LigneCommandeClientResponseDto} from '../../../gs-api/src';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-detail-cmd',
  imports: [CurrencyPipe],
  templateUrl: './detail-cmd.html',
  styleUrl: './detail-cmd.css'
})
export class DetailCmd {

    @Input() ligneCommande: LigneCommandeClientResponseDto={}
    @Output() supprimerLigneEvent = new EventEmitter<LigneCommandeClientResponseDto>();

    supprimerLigne(): void {
      this.supprimerLigneEvent.emit(this.ligneCommande);
    }
}
