import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageArticle } from './page-article';

describe('PageArticle', () => {
  let component: PageArticle;
  let fixture: ComponentFixture<PageArticle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageArticle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageArticle);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
