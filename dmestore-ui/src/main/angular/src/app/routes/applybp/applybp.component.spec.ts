import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplybpComponent } from './applybp.component';

describe('ApplybpComponent', () => {
  let component: ApplybpComponent;
  let fixture: ComponentFixture<ApplybpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplybpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplybpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
