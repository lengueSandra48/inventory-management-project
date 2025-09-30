import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageCategories } from './page-categories';

describe('PageCategories', () => {
  let component: PageCategories;
  let fixture: ComponentFixture<PageCategories>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageCategories]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageCategories);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
