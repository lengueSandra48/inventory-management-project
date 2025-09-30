import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-page-profil',
  imports: [],
  templateUrl: './page-profil.html',
  styleUrl: './page-profil.css'
})
export class PageProfil {
  constructor(
    private router: Router
  ) {
  }
  modifierMotDePasse(): void {
    this.router.navigate(['changermotdepasse'])
  }
}
