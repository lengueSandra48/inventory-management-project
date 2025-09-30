import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { EntrepriseResponseDto } from '../../../gs-api/src';

// Interface locale pour EntrepriseRequestDto
interface EntrepriseRequestDto {
  nomEntreprise?: string;
  description?: string;
  email?: string;
  codeFiscal?: string;
  numTel?: string;
  steWeb?: string;
  photo?: string;
  adresse?: {
    adresse1?: string;
    adresse2?: string;
    ville?: string;
    codePostal?: string;
    pays?: string;
  };
}

@Injectable({
  providedIn: 'root'
})
export class EntrepriseService {
  private readonly baseUrl = 'http://localhost:8888/gestiondestock/api/v1/entreprises';

  constructor(
    private readonly http: HttpClient
  ) {
  }

  findAll(): Observable<EntrepriseResponseDto[]> {
    return this.http.get<EntrepriseResponseDto[]>(`${this.baseUrl}/showAll`);
  }

  findById(id: number): Observable<EntrepriseResponseDto> {
    return this.http.get<EntrepriseResponseDto>(`${this.baseUrl}/${id}`);
  }

  create(entreprise: EntrepriseRequestDto, photo?: File): Observable<EntrepriseResponseDto> {
    // Construire les paramètres de requête
    const params = new URLSearchParams();
    params.append('nomEntreprise', entreprise.nomEntreprise || '');
    params.append('description', entreprise.description || '');
    params.append('email', entreprise.email || '');
    params.append('adresse1', entreprise.adresse?.adresse1 || '');
    if (entreprise.adresse?.adresse2) {
      params.append('adresse2', entreprise.adresse.adresse2);
    }
    params.append('ville', entreprise.adresse?.ville || '');
    params.append('codePostal', entreprise.adresse?.codePostal || '');
    params.append('pays', entreprise.adresse?.pays || '');
    params.append('codeFiscal', entreprise.codeFiscal || '');
    params.append('numTel', entreprise.numTel || '');
    params.append('steWeb', entreprise.steWeb || '');
    
    const url = `${this.baseUrl}/create?${params.toString()}`;
    
    // FormData pour la photo uniquement
    const formData = new FormData();
    if (photo) {
      formData.append('photo', photo);
    }
    
    return this.http.post<EntrepriseResponseDto>(url, formData);
  }

  update(id: number, entreprise: EntrepriseRequestDto, photo?: File): Observable<EntrepriseResponseDto> {
    // Construire les paramètres de requête
    const params = new URLSearchParams();
    params.append('nomEntreprise', entreprise.nomEntreprise || '');
    params.append('description', entreprise.description || '');
    params.append('email', entreprise.email || '');
    params.append('adresse1', entreprise.adresse?.adresse1 || '');
    if (entreprise.adresse?.adresse2) {
      params.append('adresse2', entreprise.adresse.adresse2);
    }
    params.append('ville', entreprise.adresse?.ville || '');
    params.append('codePostal', entreprise.adresse?.codePostal || '');
    params.append('pays', entreprise.adresse?.pays || '');
    params.append('codeFiscal', entreprise.codeFiscal || '');
    params.append('numTel', entreprise.numTel || '');
    params.append('steWeb', entreprise.steWeb || '');
    
    const url = `${this.baseUrl}/update/${id}?${params.toString()}`;
    
    // FormData pour la photo uniquement
    const formData = new FormData();
    if (photo) {
      formData.append('photo', photo);
    }
    
    return this.http.put<EntrepriseResponseDto>(url, formData);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }
}
