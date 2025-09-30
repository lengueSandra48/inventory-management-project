import { Component } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Article} from '../../../services/article/article';
import {ArticleRequestDto} from '../../../../gs-api/src/model/articleRequestDto';
import { CategorieResponseDto} from '../../../../gs-api/src';
import {Category} from '../../../services/category/category';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-nouvel-article',
  imports: [
    NgForOf,
    FormsModule,
    NgIf
  ],
  templateUrl: './nouvel-article.html',
  styleUrl: './nouvel-article.css'
})
export class NouvelArticle {

  articleRequestDto: ArticleRequestDto = {
    codeArticle: '',
    designation: '',
    prixUnitaire: 0,
    tauxTva: 0,
    prixUnitaireTtc: 0,
    photo: '',
    categorieId: 0,
    entrepriseId: 0
  }
  categorieResponseDto: CategorieResponseDto = {}
  listeCategorie: Array<CategorieResponseDto> = []
  errorMsg : Array<string>=[]
  selectedImage: File | null = null
  isEditMode = false

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private articleService: Article,
    private categoriService: Category
  ) {
  }

  ngOnInit(): void{
    this.categoriService.findAll()
      .subscribe({
        next : (categories)=>{
          this.listeCategorie = categories
          console.log('Categories loaded:', categories);
        },
        error: (error) => {
          console.error('Erreur lors du chargement des catégories:', error);
          this.errorMsg = ['Erreur lors du chargement des catégories'];
        }
      })

    const idArticle = this.activatedRoute.snapshot.params['idArticle']
    if(idArticle){
      this.isEditMode = true;
      this.articleService.findArticleById(idArticle)
        .subscribe({
          next: (article)=>{
            this.articleRequestDto = {
              codeArticle: article.codeArticle ?? '',
              designation: article.designation ?? '',
              prixUnitaire: article.prixUnitaire ?? 0,
              tauxTva: article.tauxTva ?? 0,
              prixUnitaireTtc: article.prixUnitaireTtc ?? 0,
              photo: article.photo ?? '',
              categorieId: article.categorie?.id ?? 0,
              entrepriseId: article.entreprise?.id ?? 0

            }
            this.categorieResponseDto = article.categorie ?? {}
          }
        })
    }
  }


  cancel(): void {
    this.router.navigate(['articles'])
  }

  enregistrerArticle(): void {
    // La catégorie est déjà liée via articleRequestDto.categorieId
    
    if (this.isEditMode) {
      // Mode modification
      const idArticle = this.activatedRoute.snapshot.params['idArticle'];
      this.articleService.updateArticle(idArticle, this.articleRequestDto, this.selectedImage || undefined)
        .subscribe({
          next : (art)=>{
              this.router.navigate(['articles'])
          }, error: (error)=>{
            console.log(error.error?.errors || error.message)
            this.errorMsg = error.error?.errors || [error.message || 'Erreur lors de la modification']
        }
        })
    } else {
      // Mode création
      this.articleService.enregistrerArticle(this.articleRequestDto, this.selectedImage || undefined)
        .subscribe({
          next : (art)=>{
              this.router.navigate(['articles'])
          }, error: (error)=>{
            console.log(error.error?.errors || error.message)
            this.errorMsg = error.error?.errors || [error.message || 'Erreur lors de la création']
        }
        })
    }
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
    }
  }

  calculerTTC() : void {
    if(this.articleRequestDto.prixUnitaire && this.articleRequestDto.tauxTva){
      this.articleRequestDto.prixUnitaireTtc =
        +this.articleRequestDto.prixUnitaire + (+(this.articleRequestDto.prixUnitaire * (this.articleRequestDto.tauxTva/100)))
    }
  }
}
