import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleResponseDto } from '../../../../gs-api/src';
import { Article } from '../../../services/article/article';

@Component({
  selector: 'app-detail-article',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './detail-article.html',
  styleUrl: './detail-article.css'
})
export class DetailArticle implements OnInit {
  
  article: ArticleResponseDto | null = null;
  isLoading = false;
  errorMsg = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly articleService: Article
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadArticle(+id);
    }
  }

  loadArticle(id: number): void {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.articleService.findArticleById(id).subscribe({
      next: (data: ArticleResponseDto) => {
        this.article = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMsg = 'Erreur lors du chargement de l\'article';
        this.isLoading = false;
        console.error('Erreur:', error);
      }
    });
  }

  supprimer(): void {
    if (this.article && confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
      this.articleService.deleteArticle(this.article.id!).subscribe({
        next: () => {
          this.router.navigate(['/articles']);
        },
        error: (error: any) => {
          console.error('Erreur lors de la suppression:', error);
          this.errorMsg = 'Erreur lors de la suppression de l\'article';
        }
      });
    }
  }

  modifier(): void {
    if (this.article?.id) {
      this.router.navigate(['nouvelarticle', this.article.id]);
    }
  }

  retour(): void {
    this.router.navigate(['articles']);
  }
}
