import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NfsPerformanceComponent } from './performance.component';

describe('PerformanceComponent', () => {
  let component: NfsPerformanceComponent;
  let fixture: ComponentFixture<NfsPerformanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NfsPerformanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NfsPerformanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
