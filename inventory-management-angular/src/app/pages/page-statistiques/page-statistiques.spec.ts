import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageStatistiques } from './page-statistiques';

describe('PageStatistiques', () => {
  let component: PageStatistiques;
  let fixture: ComponentFixture<PageStatistiques>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageStatistiques]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageStatistiques);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
