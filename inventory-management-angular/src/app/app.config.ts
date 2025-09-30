import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

import { routes } from './app.routes';
import {httpInterceptorProviders} from '../interceptors.providers';
import { provideApi } from '../gs-api/src/provide-api';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptorsFromDi()),
    ...httpInterceptorProviders,
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideApi('http://localhost:8888')
  ]
};
