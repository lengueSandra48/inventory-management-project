import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CategorieResponseDto } from '../../../../gs-api/src';
import { Category } from '../../../services/category/category';

@Component({
  selector: 'app-detail-categorie',
  imports: [CommonModule],
  templateUrl: './detail-categorie.html',
  styleUrls: ['./detail-categorie.css']
})
export class DetailCategorieComponent implements OnInit {

  categorie: CategorieResponseDto = {};
  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly categoryService: Category
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadCategorie(+id);
    }
  }

  loadCategorie(id: number): void {
    this.isLoading = true;
    this.categoryService.findById(id).subscribe({
      next: (data: CategorieResponseDto) => {
        this.categorie = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Erreur lors du chargement de la catégorie: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  retour(): void {
    this.router.navigate(['categories']);
  }

  modifier(): void {
    this.router.navigate(['nouvellecategorie', this.categorie.id]);
  }
}
