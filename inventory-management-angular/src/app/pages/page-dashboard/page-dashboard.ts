import { Component } from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {Menu} from '../../composants/menu/menu';
import {Header} from '../../composants/header/header';
import {Loader} from '../../composants/loader/loader';

@Component({
  selector: 'app-page-dashboard',
  imports: [
    RouterOutlet,
    Menu,
    Header,
    Loader
  ],
  templateUrl: './page-dashboard.html',
  styleUrl: './page-dashboard.css'
})
export class PageDashboard {

}
