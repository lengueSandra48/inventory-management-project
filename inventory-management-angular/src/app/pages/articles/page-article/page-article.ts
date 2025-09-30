import { Component, OnInit } from '@angular/core';
import {BouttonAction} from '../../../composants/boutton-action/boutton-action';
import {Router} from '@angular/router'
import {CommonModule} from '@angular/common';
import {ArticleResponseDto} from '../../../../gs-api/src';
import {Article} from '../../../services/article/article';
import {ModalConfirmation} from '../../../composants/modal-confirmation/modal-confirmation';
import {DataTable, DataTableColumn, DataTableAction} from '../../../composants/data-table/data-table';
@Component({
  selector: 'app-page-article',
  imports: [
    CommonModule,
    BouttonAction,
    ModalConfirmation,
    DataTable
  ],
  templateUrl: './page-article.html',
  styleUrl: './page-article.css'
})
export class PageArticle implements OnInit {
  listArticle: Array<ArticleResponseDto>=[];
  errorMsg = ''
  showDeleteModal = false;
  articleToDelete: ArticleResponseDto | null = null;
  loading = false;

  // DataTable configuration
  columns: DataTableColumn[] = [
    { key: 'codeArticle', label: 'Code', type: 'text', sortable: true },
    { key: 'designation', label: 'Désignation', type: 'text', sortable: true },
    { key: 'categorie.designation', label: 'Catégorie', type: 'text', sortable: true },
    { key: 'prixUnitaireFormatted', label: 'Prix Unitaire', type: 'text', sortable: true }
  ];

  actions: DataTableAction[] = [
    { icon: 'fas fa-edit', class: 'btn-edit', tooltip: 'Modifier', action: 'edit' },
    { icon: 'fas fa-trash', class: 'btn-delete', tooltip: 'Supprimer', action: 'delete' },
    { icon: 'fas fa-eye', class: 'btn-view', tooltip: 'Voir détails', action: 'view' }
  ];
  
  constructor(
    private readonly router: Router,
    private readonly articleService: Article
  ) {
  }

  ngOnInit(): void{
     this.findListArticle()
  }

  findListArticle(): void{
    this.loading = true;
    this.articleService.findAllArticle()
      .subscribe({
        next: (articles) =>{
          this.listArticle = articles.map(article => ({
            ...article,
            prixUnitaireFormatted: `€${article.prixUnitaire?.toFixed(2) || '0.00'}`
          }));
          this.loading = false;
          this.errorMsg = '';
        },
        error: (error) => {
          console.error('Erreur lors du chargement des articles:', error);
          this.errorMsg = 'Erreur lors du chargement des articles';
          this.loading = false;
        }
      })
  }
  nouvelArticle(): void{
    this.router.navigate(['nouvelarticle']);
  }


  handleSuppression($event: any):void {
    if($event === 'success'){
        this.findListArticle()
      }else{
        this.errorMsg = $event
      }
  }

  modifierArticle(article: ArticleResponseDto): void {
    this.router.navigate(['nouvelarticle', article.id]);
  }

  supprimerArticle(article: ArticleResponseDto): void {
    this.articleToDelete = article;
    this.showDeleteModal = true;
  }

  confirmerSuppression(): void {
    if (this.articleToDelete?.id) {
      this.articleService.deleteArticle(this.articleToDelete.id)
        .subscribe({
          next: () => {
            this.findListArticle();
            this.showDeleteModal = false;
            this.articleToDelete = null;
          },
          error: (error) => {
            this.errorMsg = 'Erreur lors de la suppression';
            this.showDeleteModal = false;
          }
        });
    }
  }

  annulerSuppression(): void {
    this.showDeleteModal = false;
    this.articleToDelete = null;
  }

  getDeleteMessage(): string {
    return `Êtes-vous sûr de vouloir supprimer l'article "${this.articleToDelete?.designation || ''}" ?`;
  }

  onActionClick(event: {action: string, item: any}): void {
    switch (event.action) {
      case 'edit':
        this.modifierArticle(event.item);
        break;
      case 'delete':
        this.supprimerArticle(event.item);
        break;
      case 'view':
        this.voirDetails(event.item);
        break;
    }
  }

  onSortChange(event: {column: string, direction: 'asc' | 'desc'}): void {
    // Handle sorting logic here if needed
    console.log('Sort:', event);
  }

  voirDetails(article: ArticleResponseDto): void {
    this.router.navigate(['articles', 'detail', article.id]);
  }
}
