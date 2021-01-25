import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LogicportComponent } from './logicport.component';

describe('LogicportComponent', () => {
  let component: LogicportComponent;
  let fixture: ComponentFixture<LogicportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LogicportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LogicportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
