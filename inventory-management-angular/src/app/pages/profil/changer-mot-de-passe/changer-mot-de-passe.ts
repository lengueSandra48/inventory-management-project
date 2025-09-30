import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-changer-mot-de-passe',
  imports: [],
  templateUrl: './changer-mot-de-passe.html',
  styleUrl: './changer-mot-de-passe.css'
})
export class ChangerMotDePasse {
  constructor(
    private router: Router
  ) {
  }
  cancel(): void {
    this.router.navigate(['profil'])
  }
}
