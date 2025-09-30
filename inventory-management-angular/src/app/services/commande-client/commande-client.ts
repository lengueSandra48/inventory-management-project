import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { CommandeClientRequestDto, CommandeClientResponseDto } from '../../../gs-api/src';
import { UserService } from '../user/user';

@Injectable({
  providedIn: 'root'
})
export class CommandeClientService {
  private readonly baseUrl = 'http://localhost:8888/gestiondestock/api/v1/commandesclients';

  constructor(
    private readonly http: HttpClient,
    private readonly userService: UserService
  ) {}

  create(commandeDto: CommandeClientRequestDto): Observable<CommandeClientResponseDto> {
    commandeDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId;
    return this.http.post<CommandeClientResponseDto>(`${this.baseUrl}/create`, commandeDto);
  }

  findById(id: number): Observable<CommandeClientResponseDto> {
    if (id) {
      return this.http.get<CommandeClientResponseDto>(`${this.baseUrl}/${id}`);
    }
    return of();
  }

  findByCode(code: string): Observable<CommandeClientResponseDto> {
    if (code) {
      return this.http.get<CommandeClientResponseDto>(`${this.baseUrl}/code/${code}`);
    }
    return of();
  }

  findAll(): Observable<CommandeClientResponseDto[]> {
    return this.http.get<CommandeClientResponseDto[]>(`${this.baseUrl}/showAll`);
  }

  update(id: number, commandeDto: CommandeClientRequestDto): Observable<CommandeClientResponseDto> {
    commandeDto.entrepriseId = this.userService.getConnectedUser().user?.entrepriseId;
    return this.http.put<CommandeClientResponseDto>(`${this.baseUrl}/update/${id}`, commandeDto);
  }

  delete(id: number): Observable<any> {
    if (id) {
      return this.http.delete(`${this.baseUrl}/delete/${id}`);
    }
    return of();
  }
}
