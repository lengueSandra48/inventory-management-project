import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UtilisateurControllerService, UtilisateurResponseDto } from '../../../gs-api/src';

@Injectable({
  providedIn: 'root'
})
export class UtilisateurService {

  constructor(private utilisateurController: UtilisateurControllerService) { }

  createUtilisateur(
    nom: string,
    prenom: string,
    email: string,
    motDePasse: string,
    dateDeNaissance: string,
    adresse1: string,
    ville: string,
    codePostal: string,
    pays: string,
    entrepriseId: number,
    adresse2?: string,
    image?: File
  ): Observable<UtilisateurResponseDto> {
    return this.utilisateurController.save1(
      nom, prenom, email, motDePasse, dateDeNaissance,
      adresse1, ville, codePostal, pays, entrepriseId, adresse2, image
    );
  }

  getAllUtilisateurs(): Observable<UtilisateurResponseDto[]> {
    return this.utilisateurController.findAll1();
  }

  getUtilisateurById(id: number): Observable<UtilisateurResponseDto> {
    return this.utilisateurController.findById1(id);
  }

  updateUtilisateur(
    id: number,
    nom: string,
    prenom: string,
    email: string,
    motDePasse: string,
    dateDeNaissance: string,
    adresse1: string,
    ville: string,
    codePostal: string,
    pays: string,
    entrepriseId: number,
    adresse2?: string,
    image?: File
  ): Observable<UtilisateurResponseDto> {
    return this.utilisateurController.update1(
      id, nom, prenom, email, motDePasse, dateDeNaissance,
      adresse1, ville, codePostal, pays, entrepriseId, adresse2, image
    );
  }

  deleteUtilisateur(id: number): Observable<any> {
    return this.utilisateurController.delete1(id);
  }
}
