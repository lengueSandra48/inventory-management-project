import { Injectable } from '@angular/core';
import {
  CommandeClientControllerService,
  CommandeClientRequestDto,
  CommandeClientResponseDto,
  CommandeFournisseurControllerService,
  CommandeFournisseurRequestDto,
  CommandeFournisseurResponseDto,
  LigneCommandeClientResponseDto
} from '../../../gs-api/src';
import {UserService} from '../user/user';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Cmdcltfrs {
  constructor(
    private commandeClientService: CommandeClientControllerService,
    private commandeFournisseurService: CommandeFournisseurControllerService,
    private userService: UserService
  ) { }

  enregistrerCommandeClient(commandeClientDto: CommandeClientRequestDto): Observable<CommandeClientResponseDto> {
    commandeClientDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId;
    return this.commandeClientService.save7(commandeClientDto);
  }

  enregistrerCommandeFournisseur(commandeFournisseurDto: CommandeFournisseurRequestDto): Observable<CommandeFournisseurRequestDto> {
    commandeFournisseurDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId;
    return this.commandeFournisseurService.save6(commandeFournisseurDto);
  }

  findAllCommandesClient(): Observable<CommandeClientResponseDto[]> {
    return this.commandeClientService.findAll7();
  }

  findAllCommandesFournisseur(): Observable<CommandeFournisseurResponseDto[]> {
    return this.commandeFournisseurService.findAll6();
  }

  findAllLigneCommandesClient(idCmd?: number): Observable<CommandeClientResponseDto> {
    if (idCmd) {
      return this.commandeClientService.findById7(idCmd);
    }
    return of();
  }

  findAllLigneCommandesFournisseur(idCmd?: number): Observable<CommandeFournisseurResponseDto> {
    if (idCmd) {
      return this.commandeFournisseurService.findById6(idCmd);
    }
    return of();
  }

  updateCommandeClient(id: number, commandeClientDto: CommandeClientRequestDto): Observable<CommandeClientResponseDto> {
    commandeClientDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId;
    return this.commandeClientService.update7(id, commandeClientDto);
  }

  updateCommandeFournisseur(id: number, commandeFournisseurDto: CommandeFournisseurRequestDto): Observable<CommandeFournisseurResponseDto> {
    commandeFournisseurDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId;
    return this.commandeFournisseurService.update6(id, commandeFournisseurDto);
  }

  deleteCommandeClient(id: number): Observable<any> {
    return this.commandeClientService.delete7(id);
  }

  deleteCommandeFournisseur(id: number): Observable<any> {
    return this.commandeFournisseurService.delete6(id);
  }
}
