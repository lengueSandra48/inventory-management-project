import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {UserService} from '../user/user';
import {
  ClientResponseDto,
  FournisseurResponseDto
} from '../../../gs-api/src';
import {ClientRequestDto} from '../../../gs-api/src/model/clientRequestDto';
import {Observable, of} from 'rxjs';
import {FournisseurRequestDto} from '../../../gs-api/src/model/fournisseurRequestDto';

@Injectable({
  providedIn: 'root'
})
export class Cltfrs {
  private readonly baseUrlClient = 'http://localhost:8888/gestiondestock/api/v1/clients';
  private readonly baseUrlFournisseur = 'http://localhost:8888/gestiondestock/api/v1/fournisseurs';

  constructor(
    private readonly userService: UserService,
    private readonly http: HttpClient
  ) {
  }

  enregistrerClient(clientRequestDto: ClientRequestDto, image?: File): Observable<ClientResponseDto>{
    // Utiliser l'entrepriseId du formulaire si fourni, sinon celui de l'utilisateur connecté
    if (!clientRequestDto.entrepriseId) {
      clientRequestDto.entrepriseId = <number>this.userService.getConnectedUser().user?.entrepriseId
    }
    
    // Utiliser FormData pour l'API client (image optionnelle)
    const formData = new FormData();
    formData.append('nom', clientRequestDto.nom || '');
    formData.append('prenom', clientRequestDto.prenom || '');
    formData.append('email', clientRequestDto.email || '');
    formData.append('adresse1', clientRequestDto.adresse?.adresse1 || '');
    formData.append('adresse2', clientRequestDto.adresse?.adresse2 || '');
    formData.append('ville', clientRequestDto.adresse?.ville || '');
    formData.append('codePostal', clientRequestDto.adresse?.codePostal || '');
    formData.append('pays', clientRequestDto.adresse?.pays || '');
    formData.append('numTel', clientRequestDto.numTel || '');
    formData.append('entrepriseId', clientRequestDto.entrepriseId?.toString() || '');
    
    // Image optionnelle - ajouter seulement si fournie
    if (image) {
      formData.append('image', image);
    }
    
    return this.http.post<ClientResponseDto>(`${this.baseUrlClient}/create`, formData);
  }

  enregistrerFournisseur(fournisseurRequestDto: FournisseurRequestDto, image?: File): Observable<FournisseurResponseDto>{
    // Utiliser l'entrepriseId du formulaire si fourni, sinon celui de l'utilisateur connecté
    if (!fournisseurRequestDto.entrepriseId) {
      fournisseurRequestDto.entrepriseId = <number>this.userService.getConnectedUser().user?.entrepriseId
    }
    
    // Utiliser FormData pour l'API fournisseur (image optionnelle)
    const formData = new FormData();
    formData.append('nom', fournisseurRequestDto.nom || '');
    formData.append('prenom', fournisseurRequestDto.prenom || '');
    formData.append('email', fournisseurRequestDto.email || '');
    formData.append('adresse1', fournisseurRequestDto.adresse?.adresse1 || '');
    formData.append('adresse2', fournisseurRequestDto.adresse?.adresse2 || '');
    formData.append('ville', fournisseurRequestDto.adresse?.ville || '');
    formData.append('codePostal', fournisseurRequestDto.adresse?.codePostal || '');
    formData.append('pays', fournisseurRequestDto.adresse?.pays || '');
    formData.append('numTel', fournisseurRequestDto.numTel || '');
    formData.append('entrepriseId', fournisseurRequestDto.entrepriseId?.toString() || '');
    
    // Image optionnelle - ajouter seulement si fournie
    if (image) {
      formData.append('image', image);
    }
    
    return this.http.post<FournisseurResponseDto>(`${this.baseUrlFournisseur}/create`, formData);
  }

  findAllClients(): Observable<Array<ClientResponseDto>>{
    return this.http.get<Array<ClientResponseDto>>(`${this.baseUrlClient}/showAll`);
  }
  
