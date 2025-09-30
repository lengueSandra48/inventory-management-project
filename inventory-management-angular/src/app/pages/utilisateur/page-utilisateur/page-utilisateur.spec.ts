import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageUtilisateur } from './page-utilisateur';

describe('PageUtilisateur', () => {
  let component: PageUtilisateur;
  let fixture: ComponentFixture<PageUtilisateur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageUtilisateur]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageUtilisateur);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
