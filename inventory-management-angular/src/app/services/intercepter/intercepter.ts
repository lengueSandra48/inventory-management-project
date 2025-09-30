import { Injectable } from '@angular/core';
import {HttpHandler, HttpInterceptor, HttpEvent, HttpRequest, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {AuthResponseDto} from '../../../gs-api/src';
import {LoaderService} from '../../composants/loader/service/loader';

@Injectable({
  providedIn: 'root'
})
export class Intercepter implements HttpInterceptor{
  constructor(
    private loaderService: LoaderService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loaderService.show()
    let authenticationResponse: AuthResponseDto = {}
    if (localStorage.getItem('connectedUser')) {
      authenticationResponse = JSON.parse(
        localStorage.getItem('connectedUser') as string
      )

      // Ne pas définir Content-Type pour les requêtes FormData (multipart/form-data)
      const isFormData = req.body instanceof FormData;
      
      const headers: any = {
        Authorization: 'Bearer ' + authenticationResponse.token,
        Accept: 'application/json'
      };
      
      // Ajouter Content-Type seulement si ce n'est pas FormData
      if (!isFormData) {
        headers['Content-Type'] = 'application/json';
      }

      const authReq = req.clone({
        headers: new HttpHeaders(headers)
      })
      return this.handelRequest(authReq, next)
    }
    return this.handelRequest(req, next)
  }

  handelRequest(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{
    return next.handle(req)
      .pipe(tap
      ((event: HttpEvent<any>)=>{
      if(event instanceof HttpResponse){
        this.loaderService.hide()
      }
    },  (error)=>{
      this.loaderService.hide()
    }))
  }
}