  findAllFournisseurs(): Observable<Array<FournisseurResponseDto>>{
    return this.http.get<Array<FournisseurResponseDto>>(`${this.baseUrlFournisseur}/showAll`);
  }

  findclientById(id: number): Observable<ClientResponseDto>{
    if(id){
      return this.http.get<ClientResponseDto>(`${this.baseUrlClient}/${id}`);
    }
    return of()
  }

  findfournisseurById(id: number): Observable<FournisseurResponseDto>{
    if(id){
      return this.http.get<FournisseurResponseDto>(`${this.baseUrlFournisseur}/${id}`);
    }
    return of()
  }


  deleteClient(idClient: number): Observable<any>{
    if(idClient){
      return this.http.delete(`${this.baseUrlClient}/delete/${idClient}`);
    }
    return of()
  }

  deleteFournisseur(idFournisseur: number): Observable<any>{
    if(idFournisseur){
      return this.http.delete(`${this.baseUrlFournisseur}/delete/${idFournisseur}`);
    }
    return of()
  }

  updateClient(idClient: number, clientRequestDto: ClientRequestDto, image?: File): Observable<ClientResponseDto>{
    // Utiliser l'entrepriseId du formulaire si fourni, sinon celui de l'utilisateur connecté
    if (!clientRequestDto.entrepriseId) {
      clientRequestDto.entrepriseId = <number>this.userService.getConnectedUser().user?.entrepriseId
    }
    
    const formData = new FormData();
    formData.append('nom', clientRequestDto.nom || '');
    formData.append('prenom', clientRequestDto.prenom || '');
    formData.append('email', clientRequestDto.email || '');
    formData.append('adresse1', clientRequestDto.adresse?.adresse1 || '');
    formData.append('adresse2', clientRequestDto.adresse?.adresse2 || '');
    formData.append('ville', clientRequestDto.adresse?.ville || '');
    formData.append('codePostal', clientRequestDto.adresse?.codePostal || '');
    formData.append('pays', clientRequestDto.adresse?.pays || '');
    formData.append('numTel', clientRequestDto.numTel || '');
    formData.append('entrepriseId', clientRequestDto.entrepriseId?.toString() || '');
    
    // Image optionnelle - ajouter seulement si fournie
    if (image) {
      formData.append('image', image);
    }
    
    return this.http.put<ClientResponseDto>(`${this.baseUrlClient}/update/${idClient}`, formData);
  }

  updateFournisseur(idFournisseur: number, fournisseurRequestDto: FournisseurRequestDto, image?: File): Observable<FournisseurResponseDto>{
    // Utiliser l'entrepriseId du formulaire si fourni, sinon celui de l'utilisateur connecté
    if (!fournisseurRequestDto.entrepriseId) {
      fournisseurRequestDto.entrepriseId = <number>this.userService.getConnectedUser().user?.entrepriseId
    }
    
    const formData = new FormData();
    formData.append('nom', fournisseurRequestDto.nom || '');
    formData.append('prenom', fournisseurRequestDto.prenom || '');
    formData.append('email', fournisseurRequestDto.email || '');
    formData.append('adresse1', fournisseurRequestDto.adresse?.adresse1 || '');
    formData.append('adresse2', fournisseurRequestDto.adresse?.adresse2 || '');
    formData.append('ville', fournisseurRequestDto.adresse?.ville || '');
    formData.append('codePostal', fournisseurRequestDto.adresse?.codePostal || '');
    formData.append('pays', fournisseurRequestDto.adresse?.pays || '');
    formData.append('numTel', fournisseurRequestDto.numTel || '');
    formData.append('entrepriseId', fournisseurRequestDto.entrepriseId?.toString() || '');
    
    // Image optionnelle - ajouter seulement si fournie
    if (image) {
      formData.append('image', image);
    }
    
    return this.http.put<FournisseurResponseDto>(`${this.baseUrlFournisseur}/update/${idFournisseur}`, formData);
  }
}
