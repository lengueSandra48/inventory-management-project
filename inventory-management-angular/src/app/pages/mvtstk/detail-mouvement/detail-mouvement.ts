import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MvtStkControllerService } from '../../../../gs-api/src/api/mvtStkController.service';
import { MvtStkResponseDto } from '../../../../gs-api/src/model/models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-mouvement',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './detail-mouvement.html',
  styleUrls: ['./detail-mouvement.css']
})
export class DetailMouvement implements OnInit {
  
  mouvement: MvtStkResponseDto | null = null;
  isLoading = true;
  errorMsg = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly mvtStkService: MvtStkControllerService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadMouvement(+id);
    }
  }

  loadMouvement(id: number): void {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.mvtStkService.findById3(id).subscribe({
      next: (response: MvtStkResponseDto) => {
        this.mouvement = response;
        this.isLoading = false;
      },
      error: (error: any) => {
        console.error('Erreur lors du chargement du mouvement:', error);
        this.errorMsg = 'Erreur lors du chargement du mouvement de stock';
        this.isLoading = false;
      }
    });
  }

  retour(): void {
    this.router.navigate(['/mvtstk']);
  }

  modifier(): void {
    this.router.navigate(['nouveaumouvement', this.mouvement?.id]);
  }

  supprimer(): void {
    if (this.mouvement && confirm('Êtes-vous sûr de vouloir supprimer ce mouvement ?')) {
      this.mvtStkService.delete3(this.mouvement.id!).subscribe({
        next: () => {
          this.router.navigate(['/mvtstk']);
        },
        error: (error: any) => {
          console.error('Erreur lors de la suppression:', error);
          this.errorMsg = 'Erreur lors de la suppression du mouvement';
        }
      });
    }
  }

  getTypeLabel(type: string): string {
    switch (type) {
      case 'ENTREE':
        return 'Entrée';
      case 'SORTIE':
        return 'Sortie';
      case 'CORRECTION_POS':
        return 'Correction positive';
      case 'CORRECTION_NEG':
        return 'Correction négative';
      default:
        return type;
    }
  }

  getBadgeClass(type: string): string {
    switch (type) {
      case 'ENTREE':
        return 'badge badge-success';
      case 'SORTIE':
        return 'badge badge-danger';
      case 'CORRECTION_POS':
        return 'badge badge-info';
      case 'CORRECTION_NEG':
        return 'badge badge-warning';
      default:
        return 'badge badge-secondary';
    }
  }

  getTypeMouvementLabel(type: string): string {
    switch (type) {
      case 'ENTREE': return 'Entrée';
      case 'SORTIE': return 'Sortie';
      case 'CORRECTION_POS': return 'Correction Positive';
      case 'CORRECTION_NEG': return 'Correction Négative';
      default: return type;
    }
  }

  getTypeMouvementClass(type: string): string {
    switch (type) {
      case 'ENTREE': return 'badge-entree';
      case 'SORTIE': return 'badge-sortie';
      default: return 'badge-default';
    }
  }
}
