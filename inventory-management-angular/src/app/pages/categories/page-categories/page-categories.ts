import { Component } from '@angular/core';
import {BouttonAction} from "../../../composants/boutton-action/boutton-action";
import {Pagination} from "../../../composants/pagination/pagination";
import {Router} from '@angular/router';
import {CategorieResponseDto} from '../../../../gs-api/src';
import {Category} from '../../../services/category/category';
import {NgForOf, NgIf} from '@angular/common';
import {ModalConfirmation} from '../../../composants/modal-confirmation/modal-confirmation';

@Component({
  selector: 'app-page-categories',
  imports: [
    BouttonAction,
    Pagination,
    NgForOf,
    NgIf,
    ModalConfirmation
  ],
  templateUrl: './page-categories.html',
  styleUrl: './page-categories.css'
})
export class PageCategories {

   listCategories: Array<CategorieResponseDto> = []
  selectedCatIdToDelete: number | undefined =-1
  errorMsg =''
  categoryResponseDto: CategorieResponseDto={}
  showDeleteModal = false
  categoryToDelete: CategorieResponseDto | null = null
  constructor(
    private router: Router,
    private categoryService: Category
  ) {
  }

  ngOnInit(): void {
    this.findAllCategories()
  }

  findAllCategories(): void{
    this.categoryService.findAll()
      .subscribe({
        next: (res) => {
          this.listCategories = res
        }
      })
  }
  nouvelCategory(): void {
    this.router.navigate(['nouvellecategorie'])
  }

  modifierCategory(id?: number | undefined): void {
    this.router.navigate(['nouvellecategorie', id])
  }

  supprimerCategorie(id: number | undefined): void {
    if (id) {
      const category = this.listCategories.find(c => c.id === id);
      if (category) {
        this.categoryToDelete = category;
        this.showDeleteModal = true;
      }
    }
  }

  confirmerSuppression(): void {
    if (this.categoryToDelete?.id) {
      this.categoryService.delete(this.categoryToDelete.id)
        .subscribe({
          next: () => {
            this.findAllCategories();
            this.fermerModal();
          },
          error: (err: any) => {
            this.errorMsg = err.error.message;
            this.fermerModal();
          }
        });
    }
  }

  fermerModal(): void {
    this.showDeleteModal = false;
    this.categoryToDelete = null;
  }

  getDeleteMessage(): string {
    return `Êtes-vous sûr de vouloir supprimer la catégorie "${this.categoryToDelete?.designation || ''}" ?`;
  }

  annulerSuppressionCat(): void {
    this.selectedCatIdToDelete = -1
  }

  selectCategoriePourSupprimer(id?: number | undefined): void {
      this.selectedCatIdToDelete = id
  }

  voirDetails(cat: CategorieResponseDto): void {
    if (cat.id) {
      this.router.navigate(['detailcategorie', cat.id]);
    }
  }
}
