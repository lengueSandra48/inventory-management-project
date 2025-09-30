import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailCmdCltFrs } from './detail-cmd-clt-frs';

describe('DetailCmdCltFrs', () => {
  let component: DetailCmdCltFrs;
  let fixture: ComponentFixture<DetailCmdCltFrs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailCmdCltFrs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailCmdCltFrs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
