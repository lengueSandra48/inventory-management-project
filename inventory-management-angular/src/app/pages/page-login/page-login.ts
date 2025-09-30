import { Component } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {UserService} from '../../services/user/user';
import {AuthResponseDto, LoginRequestDto} from '../../../gs-api/src';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-page-login',
  imports: [
    RouterLink,
    FormsModule,
    NgIf
  ],
  templateUrl: './page-login.html',
  styleUrl: './page-login.css'
})
export class PageLogin {

  authenticationRequest: LoginRequestDto = {
    email: '',
    password: ''
  }
  errorMessage = ""
  showPassword = false


  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  login(): void{
    this.userService.login(this.authenticationRequest)
      .subscribe({
        next : (data : AuthResponseDto) => {
          console.log('Login successful:', data);
          this.userService.setConnectedUser(data);
          this.router.navigate(['']);
        },
        error: (error) => {
          console.error('Login error:', error);
          this.errorMessage = 'Login et / ou mot de passe incorrecte';
        }
      });
  }
}
