import { Component } from '@angular/core';
import {DetailArticle} from '../detail-article/detail-article';
import {DetailCmd} from '../detail-cmd/detail-cmd';
import {ActivatedRoute, Router} from '@angular/router';
import {
  ArticleResponseDto,
  ClientResponseDto,
  CommandeClientControllerService, CommandeClientRequestDto, CommandeFournisseurRequestDto,
  LigneCommandeClientResponseDto
} from '../../../gs-api/src';
import {Cltfrs} from '../../services/cltfrs/cltfrs';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {ArticleRequestDto} from '../../../gs-api/src/model/articleRequestDto';
import {Article} from '../../services/article/article';
import {error} from 'ng-packagr/src/lib/utils/log';
import {Cmdcltfrs} from '../../services/cmdcltfrs/cmdcltfrs';

@Component({
  selector: 'app-nouvelle-cmd-clt-frs',
  imports: [
    DetailCmd,
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './nouvelle-cmd-clt-frs.html',
  styleUrl: './nouvelle-cmd-clt-frs.css'
})
export class NouvelleCmdCltFrs {

  origin = '';
  selectedClientFournisseur: ClientResponseDto={}
  listClientsFournisseurs: Array<ClientResponseDto> =[]
  searchArticleDto: ArticleResponseDto={}
  listArticle: Array<ArticleResponseDto>=[]
  articleErrorMsg = '';
  codeArticle= '';
  quantite='';

  lignesCommande : Array<LigneCommandeClientResponseDto>= []
  totalCommande = 0;
  articleNotYetSelected= false ;
  errorMsg: Array<string> = [];
  codeCommande='';

  constructor(
    private router: Router,
    private activateRoute: ActivatedRoute,
    private cltFrsService: Cltfrs,
    private articleService: Article,
    private commandeClientService: CommandeClientControllerService,
    private cmdCltFrsService: Cmdcltfrs
  ) {}

  ngOnInit(): void{
    this.activateRoute.data.subscribe(data =>{
      this.origin = data['origin']
    })
    
    // Check if we're editing an existing command
    const id = this.activateRoute.snapshot.paramMap.get('id');
    if (id) {
      this.loadCommandeForEdit(parseInt(id, 10));
    }
    
    this.findAll()
    this.findAllArticles()
  }

  cancel() {
    if (this.origin === 'client') {
      this.router.navigate(['commandesclient']);
    } else {
      this.router.navigate(['commandesfournisseur']);
    }
  }

  isEditMode(): boolean {
    return !!this.activateRoute.snapshot.paramMap.get('id');
  }

  getPageTitle(): string {
    const action = this.isEditMode() ? 'Modifier' : 'Nouvelle';
    const entity = this.origin === 'client' ? 'commande client' : 'commande fournisseur';
    return `${action} ${entity}`;
  }

  findAll(){
    if(this.origin === 'client'){
      this.cltFrsService.findAllClients()
        .subscribe({
          next: (clients)=>{
            this.listClientsFournisseurs = clients
          }
        })
    }else if(this.origin === 'fournisseur'){
      this.cltFrsService.findAllFournisseurs()
        .subscribe({
          next: (fournisseurs)=>{
            this.listClientsFournisseurs = fournisseurs
          }
        })
    }
  }

  findAllArticles():void{
    this.articleService.findAllArticle()
      .subscribe({
        next: (articles)=>{
          this.listArticle = articles
        }
      })
  }
    findArticleByCode(codeArticle: string): void{
     this.articleErrorMsg = ''
      if(codeArticle){
        this.articleService.findArticleByCode(codeArticle)
          .subscribe({
            next: (article)=>{
              this.searchArticleDto = article
            }, error: (error)=>{
              this.articleErrorMsg = error.error.message
        }
          })
      }
    }

  searchArticle(): void {
    if(this.codeArticle.length === 0){
      this.findAllArticles()
    }
      this.listArticle = this.listArticle
        .filter(art => art.codeArticle?.startsWith(this.codeArticle) ||
        art.designation?.startsWith(this.codeArticle))
  }

  ajouterLigneCommande(): void {
    const ligneCmdAlreadyExists = this.lignesCommande.find(lig =>
      lig.article?.codeArticle === this.searchArticleDto.codeArticle)
    if(ligneCmdAlreadyExists){
      this.lignesCommande.forEach(lig=>{
        if(lig && lig.article?.codeArticle === this.searchArticleDto.codeArticle){
          // @ts-ignore
          lig.quantite = +lig.quantite + +this.quantite
        }
      })
      this.quantite = ligneCmdAlreadyExists.quantite + this.quantite
    }else{
      const ligneCmd: LigneCommandeClientResponseDto = {
        article: this.searchArticleDto,
        prixUnitaire: this.searchArticleDto.prixUnitaireTtc,
        quantite: +this.quantite
      }

      this.lignesCommande.push(ligneCmd)

    }
    this.totalCommande = 0
    this.lignesCommande.forEach(ligne=>{
      if(ligne.prixUnitaire && ligne.quantite){
        this.totalCommande += +ligne.prixUnitaire * +ligne.quantite
      }

    })

    this.searchArticleDto = {}
    this.quantite = ''
    this.codeArticle=''
    this.articleNotYetSelected = false
    this.findAllArticles()
  }

  selectArticle(article: ArticleResponseDto) {
    this.searchArticleDto =article
    this.codeArticle = article.codeArticle ? article.codeArticle : ''
    this.articleNotYetSelected = true
  }

  enregistrerCommande(): void {
    // Validation avant envoi
    if (!this.selectedClientFournisseur?.id) {
      this.errorMsg = [`Veuillez sélectionner un ${this.origin === 'client' ? 'client' : 'fournisseur'}`];
      return;
    }

    if (!this.codeCommande || this.codeCommande.trim() === '') {
      this.errorMsg = ['Le code de commande est obligatoire'];
      return;
    }

    if (this.lignesCommande.length === 0) {
      this.errorMsg = ['Veuillez ajouter au moins un article à la commande'];
      return;
    }

    const commande = this.preparerCommande();
    const id = this.activateRoute.snapshot.paramMap.get('id');
    
    if(this.origin==='client'){
      if (id) {
        // Modification d'une commande client existante
        this.cmdCltFrsService.updateCommandeClient(parseInt(id, 10), commande as CommandeClientRequestDto)
          .subscribe({
            next: (cmd)=>{
              this.router.navigate(['commandesclient'])
            }, error:(error)=>{
              console.error('Erreur lors de la modification de la commande client:', error);
              this.errorMsg = error.error?.errors || [error.error?.message || 'Erreur lors de la modification de la commande'];
            }
          })
      } else {
        // Création d'une nouvelle commande client
        this.cmdCltFrsService.enregistrerCommandeClient(commande as CommandeClientRequestDto)
          .subscribe({
            next: (cmd)=>{
              this.router.navigate(['commandesclient'])
            }, error:(error)=>{
              console.error('Erreur lors de la création de la commande client:', error);
              this.errorMsg = error.error?.errors || [error.error?.message || 'Erreur lors de la création de la commande'];
            }
          })
      }
    }else if(this.origin==='fournisseur'){
      if (id) {
        // Modification d'une commande fournisseur existante
        this.cmdCltFrsService.updateCommandeFournisseur(parseInt(id, 10), commande as CommandeFournisseurRequestDto)
          .subscribe({
            next: (cmd)=>{
              this.router.navigate(['commandesfournisseur'])
            }, error:(error)=>{
              console.error('Erreur lors de la modification de la commande fournisseur:', error);
              this.errorMsg = error.error?.errors || [error.error?.message || 'Erreur lors de la modification de la commande'];
            }
          })
      } else {
        // Création d'une nouvelle commande fournisseur
        this.cmdCltFrsService.enregistrerCommandeFournisseur(commande as CommandeFournisseurRequestDto)
          .subscribe({
            next: (cmd)=>{
              this.router.navigate(['commandesfournisseur'])
            }, error:(error)=>{
              console.error('Erreur lors de la création de la commande fournisseur:', error);
              this.errorMsg = error.error?.errors || [error.error?.message || 'Erreur lors de la création de la commande'];
            }
          })
      }
    }
  }

  loadCommandeForEdit(commandeId: number): void {
    if (this.origin === 'client') {
      this.cmdCltFrsService.findAllLigneCommandesClient(commandeId)
        .subscribe({
          next: (commande: any) => {
            this.codeCommande = commande.code || '';
            this.selectedClientFournisseur = commande.client || { id: commande.clientId };
            this.lignesCommande = commande.ligneCommandeClients || [];
            this.calculateTotal();
          },
          error: (error: any) => {
            console.error('Erreur lors du chargement de la commande client:', error);
            this.errorMsg = ['Erreur lors du chargement de la commande'];
          }
        });
    } else if (this.origin === 'fournisseur') {
      this.cmdCltFrsService.findAllLigneCommandesFournisseur(commandeId)
        .subscribe({
          next: (commande: any) => {
            this.codeCommande = commande.code || '';
            this.selectedClientFournisseur = commande.fournisseur || { id: commande.fournisseurId };
            this.lignesCommande = commande.ligneCommandeFournisseurs || [];
            this.calculateTotal();
          },
          error: (error: any) => {
            console.error('Erreur lors du chargement de la commande fournisseur:', error);
            this.errorMsg = ['Erreur lors du chargement de la commande'];
          }
        });
    }
  }

  calculateTotal(): void {
    this.totalCommande = 0;
    this.lignesCommande.forEach(ligne => {
      if (ligne.prixUnitaire && ligne.quantite) {
        this.totalCommande += +ligne.prixUnitaire * +ligne.quantite;
      }
    });
  }

  supprimerLigneCommande(ligne: LigneCommandeClientResponseDto): void {
    const index = this.lignesCommande.findIndex(l => 
      l.article?.codeArticle === ligne.article?.codeArticle
    );
    
    if (index > -1) {
      this.lignesCommande.splice(index, 1);
      this.calculateTotal();
    }
  }

  private preparerCommande(): any{
    if(this.origin === 'client'){
      return {
        code: this.codeCommande,
        dateCommande: new Date().toISOString(), // Format ISO complet
        clientId: this.selectedClientFournisseur.id,
        entrepriseId: 1 // Will be set by service
      }
    } else if(this.origin === 'fournisseur'){
      return {
        code: this.codeCommande,
        dateCommande: new Date().toISOString(), // Format ISO complet
        fournisseurId: this.selectedClientFournisseur.id,
        entrepriseId: 1 // Will be set by service
      }
    }
  }

}
