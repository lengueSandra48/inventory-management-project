import { Component } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CategorieRequestDto} from '../../../../gs-api/src';
import {FormsModule} from '@angular/forms';
import {Category} from '../../../services/category/category';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-nouvelle-category',
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './nouvelle-category.html',
  styleUrl: './nouvelle-category.css'
})
export class NouvelleCategory {

  errorsMsg: Array<string> = []

  categoryRequestDto: CategorieRequestDto={}
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private categoryService : Category
  ){}

  ngOnInit(): void{
    const idCategory = this.activatedRoute.snapshot.params['idCategory'];
    if(idCategory){
      this.categoryService.findById(idCategory)
        .subscribe({
          next: (cat) =>{
            this.categoryRequestDto = cat
          }
        })
    }

  }
  cancel():void {
    this.router.navigate(['categories'])
  }

  enregistrerCategory(): void {
    this.categoryService.enregistrerCategory(this.categoryRequestDto)
      .subscribe({
        next: (res) => {
          this.router.navigate(['categories'])
        },
        error: (error) => {

          this.errorsMsg =error.error.errors;

        }

      })
  }
}
