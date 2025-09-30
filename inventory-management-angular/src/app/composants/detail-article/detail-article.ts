import {Component, EventEmitter, Input, Output} from '@angular/core';
import { ArticleResponseDto} from '../../../gs-api/src';
import {Router} from '@angular/router';
import {Article} from '../../services/article/article';

@Component({
  selector: 'app-detail-article',
  imports: [],
  templateUrl: './detail-article.html',
  styleUrl: './detail-article.css'
})
export class DetailArticle {
  @Input() articleResponseDto : ArticleResponseDto={}
  @Output() suppressionResult = new EventEmitter()
  selectedIdArticleToDelete ? = -1

    constructor(
      private router:Router,
      private articleService: Article
    ) {
    }
  modifierArticle():void {
    this.router.navigate(['nouvelarticle', this.articleResponseDto.id])
  }

  confirmerEtSupprimerArticle():void {
   if(this.articleResponseDto.id){
      this.articleService.deleteArticle(this.articleResponseDto.id)
        .subscribe({
          next: (res)=>{
            console.log(this.articleResponseDto)
              this.suppressionResult.emit('success')
          },error: (error)=>{
            this.suppressionResult.emit(error.error.error)
            }
        })
    }
  }

  protected readonly origin = origin;
}
