import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageCmdCltFrs } from './page-cmd-clt-frs';

describe('PageCmdCltFrs', () => {
  let component: PageCmdCltFrs;
  let fixture: ComponentFixture<PageCmdCltFrs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageCmdCltFrs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageCmdCltFrs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
