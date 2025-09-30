import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelArticle } from './nouvel-article';

describe('NouvelArticle', () => {
  let component: NouvelArticle;
  let fixture: ComponentFixture<NouvelArticle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouvelArticle]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelArticle);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
