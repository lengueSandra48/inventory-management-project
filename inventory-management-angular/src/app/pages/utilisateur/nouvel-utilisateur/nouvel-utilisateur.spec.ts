import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelUtilisateur } from './nouvel-utilisateur';

describe('NouvelUtilisateur', () => {
  let component: NouvelUtilisateur;
  let fixture: ComponentFixture<NouvelUtilisateur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouvelUtilisateur]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelUtilisateur);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
