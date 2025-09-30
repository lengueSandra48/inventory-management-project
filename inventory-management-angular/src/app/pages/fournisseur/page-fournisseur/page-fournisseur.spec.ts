import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageFournisseur } from './page-fournisseur';

describe('PageFournisseur', () => {
  let component: PageFournisseur;
  let fixture: ComponentFixture<PageFournisseur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageFournisseur]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageFournisseur);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
