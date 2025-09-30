import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelleCmdCltFrs } from './nouvelle-cmd-clt-frs';

describe('NouvelleCmdCltFrs', () => {
  let component: NouvelleCmdCltFrs;
  let fixture: ComponentFixture<NouvelleCmdCltFrs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouvelleCmdCltFrs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelleCmdCltFrs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
