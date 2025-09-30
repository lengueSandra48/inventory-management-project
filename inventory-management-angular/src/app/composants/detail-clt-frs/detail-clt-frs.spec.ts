import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCltFrs } from './detail-clt-frs';

describe('DetailCltFrs', () => {
  let component: DetailCltFrs;
  let fixture: ComponentFixture<DetailCltFrs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailCltFrs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCltFrs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
