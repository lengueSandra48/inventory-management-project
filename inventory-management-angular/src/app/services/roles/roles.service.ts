import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RolesControllerService, RolesResponseDto, RolesRequestDto } from '../../../gs-api/src';

@Injectable({
  providedIn: 'root'
})
export class RolesService {

  constructor(private rolesController: RolesControllerService) { }

  createRole(roleData: RolesRequestDto): Observable<RolesResponseDto> {
    return this.rolesController.save2(roleData);
  }

  getAllRoles(): Observable<RolesResponseDto[]> {
    return this.rolesController.findAll2();
  }

  getRoleById(id: number): Observable<RolesResponseDto> {
    return this.rolesController.findById2(id);
  }

  updateRole(id: number, roleData: RolesRequestDto): Observable<RolesResponseDto> {
    return this.rolesController.update2(id, roleData);
  }

  deleteRole(id: number): Observable<any> {
    return this.rolesController.delete2(id);
  }
}
