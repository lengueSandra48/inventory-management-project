import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {PageLogin} from './pages/page-login/page-login';
import {PageInscription} from './pages/page-inscription/page-inscription';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('gestiondestocks');
}
