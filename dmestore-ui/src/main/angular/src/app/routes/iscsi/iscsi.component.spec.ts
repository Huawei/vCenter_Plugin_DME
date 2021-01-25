import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IscsiComponent } from './iscsi.component';

describe('IscsiComponent', () => {
  let component: IscsiComponent;
  let fixture: ComponentFixture<IscsiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IscsiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IscsiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
