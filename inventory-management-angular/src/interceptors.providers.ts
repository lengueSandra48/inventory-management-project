import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {Intercepter} from './app/services/intercepter/intercepter';


export const httpInterceptorProviders = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: Intercepter,
    multi: true
  }
];
