import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {UserService} from '../user/user';
import {CategorieRequestDto, CategorieResponseDto} from '../../../gs-api/src';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Category {
  private readonly baseUrl = 'http://localhost:8888/gestiondestock/api/v1/categories';

  constructor(
    private readonly userService: UserService,
    private readonly http: HttpClient
  ) {
  }

  enregistrerCategory(categoryRequestDto: CategorieRequestDto): Observable<CategorieResponseDto>{
    categoryRequestDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId
    return this.http.post<CategorieResponseDto>(`${this.baseUrl}/create`, categoryRequestDto);
  }

  updateCategory(idCategorie: number, categoryRequestDto: CategorieRequestDto): Observable<CategorieResponseDto>{
    categoryRequestDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId
    return this.http.put<CategorieResponseDto>(`${this.baseUrl}/update/${idCategorie}`, categoryRequestDto);
  }

  findAll(): Observable<Array<CategorieResponseDto>>{
    return this.http.get<Array<CategorieResponseDto>>(`${this.baseUrl}/showAll`);
  }

  findById(idCategory: number): Observable<CategorieResponseDto> {
    return this.http.get<CategorieResponseDto>(`${this.baseUrl}/${idCategory}`);
  }

  delete(idCategorie?: number):Observable<any> {
    if(idCategorie){
      return this.http.delete(`${this.baseUrl}/delete/${idCategorie}`);
    }
    return of()
  }
}
