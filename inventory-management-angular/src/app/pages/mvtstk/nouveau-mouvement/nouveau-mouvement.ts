import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MvtStkService } from '../../../services/mvtstk.service';
import { MvtStkRequestDto, MvtStkResponseDto, ArticleResponseDto } from '../../../../gs-api/src/model/models';
import { Article } from '../../../services/article/article';

@Component({
  selector: 'app-nouveau-mouvement',
  imports: [CommonModule, FormsModule],
  templateUrl: './nouveau-mouvement.html',
  styleUrl: './nouveau-mouvement.css'
})
export class NouveauMouvement implements OnInit {
  
  mouvement: MvtStkRequestDto = {
    dateMvt: new Date().toISOString(),
    typeMvt: 'ENTREE' as any,
    quantite: 0,
    articleId: undefined,
    entrepriseId: 1 // Default enterprise ID
  };
  
  isEditMode = false;
  errorMsg = '';
  articles: ArticleResponseDto[] = [];
  
  typesMouvement = [
    { value: 'ENTREE', label: 'Entrée' },
    { value: 'SORTIE', label: 'Sortie' }
  ];

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly mvtStkService: MvtStkService,
    private readonly articleService: Article
  ) {}

  ngOnInit(): void {
    this.loadArticles();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadMouvement(+id);
    }
  }

  loadArticles(): void {
    this.articleService.findAllArticle().subscribe({
      next: (articles: ArticleResponseDto[]) => {
        this.articles = articles;
      },
      error: (error: any) => {
        console.error('Erreur lors du chargement des articles:', error);
      }
    });
  }

  loadMouvement(id: number): void {
    this.mvtStkService.findById(id).subscribe({
      next: (mouvement) => {
        this.mouvement = {
          dateMvt: mouvement.dateMvt || '',
          typeMvt: mouvement.typeMvt || 'ENTREE',
          quantite: mouvement.quantite || 0,
          articleId: mouvement.article?.id,
          entrepriseId: mouvement.entrepriseId
        };
        this.errorMsg = '';
      },
      error: (error) => {
        console.error('Erreur lors du chargement du mouvement:', error);
        this.errorMsg = 'Erreur lors du chargement du mouvement';
      }
    });
  }

  onSubmit(): void {
    if (this.isEditMode) {
      this.updateMouvement();
    } else {
      this.createMouvement();
    }
  }

  createMouvement(): void {
    // Convert date string to ISO format for backend and ensure articleId is a number
    const mouvementToSend = {
      ...this.mouvement,
      dateMvt: this.mouvement.dateMvt ? new Date(this.mouvement.dateMvt).toISOString() : new Date().toISOString(),
      articleId: this.mouvement.articleId ? Number(this.mouvement.articleId) : undefined,
      quantite: Number(this.mouvement.quantite)
    };
    
    this.mvtStkService.create(mouvementToSend).subscribe({
      next: (response: MvtStkResponseDto) => {
        console.log('Mouvement créé avec succès:', response);
        this.router.navigate(['/mvtstk']);
      },
      error: (error: any) => {
        console.error('Erreur lors de la création:', error);
        this.errorMsg = 'Erreur lors de la création du mouvement';
      }
    });
  }

  updateMouvement(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      // Convert date string to ISO format for backend and ensure articleId is a number
      const mouvementToSend = {
        ...this.mouvement,
        dateMvt: this.mouvement.dateMvt ? new Date(this.mouvement.dateMvt).toISOString() : new Date().toISOString(),
        articleId: this.mouvement.articleId ? Number(this.mouvement.articleId) : undefined,
        quantite: Number(this.mouvement.quantite)
      };
      
      this.mvtStkService.update(+id, mouvementToSend).subscribe({
        next: (response: MvtStkResponseDto) => {
          console.log('Mouvement modifié avec succès:', response);
          this.router.navigate(['/mvtstk']);
        },
        error: (error: any) => {
          console.error('Erreur lors de la modification:', error);
          this.errorMsg = 'Erreur lors de la modification du mouvement';
        }
      });
    }
  }

  annuler(): void {
    this.router.navigate(['/mvtstk']);
  }

  getPageTitle(): string {
    return this.isEditMode ? 'Modifier le mouvement' : 'Nouveau mouvement';
  }
}
