import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCmd } from './detail-cmd';

describe('DetailCmd', () => {
  let component: DetailCmd;
  let fixture: ComponentFixture<DetailCmd>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailCmd]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCmd);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
