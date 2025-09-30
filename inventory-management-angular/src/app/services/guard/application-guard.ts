import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, RouterStateSnapshot} from '@angular/router';
import {UserService} from '../user/user';

@Injectable({
  providedIn: 'root'
})
export class ApplicationGuard implements CanActivate{

  constructor(
    private userService: UserService
  ) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    return this.userService.isUserLoggerAndAccessTokenValid();
  }

}
