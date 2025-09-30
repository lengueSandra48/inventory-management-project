import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ClientRequestDto} from '../../../gs-api/src/model/clientRequestDto';
import {FormsModule} from '@angular/forms';
import {Cltfrs} from '../../services/cltfrs/cltfrs';
import {EntrepriseResponseDto} from '../../../gs-api/src';
import {FournisseurRequestDto} from '../../../gs-api/src/model/fournisseurRequestDto';
import {CommonModule} from '@angular/common';
import {EntrepriseService} from '../../services/entreprise/entreprise';

@Component({
  selector: 'app-nouveau-clt-frs',
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './nouveau-clt-frs.html',
  styleUrl: './nouveau-clt-frs.css'
})
export class NouveauCltFrs implements OnInit {
  origin = '';
  imagePreview: string | null = null;
  selectedImage: File | null = null;
  selectedEntreprise: EntrepriseResponseDto | null = null;
  listeEntreprises: EntrepriseResponseDto[] = [];

 clientFournisseur: ClientRequestDto={
   nom: '',
   prenom: '',
   adresse: {
     adresse1: '',
     adresse2: '',
     ville: '',
     codePostal: '',
     pays: ''
   },
   photo: '',
   email: '',
   numTel: '',
   entrepriseId: 0
 }
  errorMsg: Array<string>=[];

  constructor(
    private readonly router: Router,
    private readonly activateRoute: ActivatedRoute,
    private readonly cltFrsService: Cltfrs,
    private readonly entrepriseService: EntrepriseService
  ) {}

  ngOnInit(): void{
    this.activateRoute.data.subscribe(data =>{
      this.origin = data['origin']
    });
    
    // Check if we're editing an existing client/fournisseur
    const id = this.activateRoute.snapshot.paramMap.get('id');
    if (id) {
      this.loadClientFournisseur(parseInt(id, 10));
    }
    
    this.loadEntreprises();
  }

  loadEntreprises(): void {
    this.entrepriseService.findAll().subscribe({
      next: (entreprises) => {
        this.listeEntreprises = entreprises;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des entreprises:', error);
      }
    });
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onEntrepriseChange(): void {
    this.clientFournisseur.entrepriseId = this.selectedEntreprise?.id || 0;
  }

  isEditMode(): boolean {
    return !!this.activateRoute.snapshot.paramMap.get('id');
  }

  getPageTitle(): string {
    const action = this.isEditMode() ? 'Modifier' : 'Nouveau';
    const entity = this.origin === 'client' ? 'client' : 'fournisseur';
    return `${action} ${entity}`;
  }

 enregistrer(): void{
    this.errorMsg = [];
    const id = this.activateRoute.snapshot.paramMap.get('id');
    
    if (this.origin === 'client'){
      if (id) {
        // Modification d'un client existant
        this.cltFrsService.updateClient(parseInt(id, 10), this.mapToClient(), this.selectedImage || undefined)
          .subscribe({
            next: (client: any) => {
              this.router.navigate(['clients'])
            },
            error: (error: any) => {
              this.errorMsg = error.error.errors;
            }
          });
      } else {
        // Création d'un nouveau client
        this.cltFrsService.enregistrerClient(this.mapToClient(), this.selectedImage || undefined)
          .subscribe({
            next: (client) => {
              this.router.navigate(['clients'])
            },
            error: (error) => {
              this.errorMsg = error.error.errors;
            }
          });
      }
    }else if (this.origin === 'fournisseur'){
      if (id) {
        // Modification d'un fournisseur existant
        this.cltFrsService.updateFournisseur(parseInt(id, 10), this.mapToFournisseur(), this.selectedImage || undefined)
          .subscribe({
            next: (fournisseur: any) => {
              this.router.navigate(['fournisseurs'])
            },
            error: (error: any) => {
              this.errorMsg = error.error.errors;
            }
          });
      } else {
        // Création d'un nouveau fournisseur
        this.cltFrsService.enregistrerFournisseur(this.mapToFournisseur(), this.selectedImage || undefined)
          .subscribe({
            next: (fournisseur) => {
              this.router.navigate(['fournisseurs'])
            },
            error: (error) => {
              this.errorMsg = error.error.errors;
            }
          });
      }
    }
  }

  cancelClick():void {
    if(this.origin === 'client'){
      this.router.navigate(['clients'])
    }else if (this.origin === 'fournisseur'){
      this.router.navigate(['fournisseurs'])
    }
  }

  mapToClient(): ClientRequestDto{
    const clientDto: ClientRequestDto = this.clientFournisseur
    return clientDto
  }

  loadClientFournisseur(id: number): void {
    if (this.origin === 'client') {
      this.cltFrsService.findclientById(id)
        .subscribe({
          next: (client) => {
            this.clientFournisseur = {
              nom: client.nom || '',
              prenom: client.prenom || '',
              adresse: {
                adresse1: client.adresse?.adresse1 || '',
                adresse2: client.adresse?.adresse2 || '',
                ville: client.adresse?.ville || '',
                codePostal: client.adresse?.codePostal || '',
                pays: client.adresse?.pays || ''
              },
              photo: client.photo || '',
              email: client.email || '',
              numTel: client.numTel || '',
              entrepriseId: client.entreprise?.id || 0
            };
            
            // Set selected entreprise
            this.selectedEntreprise = this.listeEntreprises.find(e => e.id === client.entreprise?.id) || null;
            
            // Set image preview if exists
            if (client.photo) {
              this.imagePreview = client.photo;
            }
          },
          error: (error) => {
            console.error('Erreur lors du chargement du client:', error);
            this.errorMsg = ['Erreur lors du chargement des données'];
          }
        });
    } else if (this.origin === 'fournisseur') {
      this.cltFrsService.findfournisseurById(id)
        .subscribe({
          next: (fournisseur) => {
            this.clientFournisseur = {
              nom: fournisseur.nom || '',
              prenom: fournisseur.prenom || '',
              adresse: {
                adresse1: fournisseur.adresse?.adresse1 || '',
                adresse2: fournisseur.adresse?.adresse2 || '',
                ville: fournisseur.adresse?.ville || '',
                codePostal: fournisseur.adresse?.codePostal || '',
                pays: fournisseur.adresse?.pays || ''
              },
              photo: fournisseur.photo || '',
              email: fournisseur.email || '',
              numTel: fournisseur.numTel || '',
              entrepriseId: fournisseur.entreprise?.id || 0
            };
            
            // Set selected entreprise
            this.selectedEntreprise = this.listeEntreprises.find(e => e.id === fournisseur.entreprise?.id) || null;
            
            // Set image preview if exists
            if (fournisseur.photo) {
              this.imagePreview = fournisseur.photo;
            }
          },
          error: (error) => {
            console.error('Erreur lors du chargement du fournisseur:', error);
            this.errorMsg = ['Erreur lors du chargement des données'];
          }
        });
    }
  }

  mapToFournisseur(): FournisseurRequestDto{
    const fournisseurDto: FournisseurRequestDto = this.clientFournisseur
    return fournisseurDto
  }
}
