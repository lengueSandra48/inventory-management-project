import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {UserService} from '../user/user';
import {ArticleResponseDto} from '../../../gs-api/src';
import {ArticleRequestDto} from '../../../gs-api/src/model/articleRequestDto';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Article {
    private readonly baseUrl = 'http://localhost:8888/gestiondestock/api/v1/articles';

    constructor(
      private readonly userService: UserService,
      private readonly http: HttpClient
    ) {
    }


    enregistrerArticle(articleRequestDto: ArticleRequestDto, image?: File): Observable<ArticleResponseDto>{
      // @ts-ignore
      articleRequestDto.entrepriseId = <number>this.userService.getConnectedUser().user?.roles[0].entrepriseId
      
      // Toujours utiliser FormData pour l'API article (image optionnelle)
      const formData = new FormData();
      formData.append('codeArticle', articleRequestDto.codeArticle || '');
      formData.append('designation', articleRequestDto.designation || '');
      formData.append('categorieId', articleRequestDto.categorieId?.toString() || '');
      formData.append('entrepriseId', articleRequestDto.entrepriseId?.toString() || '');
      formData.append('prixUnitaire', articleRequestDto.prixUnitaire?.toString() || '');
      formData.append('tauxTva', articleRequestDto.tauxTva?.toString() || '');
      formData.append('prixUnitaireTtc', articleRequestDto.prixUnitaireTtc?.toString() || '');
      
      // Image optionnelle - ajouter seulement si fournie
      if (image) {
        formData.append('image', image);
      }
      
      return this.http.post<ArticleResponseDto>(`${this.baseUrl}/create`, formData);
    }

    findAllArticle():Observable<Array<ArticleResponseDto>>{
      return this.http.get<Array<ArticleResponseDto>>(`${this.baseUrl}/showAll`);
    }

    findArticleById(idArticle?: number) : Observable<ArticleResponseDto>{
      if(idArticle){
        return this.http.get<ArticleResponseDto>(`${this.baseUrl}/id/${idArticle}`);
      }
      return of()
    }

  deleteArticle(idArticle: number):Observable<any> {
      if(idArticle){
        return this.http.delete(`${this.baseUrl}/delete/${idArticle}`);
      }
      return of()
  }

  findArticleByCode(codeArticle: string): Observable<ArticleResponseDto> {
    return this.http.get<ArticleResponseDto>(`${this.baseUrl}/code/${codeArticle}`);
  }

  updateArticle(idArticle: number, articleRequestDto: ArticleRequestDto, image?: File): Observable<ArticleResponseDto>{
    // @ts-ignore
    articleRequestDto.entrepriseId = <number>this.userService.getConnectedUser().user?.roles[0].entrepriseId
    
    // Toujours utiliser FormData pour l'API article (image optionnelle)
    const formData = new FormData();
    formData.append('codeArticle', articleRequestDto.codeArticle || '');
    formData.append('designation', articleRequestDto.designation || '');
    formData.append('categorieId', articleRequestDto.categorieId?.toString() || '');
    formData.append('entrepriseId', articleRequestDto.entrepriseId?.toString() || '');
    formData.append('prixUnitaire', articleRequestDto.prixUnitaire?.toString() || '');
    formData.append('tauxTva', articleRequestDto.tauxTva?.toString() || '');
    formData.append('prixUnitaireTtc', articleRequestDto.prixUnitaireTtc?.toString() || '');
    
    // Image optionnelle - ajouter seulement si fournie
    if (image) {
      formData.append('image', image);
    }
    
    return this.http.put<ArticleResponseDto>(`${this.baseUrl}/update/${idArticle}`, formData);
  }
}
