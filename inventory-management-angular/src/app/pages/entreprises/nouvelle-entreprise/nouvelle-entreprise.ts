import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EntrepriseService } from '../../../services/entreprise/entreprise';

// Interface locale pour EntrepriseRequestDto
interface EntrepriseRequestDto {
  nomEntreprise?: string;
  description?: string;
  email?: string;
  codeFiscal?: string;
  numTel?: string;
  steWeb?: string;
  photo?: string;
  adresse?: {
    adresse1?: string;
    adresse2?: string;
    ville?: string;
    codePostal?: string;
    pays?: string;
  };
}

@Component({
  selector: 'app-nouvelle-entreprise',
  templateUrl: './nouvelle-entreprise.html',
  styleUrls: ['./nouvelle-entreprise.scss'],
  imports: [FormsModule, CommonModule]
})
export class NouvelleEntrepriseComponent implements OnInit {

  entrepriseRequestDto: EntrepriseRequestDto = {
    nomEntreprise: '',
    description: '',
    email: '',
    codeFiscal: '',
    numTel: '',
    steWeb: '',
    adresse: {
      adresse1: '',
      adresse2: '',
      ville: '',
      codePostal: '',
      pays: ''
    }
  };

  // Getters pour éviter les erreurs "Object is possibly undefined"
  get adresse1() { return this.entrepriseRequestDto.adresse?.adresse1 || ''; }
  set adresse1(value: string) { 
    this.entrepriseRequestDto.adresse ??= {};
    this.entrepriseRequestDto.adresse.adresse1 = value; 
  }

  get adresse2() { return this.entrepriseRequestDto.adresse?.adresse2 || ''; }
  set adresse2(value: string) { 
    this.entrepriseRequestDto.adresse ??= {};
    this.entrepriseRequestDto.adresse.adresse2 = value; 
  }

  get ville() { return this.entrepriseRequestDto.adresse?.ville || ''; }
  set ville(value: string) { 
    this.entrepriseRequestDto.adresse ??= {};
    this.entrepriseRequestDto.adresse.ville = value; 
  }

  get codePostal() { return this.entrepriseRequestDto.adresse?.codePostal || ''; }
  set codePostal(value: string) { 
    this.entrepriseRequestDto.adresse ??= {};
    this.entrepriseRequestDto.adresse.codePostal = value; 
  }

  get pays() { return this.entrepriseRequestDto.adresse?.pays || ''; }
  set pays(value: string) { 
    this.entrepriseRequestDto.adresse ??= {};
    this.entrepriseRequestDto.adresse.pays = value; 
  }

  errorMsg: Array<string> = [];
  isEditMode = false;
  selectedImage: File | null = null;
  imagePreview: string | null = null;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private entrepriseService: EntrepriseService
  ) { }

  ngOnInit(): void {
    const idEntreprise = this.activatedRoute.snapshot.params['idEntreprise'];
    if (idEntreprise) {
      this.isEditMode = true;
      this.entrepriseService.findById(idEntreprise)
        .subscribe({
          next: (entreprise) => {
            this.entrepriseRequestDto = {
              nomEntreprise: entreprise.nomEntreprise,
              description: entreprise.description,
              email: entreprise.email,
              codeFiscal: entreprise.codeFiscal,
              numTel: entreprise.numTel,
              steWeb: entreprise.steWeb,
              adresse: {
                adresse1: entreprise.adresse?.adresse1 || '',
                adresse2: entreprise.adresse?.adresse2 || '',
                ville: entreprise.adresse?.ville || '',
                codePostal: entreprise.adresse?.codePostal || '',
                pays: entreprise.adresse?.pays || ''
              }
            };
          },
          error: (error) => {
            console.error('Erreur lors du chargement de l\'entreprise:', error);
            this.errorMsg = ['Erreur lors du chargement de l\'entreprise'];
          }
        });
    }
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      
      // Prévisualisation de l'image
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  cancel(): void {
    this.router.navigate(['entreprises']);
  }

  enregistrerEntreprise(): void {
    this.errorMsg = [];
    
    if (this.isEditMode) {
      // Mode modification
      const idEntreprise = this.activatedRoute.snapshot.params['idEntreprise'];
      this.entrepriseService.update(idEntreprise, this.entrepriseRequestDto, this.selectedImage || undefined)
        .subscribe({
          next: () => {
            this.router.navigate(['entreprises']);
          },
          error: (error) => {
            console.error('Erreur lors de la modification:', error);
            this.errorMsg = error.error?.errors || ['Erreur lors de la modification de l\'entreprise'];
          }
        });
    } else {
      // Mode création
      this.entrepriseService.create(this.entrepriseRequestDto, this.selectedImage || undefined)
        .subscribe({
          next: () => {
            this.router.navigate(['entreprises']);
          },
          error: (error) => {
            console.error('Erreur lors de la création:', error);
            this.errorMsg = error.error?.errors || ['Erreur lors de la création de l\'entreprise'];
          }
        });
    }
  }
}
