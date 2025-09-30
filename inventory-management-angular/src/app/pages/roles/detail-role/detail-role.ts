import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RolesService } from '../../../services/roles/roles.service';
import { RolesResponseDto } from '../../../../gs-api/src';

@Component({
  selector: 'app-detail-role',
  imports: [CommonModule],
  templateUrl: './detail-role.html',
  styleUrl: './detail-role.css'
})
export class DetailRole implements OnInit {
  
  role: any = null;
  isLoading = false;
  errorMsg = '';
  roleId: number = 0;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly rolesService: RolesService
  ) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];
    if (id) {
      this.roleId = +id;
      this.loadRole();
    }
  }

  loadRole(): void {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.rolesService.getRoleById(this.roleId).subscribe({
      next: (data: RolesResponseDto) => {
        this.role = data;
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMsg = 'Erreur lors du chargement du rôle: ' + (error.error?.message || error.message);
        this.isLoading = false;
      }
    });
  }

  retourALaListe(): void {
    this.router.navigate(['roles']);
  }

  modifier(): void {
    this.router.navigate(['nouveaurole', this.roleId]);
  }

  supprimer(): void {
    if (confirm(`Êtes-vous sûr de vouloir supprimer le rôle "${this.role?.roleName}" ?`)) {
      this.rolesService.deleteRole(this.roleId).subscribe({
        next: () => {
          this.router.navigate(['roles']);
        },
        error: (error) => {
          this.errorMsg = 'Erreur lors de la suppression: ' + (error.error?.message || error.message);
        }
      });
    }
  }
}
