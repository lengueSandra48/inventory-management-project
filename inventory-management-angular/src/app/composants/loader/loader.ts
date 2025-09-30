import { Component } from '@angular/core';
import {LoaderService} from './service/loader';
import {Subscription} from 'rxjs';
import {LoaderState} from './loader.model';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-loader',
  imports: [
    NgIf
  ],
  templateUrl: './loader.html',
  styleUrl: './loader.css'
})
export class Loader {
  show = false
  subscription: Subscription | undefined
  constructor(
    private loaderService: LoaderService
  ) {
  }

  ngOnInit(): void{
    this.loaderService.loaderState
      .subscribe({
        next: (state: LoaderState)=>{
          this.show = state.show
        }
      })
  }

  ngOnDestroy(): void{
    this.subscription?.unsubscribe()
  }
}
