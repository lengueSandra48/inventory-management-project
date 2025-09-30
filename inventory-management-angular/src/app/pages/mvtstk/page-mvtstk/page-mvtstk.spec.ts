import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageMvtstk } from './page-mvtstk';

describe('PageMvtstk', () => {
  let component: PageMvtstk;
  let fixture: ComponentFixture<PageMvtstk>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageMvtstk]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageMvtstk);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
