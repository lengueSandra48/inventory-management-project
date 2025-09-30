import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageClient } from './page-client';

describe('PageClient', () => {
  let component: PageClient;
  let fixture: ComponentFixture<PageClient>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageClient]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageClient);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
