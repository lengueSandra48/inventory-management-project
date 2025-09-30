import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailMvtStk } from './detail-mvt-stk';

describe('DetailMvtStk', () => {
  let component: DetailMvtStk;
  let fixture: ComponentFixture<DetailMvtStk>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailMvtStk]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailMvtStk);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
