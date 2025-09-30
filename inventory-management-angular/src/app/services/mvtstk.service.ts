import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MvtStkRequestDto, MvtStkResponseDto } from '../../gs-api/src/model/models';

@Injectable({
  providedIn: 'root'
})
export class MvtStkService {

  private readonly baseUrl = 'http://localhost:8888/gestiondestock/api/v1/mvtstk';

  constructor(private readonly http: HttpClient) { }

  findAll(): Observable<MvtStkResponseDto[]> {
    return this.http.get<MvtStkResponseDto[]>(`${this.baseUrl}/showAll`);
  }

  findById(id: number): Observable<MvtStkResponseDto> {
    return this.http.get<MvtStkResponseDto>(`${this.baseUrl}/${id}`);
  }

  create(mvtStk: MvtStkRequestDto): Observable<MvtStkResponseDto> {
    return this.http.post<MvtStkResponseDto>(`${this.baseUrl}/create`, mvtStk);
  }

  update(id: number, mvtStk: MvtStkRequestDto): Observable<MvtStkResponseDto> {
    return this.http.put<MvtStkResponseDto>(`${this.baseUrl}/update/${id}`, mvtStk);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`);
  }
}
